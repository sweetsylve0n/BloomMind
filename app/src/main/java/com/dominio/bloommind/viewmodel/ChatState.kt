package com.dominio.bloommind.viewmodel

// Modelo para un solo mensaje en la lista.
// ¡ESTA ES LA CORRECCIÓN PRINCIPAL! 'data class' estaba mal puesto.
data class Message(
    val text: String,
    val isFromUser: Boolean,
    val isError: Boolean = false
)

// Estado completo de la UI del Chat. Esta parte estaba bien.
data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val isSending: Boolean = false,
    val quotaLeft: Int = 0,
    val quotaWarningThreshold: Int = 5,
    val quotaReached: Boolean = false,
    val error: String? = null
)