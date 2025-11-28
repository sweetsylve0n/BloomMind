package com.dominio.bloommind.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dominio.bloommind.R
import com.dominio.bloommind.data.datastore.ChatQuotaRepository
import com.dominio.bloommind.data.ChatHistoryRepository
import com.dominio.bloommind.domain.SendGeminiMessage
import com.dominio.bloommind.data.dto.GeminiContent
import com.dominio.bloommind.data.dto.GeminiPart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val quotaRepository: ChatQuotaRepository,
    private val historyRepository: ChatHistoryRepository,
    private val sendGeminiMessage: SendGeminiMessage,
    application: Application
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    // Mantener el historial completo para la API
    private val apiHistory = mutableListOf<GeminiContent>()

    init {
        loadQuota()
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            val session = historyRepository.getSession()
            if (session.uiMessages.isNotEmpty()) {
                _uiState.update { it.copy(messages = session.uiMessages) }
                apiHistory.clear()
                apiHistory.addAll(session.apiHistory)
            }
        }
    }

    fun initializeWithEmotions(emotions: String?) {
        if (emotions.isNullOrBlank()) return

        viewModelScope.launch {
            // Solo inicializar si no hay historial previo O si el historial está vacío
            if (apiHistory.isEmpty()) {
                val contextPrompt = getApplication<Application>().getString(R.string.gemini_initial_prompt, emotions)
                
                val content = GeminiContent(role = "user", parts = listOf(GeminiPart(text = contextPrompt)))
                apiHistory.add(content)
                
                triggerGeminiResponse()
            }
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

    fun sendMessage(userInput: String) {
        if (_uiState.value.isSending || _uiState.value.quotaReached) return

        val userMessage = Message(text = userInput, isFromUser = true)
        _uiState.update {
            it.copy(messages = it.messages + userMessage, isSending = true)
        }

        val content = GeminiContent(role = "user", parts = listOf(GeminiPart(text = userInput)))
        apiHistory.add(content)
        
        saveCurrentState()

        triggerGeminiResponse()
    }

    private fun triggerGeminiResponse() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSending = true) }
            
            val systemPrompt = getApplication<Application>().getString(R.string.gemini_system_instruction)
            
            val result = sendGeminiMessage(apiHistory, systemPrompt)
            
            result.onSuccess { responseText ->
                val botContent = GeminiContent(role = "model", parts = listOf(GeminiPart(text = responseText)))
                apiHistory.add(botContent)
                
                val botMessage = Message(text = responseText, isFromUser = false)
                _uiState.update { currentState ->
                    currentState.copy(messages = currentState.messages + botMessage, isSending = false)
                }
                
                quotaRepository.incrementAndSave()
                saveCurrentState() // Guardar estado tras respuesta
                loadQuota()
            }.onFailure { error ->
                val errorMessage = Message(text = "Error: ${error.message}", isFromUser = false, isError = true)
                _uiState.update { currentState ->
                    currentState.copy(messages = currentState.messages + errorMessage, isSending = false)
                }
            }
        }
    }

    private fun saveCurrentState() {
        viewModelScope.launch {
            historyRepository.saveSession(_uiState.value.messages, apiHistory)
        }
    }
    
    // Método opcional si quieres añadir un botón para borrar historial
    fun clearChatHistory() {
        viewModelScope.launch {
            historyRepository.clearHistory()
            _uiState.update { it.copy(messages = emptyList()) }
            apiHistory.clear()
        }
    }
}
