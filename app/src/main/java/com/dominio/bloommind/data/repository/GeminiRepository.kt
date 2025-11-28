package com.dominio.bloommind.data.repository

import com.dominio.bloommind.data.dto.GeminiContent
import com.dominio.bloommind.data.internet.GeminiService

class GeminiRepository {
    private val geminiService = GeminiService()
    
    suspend fun sendMessage(history: List<GeminiContent>, systemPrompt: String?): Result<String> {
        return geminiService.sendMessage(history, systemPrompt)
    }
}
