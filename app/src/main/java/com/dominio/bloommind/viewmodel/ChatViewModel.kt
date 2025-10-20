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

// Usamos AndroidViewModel para poder acceder al Context y crear el repositorio
class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    private val sendGeminiMessageUseCase = SendGeminiMessage()
    private val quotaRepository = ChatQuotaRepository(application.applicationContext)

    init {
        // Al iniciar el ViewModel, cargamos el estado de la cuota
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
            // 1. Verificación de la cuota
            if (!quotaRepository.canSendNow()) {
                _uiState.update { it.copy(error = "Límite de mensajes alcanzado por hoy.") }
                return@launch
            }

            // 2. Añadir mensaje de usuario de forma optimista
            val userMessage = Message(text = userInput, isFromUser = true)
            _uiState.update {
                it.copy(
                    isSending = true,
                    messages = it.messages + userMessage,
                    error = null
                )
            }

            // 3. Llamar a la API de Gemini
            val result = sendGeminiMessageUseCase(userInput)

            result.onSuccess { responseText ->
                // 4. Añadir respuesta de Gemini y actualizar cuota
                val geminiMessage =
                    Message(text = responseText, isFromUser = false)
                quotaRepository.incrementAndSave()
                val newQuotaInfo = quotaRepository.getQuotaInfo() // Volvemos a leer para tener el valor actualizado
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
                // 5. Manejar el error
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