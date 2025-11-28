package com.dominio.bloommind.domain

import com.dominio.bloommind.data.GeminiRepository
import com.dominio.bloommind.data.dto.GeminiContent

class SendGeminiMessage(private val geminiRepository: GeminiRepository) {
    suspend operator fun invoke(history: List<GeminiContent>, systemPrompt: String?): Result<String> {
        return geminiRepository.sendMessage(history, systemPrompt)
    }
}
