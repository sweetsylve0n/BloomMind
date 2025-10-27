package com.dominio.bloommind.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dominio.bloommind.viewmodel.AffirmationUiState
import com.dominio.bloommind.viewmodel.AffirmationViewModel
import com.dominio.bloommind.viewmodel.AffirmationViewModelFactory

@Composable
fun AffirmationDetailScreen() {
    val context = LocalContext.current
    val vm: AffirmationViewModel = viewModel(factory = AffirmationViewModelFactory(context))
    val state by vm.affirmationState.collectAsState()

    when (state) {
        is AffirmationUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is AffirmationUiState.Success -> {
            val affirm = (state as AffirmationUiState.Success).affirmation
            // Reuse existing AffirmationScreen design
            AffirmationScreen(affirmationText = affirm.text, imageIndex = affirm.imageIndex)
        }
        is AffirmationUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = (state as AffirmationUiState.Error).message)
            }
        }
    }
}
