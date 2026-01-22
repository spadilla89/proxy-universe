package com.spadilla89.proxyuniverse.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spadilla89.proxyuniverse.data.model.AnonymityLevel
import com.spadilla89.proxyuniverse.data.model.Country

@Composable
fun FilterSection(
    countries: List<Country>,
    selectedAnonymityLevels: Set<AnonymityLevel>,
    onCountriesChanged: (List<Country>) -> Unit,
    onAnonymityChanged: (Set<AnonymityLevel>) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCountryDialog by remember { mutableStateOf(false) }
    var expandedFilters by remember { mutableStateOf(false) }
    
    val selectedCountries = countries.filter { it.isSelected }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filters",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Filters",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                
                TextButton(onClick = { expandedFilters = !expandedFilters }) {
                    Text(if (expandedFilters) "Hide" else "Show")
                }
            }
            
            if (expandedFilters) {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                // Country filter
                OutlinedButton(
                    onClick = { showCountryDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        text = if (selectedCountries.isEmpty()) {
                            "Select Countries (All)"
                        } else {
                            "Countries: ${selectedCountries.size} selected"
                        }
                    )
                }
                
                // Anonymity filter
                AnonymityFilter(
                    selectedLevels = selectedAnonymityLevels,
                    onLevelsChanged = onAnonymityChanged
                )
                
                // Active filters summary
                if (selectedCountries.isNotEmpty() || selectedAnonymityLevels.isNotEmpty()) {
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    Text(
                        text = "Active Filters:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    if (selectedCountries.isNotEmpty()) {
                        Text(
                            text = "• ${selectedCountries.size} countries",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    
                    if (selectedAnonymityLevels.isNotEmpty()) {
                        Text(
                            text = "• Anonymity: ${selectedAnonymityLevels.joinToString { it.displayName }}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    
                    // Clear all filters button
                    TextButton(
                        onClick = {
                            onCountriesChanged(countries.map { it.copy(isSelected = false) })
                            onAnonymityChanged(emptySet())
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Clear All Filters")
                    }
                }
            }
        }
    }
    
    // Country selector dialog
    if (showCountryDialog) {
        CountrySelector(
            countries = countries,
            onCountriesSelected = onCountriesChanged,
            onDismiss = { showCountryDialog = false }
        )
    }
}
