package com.dominio.bloommind.data

import com.dominio.bloommind.data.internet.GeminiService
class GeminiRepository {
    private val geminiService = GeminiService()
    suspend fun sendMessage(userMessage: String): Result<String>  {
        return geminiService.sendMessage(userMessage)
    }
}