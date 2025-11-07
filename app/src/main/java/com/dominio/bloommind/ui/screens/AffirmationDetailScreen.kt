package com.dominio.bloommind.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dominio.bloommind.viewmodel.AffirmationUiState
import com.dominio.bloommind.viewmodel.AffirmationViewModel
import com.dominio.bloommind.viewmodel.AffirmationViewModelFactory

@Composable
fun AffirmationDetailScreen() {
    val context = LocalContext.current
    val vm: AffirmationViewModel = viewModel(factory = AffirmationViewModelFactory(context))
    val state by vm.affirmationState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val currentState = state) {
            is AffirmationUiState.Loading -> {
                Box(modifier = Modifier.offset(y = (-40).dp)) {
                    CircularProgressIndicator()
                }
            }
            is AffirmationUiState.Success -> {
                Box(modifier = Modifier.offset(y = (-40).dp)) {
                    AffirmationScreen(
                        affirmationText = currentState.affirmation.text,
                        imageIndex = currentState.affirmation.imageIndex
                    )
                }
            }
            is AffirmationUiState.Error -> {
                Box(modifier = Modifier.offset(y = (-40).dp)) {
                    Text(text = currentState.message)
                }
            }
        }
    }
}
