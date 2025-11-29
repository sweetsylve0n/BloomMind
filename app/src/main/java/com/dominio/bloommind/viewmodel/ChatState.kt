package com.dominio.bloommind.viewmodel

import com.dominio.bloommind.data.datastore.ChatQuotaRepository


data class Message(
    val text: String,
    val isFromUser: Boolean,
    val isError: Boolean = false
)

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val isSending: Boolean = false,
    val quotaLeft: Int = ChatQuotaRepository.MAX_QUOTA,
    val quotaWarningThreshold: Int = 10,
    val quotaReached: Boolean = false
)
