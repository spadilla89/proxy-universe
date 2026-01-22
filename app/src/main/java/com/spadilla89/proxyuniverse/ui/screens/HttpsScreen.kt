package com.spadilla89.proxyuniverse.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.spadilla89.proxyuniverse.data.model.ProxyProtocol
import com.spadilla89.proxyuniverse.viewmodel.ProxyViewModel

@Composable
fun HttpsScreen(
    viewModel: ProxyViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    ProxyScreen(
        protocol = ProxyProtocol.HTTPS,
        viewModel = viewModel,
        modifier = modifier
    )
}
