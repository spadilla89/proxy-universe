package com.spadilla89.proxyuniverse.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spadilla89.proxyuniverse.data.model.AnonymityLevel
import com.spadilla89.proxyuniverse.data.model.Country
import com.spadilla89.proxyuniverse.data.model.Proxy
import com.spadilla89.proxyuniverse.data.model.ProxyProtocol
import com.spadilla89.proxyuniverse.data.repository.ProxyRepository
import com.spadilla89.proxyuniverse.utils.ProxyValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProxyUiState(
    val proxies: List<Proxy> = emptyList(),
    val selectedProxies: Set<String> = emptySet(), // Set of proxy unique IDs
    val isLoading: Boolean = false,
    val isValidating: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val validationProgress: Pair<Int, Int>? = null, // (completed, total)
    
    // Filters
    val availableCountries: List<Country> = Country.getAllCountries(),
    val selectedCountries: List<Country> = emptyList(),
    val selectedAnonymityLevels: Set<AnonymityLevel> = emptySet(),
    val searchQuery: String = ""
)

class ProxyViewModel : ViewModel() {
    
    private val repository = ProxyRepository()
    
    private val _uiState = MutableStateFlow(ProxyUiState())
    val uiState: StateFlow<ProxyUiState> = _uiState.asStateFlow()
    
    /**
     * Fetch proxies based on current protocol and filters
     */
    fun fetchProxies(protocol: ProxyProtocol) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                val countryCodes = _uiState.value.selectedCountries
                    .map { it.code }
                    .takeIf { it.isNotEmpty() }
                
                val anonymityLevels = _uiState.value.selectedAnonymityLevels
                    .takeIf { it.isNotEmpty() }?.toList()
                
                val proxies = repository.fetchProxies(
                    protocol = protocol,
                    countries = countryCodes,
                    anonymityLevels = anonymityLevels
                )
                
                _uiState.update { 
                    it.copy(
                        proxies = proxies,
                        isLoading = false,
                        selectedProxies = emptySet(),
                        successMessage = "Found ${proxies.size} proxies"
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = "Error fetching proxies: ${e.message}"
                    )
                }
            }
        }
    }
    
    /**
     * Validate selected proxies
     */
    fun validateSelectedProxies() {
        val selectedIds = _uiState.value.selectedProxies
        if (selectedIds.isEmpty()) return
        
        val proxiesToValidate = _uiState.value.proxies.filter { 
            it.getUniqueId() in selectedIds 
        }
        
        validateProxies(proxiesToValidate)
    }
    
    /**
     * Validate all proxies
     */
    fun validateAllProxies() {
        validateProxies(_uiState.value.proxies)
    }
    
    /**
     * Validate a list of proxies
     */
    private fun validateProxies(proxiesToValidate: List<Proxy>) {
        viewModelScope.launch {
            _uiState.update { 
                it.copy(
                    isValidating = true,
                    validationProgress = Pair(0, proxiesToValidate.size)
                )
            }
            
            try {
                val validatedProxies = ProxyValidator.validateProxies(
                    proxies = proxiesToValidate,
                    onProgress = { completed, total ->
                        _uiState.update { 
                            it.copy(validationProgress = Pair(completed, total))
                        }
                    }
                )
                
                // Update the validated proxies in the list
                val updatedProxies = _uiState.value.proxies.map { proxy ->
                    validatedProxies.find { it.getUniqueId() == proxy.getUniqueId() } ?: proxy
                }
                
                val workingCount = validatedProxies.count { it.isWorking == true }
                val failedCount = validatedProxies.count { it.isWorking == false }
                
                _uiState.update { 
                    it.copy(
                        proxies = updatedProxies,
                        isValidating = false,
                        validationProgress = null,
                        successMessage = "Validation complete: $workingCount working, $failedCount failed"
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isValidating = false,
                        validationProgress = null,
                        error = "Validation error: ${e.message}"
                    )
                }
            }
        }
    }
    
    /**
     * Toggle proxy selection
     */
    fun toggleProxySelection(proxy: Proxy) {
        val id = proxy.getUniqueId()
        _uiState.update { state ->
            val newSelection = if (id in state.selectedProxies) {
                state.selectedProxies - id
            } else {
                state.selectedProxies + id
            }
            state.copy(selectedProxies = newSelection)
        }
    }
    
    /**
     * Select all proxies
     */
    fun selectAllProxies() {
        val allIds = _uiState.value.proxies.map { it.getUniqueId() }.toSet()
        _uiState.update { it.copy(selectedProxies = allIds) }
    }
    
    /**
     * Deselect all proxies
     */
    fun deselectAllProxies() {
        _uiState.update { it.copy(selectedProxies = emptySet()) }
    }
    
    /**
     * Get selected proxies
     */
    fun getSelectedProxies(): List<Proxy> {
        val selectedIds = _uiState.value.selectedProxies
        return _uiState.value.proxies.filter { it.getUniqueId() in selectedIds }
    }
    
    /**
     * Update country selection
     */
    fun updateCountrySelection(countries: List<Country>) {
        _uiState.update { 
            it.copy(
                selectedCountries = countries.filter { country -> country.isSelected },
                availableCountries = countries
            )
        }
    }
    
    /**
     * Update anonymity level selection
     */
    fun updateAnonymitySelection(levels: Set<AnonymityLevel>) {
        _uiState.update { it.copy(selectedAnonymityLevels = levels) }
    }
    
    /**
     * Update search query for filtering displayed proxies
     */
    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }
    
    /**
     * Get filtered proxies based on search query
     */
    fun getFilteredProxies(): List<Proxy> {
        val query = _uiState.value.searchQuery.lowercase()
        if (query.isEmpty()) return _uiState.value.proxies
        
        return _uiState.value.proxies.filter { proxy ->
            proxy.ip.contains(query) ||
            proxy.port.toString().contains(query) ||
            proxy.country.lowercase().contains(query) ||
            proxy.toFormattedString().contains(query)
        }
    }
    
    /**
     * Clear messages
     */
    fun clearMessages() {
        _uiState.update { it.copy(error = null, successMessage = null) }
    }
    
    /**
     * Clear all proxies
     */
    fun clearProxies() {
        _uiState.update { 
            it.copy(
                proxies = emptyList(),
                selectedProxies = emptySet()
            )
        }
    }
}
