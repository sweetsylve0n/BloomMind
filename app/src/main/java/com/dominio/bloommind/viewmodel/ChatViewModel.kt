package com.dominio.bloommind.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dominio.bloommind.data.datastore.ChatQuotaRepository
import com.dominio.bloommind.domain.SendGeminiMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()
    private val sendGeminiMessageUseCase = SendGeminiMessage()
    private val quotaRepository = ChatQuotaRepository(application.applicationContext)

    init {
        loadQuota()
    }

    private fun loadQuota() {
        viewModelScope.launch {
            val quotaInfo = quotaRepository.getQuotaInfo()
            val quotaLeft = ChatQuotaRepository.MAX_QUOTA - quotaInfo.count
            _uiState.update {
                it.copy(
                    quotaLeft = quotaLeft,
                    quotaReached = quotaLeft <= 0
                )
            }
        }
    }

    fun sendMessage(userInput: String) {
        viewModelScope.launch {
            if (!quotaRepository.canSendNow()) {
                _uiState.update { it.copy(error = "Límite de mensajes alcanzado por hoy.") }
                return@launch
            }

            val userMessage = Message(text = userInput, isFromUser = true)
            _uiState.update {
                it.copy(
                    isSending = true,
                    messages = it.messages + userMessage,
                    error = null
                )
            }

            val result = sendGeminiMessageUseCase(userInput)
            result.onSuccess { responseText ->
                val geminiMessage =
                    Message(text = responseText, isFromUser = false)
                quotaRepository.incrementAndSave()
                val newQuotaInfo = quotaRepository.getQuotaInfo()
                val newQuotaLeft = ChatQuotaRepository.MAX_QUOTA - newQuotaInfo.count

                _uiState.update {
                    it.copy(
                        isSending = false,
                        messages = it.messages + geminiMessage,
                        quotaLeft = newQuotaLeft,
                        quotaReached = newQuotaLeft <= 0
                    )
                }
            }.onFailure { exception ->
                val errorMessage = Message(text = "Error al enviar: ${exception.message}", isFromUser = false, isError = true)
                _uiState.update {
                    it.copy(
                        isSending = false,
                        messages = it.messages + errorMessage
                    )
                }
            }
        }
    }
}