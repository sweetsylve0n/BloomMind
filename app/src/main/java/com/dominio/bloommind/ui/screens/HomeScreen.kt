package com.dominio.bloommind.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dominio.bloommind.R
import com.dominio.bloommind.data.datastore.UserProfile
import com.dominio.bloommind.ui.components.EmergencyCarouselCard
import com.dominio.bloommind.ui.components.SimpleActionCard
import com.dominio.bloommind.ui.navigation.Routes
import com.dominio.bloommind.viewmodel.AdviceUiState
import com.dominio.bloommind.viewmodel.HomeViewModel
import com.dominio.bloommind.viewmodel.HomeViewModelFactory
import java.util.Calendar

@Composable
fun HomeScreen(navController: NavController, userProfile: UserProfile) {
    val context = LocalContext.current
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(context))
    val adviceState by homeViewModel.adviceState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))
            GreetingHeader(userName = userProfile.name)

            val readyTextRes = if (userProfile.gender == stringResource(id = R.string.gender_option_she)) {
                R.string.ready_for_the_day_feminine
            } else {
                R.string.ready_for_the_day_masculine
            }
            Text(
                text = stringResource(id = readyTextRes),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    SimpleActionCard(
                        title = stringResource(id = R.string.card_checkin_title),
                        onClick = { navController.navigate(Routes.CHECK_IN_GRAPH) }
                    ) {
                        Text(text = stringResource(id = R.string.card_checkin_prompt))
                    }
                }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    CharacterImage(drawableRes = R.drawable.isabelle, description = "Isabelle", size = 150.dp)
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    CharacterImage(drawableRes = R.drawable.kabuki, description = "Kabuki", size = 140.dp)
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    SimpleActionCard(
                        title = stringResource(id = R.string.card_affirmation_title),
                        onClick = { navController.navigate(Routes.AFFIRMATION_DETAIL) }
                    ) {
                        Text(text = stringResource(id = R.string.affirmation_card_prompt))
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    SimpleActionCard(title = stringResource(id = R.string.card_tip_title)) {
                        when (val state = adviceState) {
                            is AdviceUiState.Loading -> {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
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
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    CharacterImage(drawableRes = R.drawable.shino, description = "Shino", size = 150.dp)
                }
            }
        }

        item {
            EmergencyCarouselCard()
        }
    }
}

@Composable
private fun GreetingHeader(userName: String) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)

    val greetingRes = when (hour) {
        in 0..11 -> R.string.greeting_morning
        in 12..17 -> R.string.greeting_afternoon
        else -> R.string.greeting_evening
    }

    Text(
        text = stringResource(id = greetingRes, userName),
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun CharacterImage(
    drawableRes: Int,
    description: String,
    size: Dp
) {
    Image(
        painter = painterResource(id = drawableRes),
        contentDescription = description,
        modifier = Modifier.size(size),
        contentScale = ContentScale.Fit
    )
}
