package com.dominio.bloommind.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dominio.bloommind.R
import com.dominio.bloommind.data.repository.MessageRepository
import com.dominio.bloommind.ui.navigation.BloomMindNavItems
import com.dominio.bloommind.ui.navigation.Routes
import kotlinx.coroutines.launch

@Composable
fun SaveMomentScreen(navController: NavController) {
    val context = LocalContext.current
    val messageRepository = remember { MessageRepository(context) }
    val scope = rememberCoroutineScope()
    var message by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(bottom = 140.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.save_moment_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.save_moment_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = message,
                onValueChange = { 
                    if (it.lines().size <= 3) {
                        message = it 
                    }
                },
                label = { Text(stringResource(id = R.string.save_moment_hint)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    scope.launch {
                        messageRepository.saveFutureMessage(message)
                        navController.navigate(BloomMindNavItems.Home.route) {
                            popUpTo(Routes.CHECK_IN_GRAPH) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.6f),
                enabled = message.isNotBlank()
            ) {
                Text(text = stringResource(id = R.string.save_moment_button))
            }
        }

        Image(
            painter = painterResource(id = R.drawable.shino2),
            contentDescription = "Shino",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(300.dp)
                .padding(bottom = 16.dp)
        )
    }
}
