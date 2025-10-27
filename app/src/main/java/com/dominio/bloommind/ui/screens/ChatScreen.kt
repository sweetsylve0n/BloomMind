package com.dominio.bloommind.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dominio.bloommind.viewmodel.ChatViewModel
import com.dominio.bloommind.viewmodel.Message
import kotlinx.coroutines.launch
@Composable
fun ChatScreen() {
    val chatViewModel: ChatViewModel = viewModel()
    val uiState by chatViewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

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
            .padding(16.dp)
    ) {
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
        quotaReached -> androidx.compose.ui.res.stringResource(id = com.dominio.bloommind.R.string.chat_quota_reached)
        quotaLeft <= warningThreshold -> androidx.compose.ui.res.stringResource(id = com.dominio.bloommind.R.string.chat_quota_remaining, quotaLeft)
        else -> null
    }

    if (bannerText != null) {
        Text(
            text = bannerText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            color = if (quotaReached) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
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
                containerColor = when {
                    message.isError -> MaterialTheme.colorScheme.errorContainer
                    message.isFromUser -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.secondaryContainer
                }
            ),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyLarge
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
            label = { Text(if (isEnabled) androidx.compose.ui.res.stringResource(id = com.dominio.bloommind.R.string.chat_input_hint) else androidx.compose.ui.res.stringResource(id = com.dominio.bloommind.R.string.chat_limit_reached)) },
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
            Text(androidx.compose.ui.res.stringResource(id = com.dominio.bloommind.R.string.send_button))
        }
    }
}