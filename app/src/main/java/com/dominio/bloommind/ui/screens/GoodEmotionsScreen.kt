package com.dominio.bloommind.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dominio.bloommind.R
import com.dominio.bloommind.data.EmotionRepository
import com.dominio.bloommind.ui.components.EmotionButton
import com.dominio.bloommind.ui.navigation.BloomMindNavItems
import com.dominio.bloommind.ui.navigation.Routes
import com.dominio.bloommind.viewmodel.CheckInViewModel
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun GoodEmotionsScreen(navController: NavController) {
    val viewModel: CheckInViewModel = viewModel()
    val selectedEmotions by viewModel.selectedEmotions.collectAsState()
    val context = LocalContext.current
    val emotionRepository = EmotionRepository(context)
    val scope = rememberCoroutineScope()

    // A single, scrollable column that centers its content.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Make the whole block scrollable
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // Center the whole content block vertically
    ) {
        Text(
            text = stringResource(id = R.string.emotions_selection_prompt),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Two-column button grid
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            viewModel.goodEmotions.chunked(2).forEach { rowEmotions ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    rowEmotions.forEach { emotion ->
                        EmotionButton(
                            text = stringResource(id = emotion.nameResId),
                            isSelected = emotion in selectedEmotions,
                            onClick = { viewModel.toggleEmotionSelection(emotion) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp)) // Spacer between emotions and actions

        // Action buttons, now part of the centered & scrollable block
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    scope.launch {
                        viewModel.saveCheckIn(emotionRepository)
                        navController.navigate(BloomMindNavItems.Home.route) {
                            popUpTo(Routes.CHECK_IN_GRAPH) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                },
                enabled = selectedEmotions.isNotEmpty(),
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(id = R.string.checkin_save_and_home))
            }
            Button(
                onClick = {
                    scope.launch {
                        viewModel.saveCheckIn(emotionRepository)
                        val emotionNames = selectedEmotions.joinToString(", ") { context.getString(it.nameResId) }
                        val encodedEmotions = URLEncoder.encode(emotionNames, StandardCharsets.UTF_8.name())
                        navController.navigate("${BloomMindNavItems.Chat.route}?emotions=$encodedEmotions") {
                            popUpTo(Routes.CHECK_IN_GRAPH) { inclusive = true }
                        }
                    }
                },
                enabled = selectedEmotions.isNotEmpty(),
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(id = R.string.checkin_talk_about_it))
            }
        }
    }
}
