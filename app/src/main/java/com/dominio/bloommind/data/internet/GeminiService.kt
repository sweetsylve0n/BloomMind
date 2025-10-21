package com.dominio.bloommind.data.internet

import com.dominio.bloommind.data.dto.GeminiRequestDto
import com.dominio.bloommind.data.dto.GeminiContent
import com.dominio.bloommind.data.dto.GeminiPart
import com.dominio.bloommind.data.dto.GenerationConfig
import com.dominio.bloommind.data.retrofit.GeminiRetrofitInstance
import com.dominio.bloommind.BuildConfig
import retrofit2.awaitResponse
class GeminiService {
    suspend fun sendMessage(userMessage: String): Result<String> {
        try {
            val request = GeminiRequestDto(
                contents = listOf(
                    GeminiContent(parts = listOf(GeminiPart(text = "Responde a lo siguiente de forma amable, con empatia por el usuario, en un solo párrafo y haciendo una pregunta al final para continuar la conversacion: $userMessage")))
                ),
                generationConfig = GenerationConfig(temperature = 0.8f, maxOutputTokens = 150)
            )
            val call = GeminiRetrofitInstance.apiInterface.sendMessage(
                BuildConfig.GEMINI_API_KEY, request
            )
            val response = call.awaitResponse()

            if (response.isSuccessful) {
                val body = response.body()
                val responseText = body?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text

                return if (!responseText.isNullOrEmpty()) {
                    Result.success(responseText)
                } else {
                    val reason = body?.promptFeedback?.blockReason
                    if (reason != null) {
                        Result.failure(Exception("La solicitud fue bloqueada por seguridad. Razón: $reason"))
                    } else {
                        Result.failure(Exception("La respuesta de la API estaba vacía o en un formato inesperado."))
                    }
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Cuerpo del error ilegible"
                return Result.failure(Exception("Error de red ${response.code()}: $errorBody"))
            }
        } catch (e: Exception) {
            return Result.failure(Exception("No se pudo conectar al servicio: ${e.message}", e))
        }
    }
}