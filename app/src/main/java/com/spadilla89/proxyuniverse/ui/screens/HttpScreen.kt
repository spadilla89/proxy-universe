package com.spadilla89.proxyuniverse.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.spadilla89.proxyuniverse.data.model.ProxyProtocol
import com.spadilla89.proxyuniverse.viewmodel.ProxyViewModel

@Composable
fun HttpScreen(
    viewModel: ProxyViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    ProxyScreen(
        protocol = ProxyProtocol.HTTP,
        viewModel = viewModel,
        modifier = modifier
    )
}
