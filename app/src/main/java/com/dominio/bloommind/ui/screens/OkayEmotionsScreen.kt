package com.dominio.bloommind.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dominio.bloommind.R
import com.dominio.bloommind.data.repository.EmotionRepository
import com.dominio.bloommind.data.repository.MessageRepository
import com.dominio.bloommind.ui.components.EmotionButton
import com.dominio.bloommind.ui.navigation.BloomMindNavItems
import com.dominio.bloommind.ui.navigation.Routes
import com.dominio.bloommind.viewmodel.CheckInViewModel
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun OkayEmotionsScreen(navController: NavController) {
    val viewModel: CheckInViewModel = viewModel()
    val selectedEmotions by viewModel.selectedEmotions.collectAsState()
    val context = LocalContext.current
    val emotionRepository = EmotionRepository(context)
    
    // Repositorio para desactivar la bandera
    val messageRepository = remember { MessageRepository(context) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.emotions_selection_prompt),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            viewModel.okayEmotions.chunked(2).forEach { rowEmotions ->
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

        Spacer(modifier = Modifier.height(32.dp))

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
                        // Desactivar bandera de mal día
                        messageRepository.setBadDayFlag(false)
                        
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
                        messageRepository.setBadDayFlag(false) // También aquí

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
        
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.tree),
            contentDescription = null,
            modifier = Modifier.size(400.dp)
        )
    }
}
