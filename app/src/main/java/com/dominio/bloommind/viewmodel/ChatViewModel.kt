package com.dominio.bloommind.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dominio.bloommind.R
import com.dominio.bloommind.data.datastore.ChatQuotaRepository
import com.dominio.bloommind.domain.SendGeminiMessage
import com.dominio.bloommind.data.dto.GeminiContent
import com.dominio.bloommind.data.dto.GeminiPart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val quotaRepository: ChatQuotaRepository,
    private val sendGeminiMessage: SendGeminiMessage,
    application: Application
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    // Mantener el historial completo para la API
    private val apiHistory = mutableListOf<GeminiContent>()

    init {
        loadQuota()
    }

    fun initializeWithEmotions(emotions: String?) {
        if (emotions.isNullOrBlank() || apiHistory.isNotEmpty()) return

        val contextPrompt = getApplication<Application>().getString(R.string.gemini_initial_prompt, emotions)
        
        val content = GeminiContent(role = "user", parts = listOf(GeminiPart(text = contextPrompt)))
        apiHistory.add(content)
        
        triggerGeminiResponse()
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

    fun sendMessage(userInput: String) {
        if (_uiState.value.isSending || _uiState.value.quotaReached) return

        val userMessage = Message(text = userInput, isFromUser = true)
        _uiState.update {
            it.copy(messages = it.messages + userMessage, isSending = true)
        }

        val content = GeminiContent(role = "user", parts = listOf(GeminiPart(text = userInput)))
        apiHistory.add(content)

        triggerGeminiResponse()
    }

    private fun triggerGeminiResponse() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSending = true) }
            
            val systemPrompt = getApplication<Application>().getString(R.string.gemini_system_instruction)
            
            // Usar el UseCase en lugar de llamar al servicio directo
            val result = sendGeminiMessage(apiHistory, systemPrompt)
            
            result.onSuccess { responseText ->
                val botContent = GeminiContent(role = "model", parts = listOf(GeminiPart(text = responseText)))
                apiHistory.add(botContent)
                
                val botMessage = Message(text = responseText, isFromUser = false)
                _uiState.update { currentState ->
                    currentState.copy(messages = currentState.messages + botMessage, isSending = false)
                }
                
                quotaRepository.incrementAndSave()
                loadQuota()
            }.onFailure { error ->
                val errorMessage = Message(text = "Error: ${error.message}", isFromUser = false, isError = true)
                _uiState.update { currentState ->
                    currentState.copy(messages = currentState.messages + errorMessage, isSending = false)
                }
            }
        }
    }
}
