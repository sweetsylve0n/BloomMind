package com.dominio.bloommind.domain

import com.dominio.bloommind.data.GeminiRepository
class SendGeminiMessage {
    private val geminiRepository = GeminiRepository()
    suspend operator fun invoke(userMessage: String): Result<String>{
        return geminiRepository.sendMessage(userMessage)
    }
}