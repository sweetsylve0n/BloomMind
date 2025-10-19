package com.dominio.bloommind.data.dto

data class GeminiRequestDto(
    val contents: List<GeminiContent>,
    val generationConfig: GenerationConfig? = null
)
data class GeminiContent(
    val parts: List<GeminiPart>
)
data class GeminiPart(
    val text: String
)
data class GenerationConfig( // Configuración de texto
    val temperature: Float = 0.8f, // alearoriedad de la respuesta
    val maxOutputTokens: Int //limite de tokens
)