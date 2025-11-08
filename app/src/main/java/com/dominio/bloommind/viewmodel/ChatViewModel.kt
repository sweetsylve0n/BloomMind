package com.dominio.bloommind.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dominio.bloommind.data.datastore.ChatQuotaRepository
import com.dominio.bloommind.data.internet.GeminiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val quotaRepository: ChatQuotaRepository,
    private val geminiService: GeminiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadQuota()
    }

    /**
     * To be called when the screen is created. If emotions are passed, it starts
     * a contextual conversation with the AI without showing the prompt to the user.
     */
    fun initializeWithEmotions(emotions: String?) {
        if (!emotions.isNullOrBlank()) {
            val systemPrompt = "El usuario acaba de hacer un check-in y se siente: $emotions. Inicia una conversación amable y empática, validando sus sentimientos y terminando con una pregunta abierta sobre si quiere hablar de ello. No menciones que eres una IA. Sé breve y natural."
            // Send the prompt to the AI, but don't add it to the chat history and don't count it towards the quota.
            sendMessage(systemPrompt, isSystemMessage = true)
        }
    }

    private fun loadQuota() {
        viewModelScope.launch {
            val quotaInfo = quotaRepository.getQuotaInfo()
            val quotaLeft = ChatQuotaRepository.MAX_QUOTA - quotaInfo.count
            val hasReachedLimit = quotaInfo.count >= ChatQuotaRepository.MAX_QUOTA
            _uiState.update {
                it.copy(quotaLeft = quotaLeft, quotaReached = hasReachedLimit)
            }
        }
    }

    fun sendMessage(userInput: String, isSystemMessage: Boolean = false) {
        if (_uiState.value.isSending || (!isSystemMessage && _uiState.value.quotaReached)) return

        if (!isSystemMessage) {
            val userMessage = Message(text = userInput, isFromUser = true)
            _uiState.update {
                it.copy(messages = it.messages + userMessage, isSending = true)
            }
        } else {
            _uiState.update { it.copy(isSending = true) }
        }

        viewModelScope.launch {
            val result = geminiService.sendMessage(userInput)
            result.onSuccess {
                val botMessage = Message(text = it, isFromUser = false)
                _uiState.update { currentState ->
                    currentState.copy(messages = currentState.messages + botMessage, isSending = false)
                }
                if (!isSystemMessage) {
                    quotaRepository.incrementAndSave()
                    loadQuota()
                }
            }.onFailure {
                val errorMessage = Message(text = "Error: ${it.message}", isFromUser = false, isError = true)
                _uiState.update { currentState ->
                    currentState.copy(messages = currentState.messages + errorMessage, isSending = false)
                }
            }
        }
    }
}
