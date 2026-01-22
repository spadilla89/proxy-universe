package com.spadilla89.proxyuniverse.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spadilla89.proxyuniverse.data.model.AnonymityLevel

@Composable
fun AnonymityFilter(
    selectedLevels: Set<AnonymityLevel>,
    onLevelsChanged: (Set<AnonymityLevel>) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "Anonymity Level",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // Elite
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = AnonymityLevel.ELITE in selectedLevels,
                onCheckedChange = { checked ->
                    val newLevels = selectedLevels.toMutableSet()
                    if (checked) {
                        newLevels.add(AnonymityLevel.ELITE)
                    } else {
                        newLevels.remove(AnonymityLevel.ELITE)
                    }
                    onLevelsChanged(newLevels)
                }
            )
            Text(
                text = "Elite (High Anonymity)",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        
        // Anonymous
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = AnonymityLevel.ANONYMOUS in selectedLevels,
                onCheckedChange = { checked ->
                    val newLevels = selectedLevels.toMutableSet()
                    if (checked) {
                        newLevels.add(AnonymityLevel.ANONYMOUS)
                    } else {
                        newLevels.remove(AnonymityLevel.ANONYMOUS)
                    }
                    onLevelsChanged(newLevels)
                }
            )
            Text(
                text = "Anonymous (Medium)",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        
        // Transparent
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = AnonymityLevel.TRANSPARENT in selectedLevels,
                onCheckedChange = { checked ->
                    val newLevels = selectedLevels.toMutableSet()
                    if (checked) {
                        newLevels.add(AnonymityLevel.TRANSPARENT)
                    } else {
                        newLevels.remove(AnonymityLevel.TRANSPARENT)
                    }
                    onLevelsChanged(newLevels)
                }
            )
            Text(
                text = "Transparent (No Anonymity)",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        
        // Clear all button
        if (selectedLevels.isNotEmpty()) {
            TextButton(
                onClick = { onLevelsChanged(emptySet()) },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Clear Selection")
            }
        }
    }
}
