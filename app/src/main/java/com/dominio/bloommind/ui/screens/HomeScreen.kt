package com.dominio.bloommind.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.navigation.NavController
import com.dominio.bloommind.ui.components.EmergencyCarouselCard
import com.dominio.bloommind.ui.components.SimpleActionCard
import com.dominio.bloommind.ui.navigation.Routes
import com.dominio.bloommind.viewmodel.AdviceUiState
import com.dominio.bloommind.viewmodel.HomeViewModel
import com.dominio.bloommind.viewmodel.HomeViewModelFactory
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(context))

    val adviceState by homeViewModel.adviceState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            SimpleActionCard(
                title = androidx.compose.ui.res.stringResource(id = com.dominio.bloommind.R.string.card_checkin_title),
                onClick = { navController.navigate(Routes.CHECK_IN) }
            ) {
                Text(text = androidx.compose.ui.res.stringResource(id = com.dominio.bloommind.R.string.card_checkin_prompt))
            }
        }

        item {
            SimpleActionCard(
                title = androidx.compose.ui.res.stringResource(id = com.dominio.bloommind.R.string.card_affirmation_title),
                onClick = { navController.navigate(Routes.AFFIRMATION_DETAIL) }
            ) {
                Text(text = androidx.compose.ui.res.stringResource(id = com.dominio.bloommind.R.string.affirmation_card_prompt))
            }
        }

        item {
            SimpleActionCard(title = androidx.compose.ui.res.stringResource(id = com.dominio.bloommind.R.string.card_tip_title)) {
                when (val state = adviceState) {
                    is AdviceUiState.Loading -> {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is AdviceUiState.Success -> {
                        Text(text = state.advice.advice)
                    }
                    is AdviceUiState.Error -> {
                        Text(text = state.message)
                    }
                }
            }
        }

        item {
            EmergencyCarouselCard()
        }
    }
}
