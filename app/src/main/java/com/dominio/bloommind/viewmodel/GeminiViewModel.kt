package com.dominio.bloommind.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dominio.bloommind.domain.SendGeminiMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


//funcionalidad para enviar mensajes a Gemini y manejar la respuesta
class GeminiViewModel : ViewModel() {
    // estado para almacenar la respuesta de Gemini
    private val _geminiResponse = MutableStateFlow<String?>(null)
    val geminiResponse: StateFlow<String?> = _geminiResponse
    private val sendGeminiMessage = SendGeminiMessage()

    fun sendMessage(userInput: String) {
        if (userInput.isBlank()) {
            _geminiResponse.value = "Por favor, escribe un mensaje."
            return
        }

        viewModelScope.launch {
            _geminiResponse.value = "Generando respuesta..."
            val result = sendGeminiMessage(userInput) // Llama al caso de uso para enviar el mensaje

            result.onSuccess { responseText ->
                _geminiResponse.value = responseText
            }.onFailure { exception ->
                _geminiResponse.value = "Error: ${exception.message}"
            }
        }
    }
}