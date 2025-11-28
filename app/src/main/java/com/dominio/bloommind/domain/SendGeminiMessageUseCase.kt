package com.dominio.bloommind.domain

import com.dominio.bloommind.data.repository.GeminiRepository
import com.dominio.bloommind.data.dto.GeminiContent

class SendGeminiMessageUseCase(private val geminiRepository: GeminiRepository) {
    suspend operator fun invoke(history: List<GeminiContent>, systemPrompt: String?): Result<String> {
        return geminiRepository.sendMessage(history, systemPrompt)
    }
}
