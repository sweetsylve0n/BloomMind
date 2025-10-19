package com.dominio.bloommind.data.dto
import com.google.gson.annotations.SerializedName


data class GeminiResponseDto(
    @SerializedName("candidates") val candidates: List<GeminiCandidate> , // lista de respuestas (candidates)
    @SerializedName("promptFeedback") val promptFeedback: PromptFeedbackDto?

)
data class GeminiCandidate(
    @SerializedName("content") val content: GeminiContent // contenido de la respuesta
)

data class PromptFeedbackDto(
    @SerializedName("blockReason") val blockReason: String?
)