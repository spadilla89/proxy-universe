package com.spadilla89.proxyuniverse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.spadilla89.proxyuniverse.ui.screens.*
import com.spadilla89.proxyuniverse.ui.theme.UniverseProxyTheme
import com.spadilla89.proxyuniverse.viewmodel.ProxyViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UniverseProxyTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val viewModel: ProxyViewModel = viewModel()
    
    val tabs = listOf("HTTP", "HTTPS", "SOCKS4", "SOCKS5")
    
    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Universe Proxy") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
                
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { 
                                selectedTabIndex = index
                                // Clear proxies when switching tabs
                                viewModel.clearProxies()
                            },
                            text = { Text(title) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            when (selectedTabIndex) {
                0 -> HttpScreen(viewModel = viewModel)
                1 -> HttpsScreen(viewModel = viewModel)
                2 -> Socks4Screen(viewModel = viewModel)
                3 -> Socks5Screen(viewModel = viewModel)
            }
        }
    }
}
