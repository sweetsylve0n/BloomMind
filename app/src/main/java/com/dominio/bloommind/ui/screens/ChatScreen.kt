package com.dominio.bloommind.ui.screens

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dominio.bloommind.R
import com.dominio.bloommind.data.datastore.ChatQuotaRepository
import com.dominio.bloommind.data.internet.GeminiService
import com.dominio.bloommind.ui.theme.ChatBubbleGray
import com.dominio.bloommind.viewmodel.ChatViewModel
import com.dominio.bloommind.viewmodel.ChatViewModelFactory
import com.dominio.bloommind.viewmodel.Message
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(emotions: String?) {
    val context = LocalContext.current
    val application = context.applicationContext as Application // Get application instance
    
    val chatViewModel: ChatViewModel = viewModel(
        // Pass the application to the factory
        factory = ChatViewModelFactory(ChatQuotaRepository(context), GeminiService(), application)
    )

    val uiState by chatViewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        chatViewModel.initializeWithEmotions(emotions)
    }

    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            coroutineScope.launch {
                lazyListState.animateScrollToItem(uiState.messages.lastIndex)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.blanca),
            contentDescription = "Chatbot Avatar",
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        QuotaBanner(
            quotaLeft = uiState.quotaLeft,
            quotaReached = uiState.quotaReached,
            warningThreshold = uiState.quotaWarningThreshold
        )

        LazyColumn(
            state = lazyListState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(uiState.messages) { message ->
                MessageBubble(message)
            }
            if (uiState.isSending) {
                item {
                    MessageBubble(Message("...", isFromUser = false))
                }
            }
        }

        MessageInput(
            onSendMessage = { text -> chatViewModel.sendMessage(text) },
            isEnabled = !uiState.quotaReached && !uiState.isSending
        )
    }
}

@Composable
fun QuotaBanner(quotaLeft: Int, quotaReached: Boolean, warningThreshold: Int) {
    val bannerText = when {
        quotaReached -> stringResource(id = R.string.chat_quota_reached)
        quotaLeft <= warningThreshold -> stringResource(id = R.string.chat_quota_remaining, quotaLeft)
        else -> stringResource(id = R.string.chat_welcome_prompt)
    }

    Text(
        text = bannerText,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
        color = if (quotaReached) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    )
}

@Composable
fun MessageBubble(message: Message) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (message.isError) MaterialTheme.colorScheme.errorContainer else ChatBubbleGray
            ),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = if (isSystemInDarkTheme()) Color.Black else Color.Unspecified
            )
        }
    }
}

@Composable
fun MessageInput(onSendMessage: (String) -> Unit, isEnabled: Boolean) {
    var userInput by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = userInput,
            onValueChange = { userInput = it },
            label = { Text(if (isEnabled) stringResource(id = R.string.chat_input_hint) else stringResource(id = R.string.chat_limit_reached)) },
            modifier = Modifier.weight(1f),
            enabled = isEnabled,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (userInput.isNotBlank() && isEnabled) {
                        onSendMessage(userInput)
                        userInput = ""
                        keyboardController?.hide()
                    }
                }
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {
                if (userInput.isNotBlank()) {
                    onSendMessage(userInput)
                    userInput = ""
                    keyboardController?.hide()
                }
            },
            enabled = userInput.isNotBlank() && isEnabled
        ) {
            Text(stringResource(id = R.string.send_button))
        }
    }
}
