package com.spadilla89.proxyuniverse.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spadilla89.proxyuniverse.data.model.Proxy
import com.spadilla89.proxyuniverse.ui.theme.StatusNotVerified
import com.spadilla89.proxyuniverse.ui.theme.StatusNotWorking
import com.spadilla89.proxyuniverse.ui.theme.StatusWorking

@Composable
fun ProxyListItem(
    proxy: Proxy,
    isSelected: Boolean,
    onSelectionChange: (Boolean) -> Unit,
    onCopyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox
            Checkbox(
                checked = isSelected,
                onCheckedChange = onSelectionChange,
                modifier = Modifier.padding(end = 8.dp)
            )
            
            // Proxy info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // IP:PORT
                Text(
                    text = proxy.toFormattedString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                // Metadata
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    // Country
                    Text(
                        text = proxy.country,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    // Anonymity
                    proxy.anonymity?.let {
                        Text(
                            text = it.displayName,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    // Speed
                    proxy.speed?.let {
                        Text(
                            text = "${it}ms",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            // Status indicator
            val (statusColor, statusText) = when (proxy.isWorking) {
                true -> StatusWorking to "✓"
                false -> StatusNotWorking to "✗"
                null -> StatusNotVerified to "?"
            }
            
            Surface(
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 8.dp),
                shape = MaterialTheme.shapes.small,
                color = statusColor
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = statusText,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
            
            // Copy button
            IconButton(
                onClick = onCopyClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy proxy",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
