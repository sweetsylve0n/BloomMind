package com.dominio.bloommind.viewmodel

data class Message(
    val text: String,
    val isFromUser: Boolean,
    val isError: Boolean = false
)

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val isSending: Boolean = false,
    val quotaLeft: Int = 0,
    val quotaWarningThreshold: Int = 5,
    val quotaReached: Boolean = false,
    val error: String? = null
)