package com.spadilla89.proxyuniverse.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ExportDialog(
    totalProxies: Int,
    selectedProxies: Int,
    onExportAll: () -> Unit,
    onExportSelected: () -> Unit,
    onCopyAmount: (Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var customAmount by remember { mutableStateOf("") }
    var showCustomInput by remember { mutableStateOf(false) }
    
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Export/Copy Options",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                if (!showCustomInput) {
                    // Quick options
                    if (selectedProxies > 0) {
                        Button(
                            onClick = {
                                onCopyAmount(selectedProxies)
                                onDismiss()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            Text("Copy Selected ($selectedProxies)")
                        }
                    }
                    
                    Button(
                        onClick = {
                            onCopyAmount(10.coerceAtMost(totalProxies))
                            onDismiss()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Text("Copy 10 Proxies")
                    }
                    
                    Button(
                        onClick = {
                            onCopyAmount(50.coerceAtMost(totalProxies))
                            onDismiss()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Text("Copy 50 Proxies")
                    }
                    
                    Button(
                        onClick = {
                            onCopyAmount(100.coerceAtMost(totalProxies))
                            onDismiss()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Text("Copy 100 Proxies")
                    }
                    
                    Button(
                        onClick = {
                            onCopyAmount(totalProxies)
                            onDismiss()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Text("Copy All ($totalProxies)")
                    }
                    
                    OutlinedButton(
                        onClick = { showCustomInput = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Text("Custom Amount")
                    }
                    
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    // Export to file options
                    if (selectedProxies > 0) {
                        FilledTonalButton(
                            onClick = {
                                onExportSelected()
                                onDismiss()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            Text("Export Selected to File")
                        }
                    }
                    
                    FilledTonalButton(
                        onClick = {
                            onExportAll()
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Export All to File")
                    }
                } else {
                    // Custom amount input
                    OutlinedTextField(
                        value = customAmount,
                        onValueChange = { customAmount = it.filter { char -> char.isDigit() } },
                        label = { Text("Number of proxies") },
                        placeholder = { Text("Enter amount") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        singleLine = true
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showCustomInput = false },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Back")
                        }
                        
                        Button(
                            onClick = {
                                val amount = customAmount.toIntOrNull()
                                if (amount != null && amount > 0) {
                                    onCopyAmount(amount.coerceAtMost(totalProxies))
                                    onDismiss()
                                }
                            },
                            modifier = Modifier.weight(1f),
                            enabled = customAmount.toIntOrNull()?.let { it > 0 } == true
                        ) {
                            Text("Copy")
                        }
                    }
                }
                
                // Cancel button
                if (!showCustomInput) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}
