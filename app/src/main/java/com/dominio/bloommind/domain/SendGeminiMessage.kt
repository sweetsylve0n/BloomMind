package com.dominio.bloommind.domain

import com.dominio.bloommind.data.GeminiRepository

//Caso de uso para enviar un mensaje a Gemini
class SendGeminiMessage {
    private val geminiRepository = GeminiRepository()
    suspend operator fun invoke(userMessage: String): Result<String>{
        return geminiRepository.sendMessage(userMessage) //la respuesta es delegada al repositorio
    }
}