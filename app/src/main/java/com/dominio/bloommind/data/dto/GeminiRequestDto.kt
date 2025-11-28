package com.dominio.bloommind.data.dto
data class GeminiRequestDto(
    val contents: List<GeminiContent>,
    val generationConfig: GenerationConfig? = null,
    val systemInstruction: SystemInstruction? = null
)
data class GeminiContent(
    val role: String? = null,
    val parts: List<GeminiPart>
)
data class GeminiPart(
    val text: String
)
data class SystemInstruction(
    val parts: List<GeminiPart>
)
data class GenerationConfig(
    val temperature: Float = 0.8f,
    val maxOutputTokens: Int
)