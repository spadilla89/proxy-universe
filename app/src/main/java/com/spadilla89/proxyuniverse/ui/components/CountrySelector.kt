package com.spadilla89.proxyuniverse.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.spadilla89.proxyuniverse.data.model.Country

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountrySelector(
    countries: List<Country>,
    onCountriesSelected: (List<Country>) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCountries by remember { 
        mutableStateOf(countries.filter { it.isSelected }.toMutableList()) 
    }
    
    val filteredCountries = remember(searchQuery, countries) {
        if (searchQuery.isEmpty()) {
            countries
        } else {
            countries.filter { 
                it.name.contains(searchQuery, ignoreCase = true) ||
                it.code.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            shape = MaterialTheme.shapes.large,
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Title
                Text(
                    text = "Select Countries",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // Search bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    placeholder = { Text("Search country...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true
                )
                
                // Select All / Deselect All
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = {
                            selectedCountries.clear()
                            selectedCountries.addAll(countries)
                        }
                    ) {
                        Text("Select All")
                    }
                    
                    TextButton(
                        onClick = {
                            selectedCountries.clear()
                        }
                    ) {
                        Text("Clear All")
                    }
                }
                
                // Selected count
                Text(
                    text = "${selectedCountries.size} countries selected",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                
                // Countries list
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(filteredCountries) { country ->
                        val isSelected = selectedCountries.any { it.code == country.code }
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = { checked ->
                                    if (checked) {
                                        selectedCountries.add(country)
                                    } else {
                                        selectedCountries.removeAll { it.code == country.code }
                                    }
                                }
                            )
                            
                            Text(
                                text = "${country.name} (${country.code})",
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .align(androidx.compose.ui.Alignment.CenterVertically)
                            )
                        }
                    }
                }
                
                // Action buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End,
                    horizontalSpacing = 8.dp
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    
                    Button(
                        onClick = {
                            val updatedCountries = countries.map { country ->
                                country.copy(
                                    isSelected = selectedCountries.any { it.code == country.code }
                                )
                            }
                            onCountriesSelected(updatedCountries)
                            onDismiss()
                        }
                    ) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}
