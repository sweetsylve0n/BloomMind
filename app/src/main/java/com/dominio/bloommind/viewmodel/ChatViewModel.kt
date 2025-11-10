package com.dominio.bloommind.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dominio.bloommind.R
import com.dominio.bloommind.data.datastore.ChatQuotaRepository
import com.dominio.bloommind.data.internet.GeminiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ChatViewModel(
    private val quotaRepository: ChatQuotaRepository,
    private val geminiService: GeminiService,
    application: Application
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadQuota()
    }

    fun initializeWithEmotions(emotions: String?) {
        if (!emotions.isNullOrBlank()) {
            val systemPrompt = getApplication<Application>().getString(R.string.gemini_initial_prompt, emotions)
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

        val prompt: String
        if (isSystemMessage) {
            prompt = userInput
            _uiState.update { it.copy(isSending = true) }
        } else {
            val userMessage = Message(text = userInput, isFromUser = true)
            _uiState.update {
                it.copy(messages = it.messages + userMessage, isSending = true)
            }
            prompt = getApplication<Application>().getString(R.string.gemini_base_prompt, userInput)
        }

        viewModelScope.launch {
            val result = geminiService.sendMessage(prompt)
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
