package com.spadilla89.proxyuniverse.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.spadilla89.proxyuniverse.data.model.Proxy
import com.spadilla89.proxyuniverse.data.model.ProxyProtocol
import com.spadilla89.proxyuniverse.ui.components.*
import com.spadilla89.proxyuniverse.utils.ClipboardHelper
import com.spadilla89.proxyuniverse.utils.FileExporter
import com.spadilla89.proxyuniverse.viewmodel.ProxyViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProxyScreen(
    protocol: ProxyProtocol,
    viewModel: ProxyViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var showExportDialog by remember { mutableStateOf(false) }
    
    val filteredProxies = viewModel.getFilteredProxies()
    val selectedProxies = viewModel.getSelectedProxies()
    
    // Show messages
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            viewModel.clearMessages()
        }
    }
    
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearMessages()
        }
    }
    
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Filter Section
            FilterSection(
                countries = uiState.availableCountries,
                selectedAnonymityLevels = uiState.selectedAnonymityLevels,
                onCountriesChanged = { viewModel.updateCountrySelection(it) },
                onAnonymityChanged = { viewModel.updateAnonymitySelection(it) }
            )
            
            // Search bar
            if (uiState.proxies.isNotEmpty()) {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Search proxies...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true
                )
            }
            
            // Proxy list controls
            if (filteredProxies.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${selectedProxies.size} selected of ${filteredProxies.size} total",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(onClick = { viewModel.selectAllProxies() }) {
                            Text("Select All")
                        }
                        TextButton(onClick = { viewModel.deselectAllProxies() }) {
                            Text("Clear")
                        }
                    }
                }
            }
            
            // Proxy list
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Obtaining proxies...")
                    }
                }
            } else if (uiState.isValidating) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        uiState.validationProgress?.let { (completed, total) ->
                            Text("Validating proxies: $completed / $total")
                            LinearProgressIndicator(
                                progress = completed.toFloat() / total.toFloat(),
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .padding(top = 8.dp)
                            )
                        }
                    }
                }
            } else if (filteredProxies.isEmpty() && uiState.proxies.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No proxies. Press the button to fetch proxies.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else if (filteredProxies.isEmpty() && uiState.proxies.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No proxies match your search.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(filteredProxies, key = { it.getUniqueId() }) { proxy ->
                        ProxyListItem(
                            proxy = proxy,
                            isSelected = proxy.getUniqueId() in uiState.selectedProxies,
                            onSelectionChange = { viewModel.toggleProxySelection(proxy) },
                            onCopyClick = {
                                ClipboardHelper.copyToClipboard(
                                    context,
                                    proxy.toFormattedString()
                                )
                                Toast.makeText(
                                    context,
                                    "Copied: ${proxy.toFormattedString()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                }
            }
            
            // Bottom action bar
            if (uiState.proxies.isNotEmpty()) {
                BottomAppBar(
                    actions = {
                        IconButton(
                            onClick = { 
                                if (selectedProxies.isNotEmpty()) {
                                    viewModel.validateSelectedProxies()
                                }
                            },
                            enabled = selectedProxies.isNotEmpty() && !uiState.isValidating
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Validate Selected")
                        }
                        
                        IconButton(
                            onClick = { viewModel.validateAllProxies() },
                            enabled = !uiState.isValidating
                        ) {
                            Icon(Icons.Default.Done, contentDescription = "Validate All")
                        }
                        
                        IconButton(
                            onClick = { showExportDialog = true },
                            enabled = !uiState.isLoading && !uiState.isValidating
                        ) {
                            Icon(Icons.Default.Save, contentDescription = "Export/Copy")
                        }
                    }
                )
            }
        }
        
        // Floating Action Button for fetching proxies
        FloatingActionButton(
            onClick = { viewModel.fetchProxies(protocol) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .offset(y = (-56).dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Default.Refresh, contentDescription = "Get Proxies")
        }
    }
    
    // Export dialog
    if (showExportDialog) {
        ExportDialog(
            totalProxies = filteredProxies.size,
            selectedProxies = selectedProxies.size,
            onExportAll = {
                scope.launch {
                    val path = FileExporter.exportProxies(context, filteredProxies, protocol)
                    if (path != null) {
                        Toast.makeText(
                            context,
                            "Exported ${filteredProxies.size} proxies to $path",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Failed to export proxies",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            },
            onExportSelected = {
                scope.launch {
                    val path = FileExporter.exportProxies(context, selectedProxies, protocol)
                    if (path != null) {
                        Toast.makeText(
                            context,
                            "Exported ${selectedProxies.size} proxies to $path",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Failed to export proxies",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            },
            onCopyAmount = { amount ->
                val proxiesToCopy = filteredProxies.take(amount)
                val text = FileExporter.formatProxiesAsString(proxiesToCopy)
                ClipboardHelper.copyToClipboard(context, text)
                Toast.makeText(
                    context,
                    "$amount proxies copied to clipboard",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onDismiss = { showExportDialog = false }
        )
    }
}
