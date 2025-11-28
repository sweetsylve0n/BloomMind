package com.dominio.bloommind.data.internet

import com.dominio.bloommind.data.dto.GeminiRequestDto
import com.dominio.bloommind.data.dto.GeminiContent
import com.dominio.bloommind.data.dto.GeminiPart
import com.dominio.bloommind.data.dto.GenerationConfig
import com.dominio.bloommind.data.dto.SystemInstruction
import com.dominio.bloommind.data.retrofit.GeminiRetrofitInstance
import com.dominio.bloommind.BuildConfig
import retrofit2.awaitResponse

class GeminiService {
    suspend fun sendMessage(history: List<GeminiContent>, systemPrompt: String? = null): Result<String> {
        try {
            val systemInstruction = systemPrompt?.let {
                SystemInstruction(parts = listOf(GeminiPart(text = it)))
            }

            val request = GeminiRequestDto(
                contents = history,
                generationConfig = GenerationConfig(temperature = 0.8f, maxOutputTokens = 150),
                systemInstruction = systemInstruction
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
                        Result.failure(Exception("The request was blocked for safety reasons. Reason: $reason"))
                    } else {
                        Result.failure(Exception("The API response was empty or in an unexpected format."))
                    }
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unreadable error body"
                return Result.failure(Exception("Network error ${response.code()}: $errorBody"))
            }
        } catch (e: Exception) {
            return Result.failure(Exception("Could not connect to the service: ${e.message}", e))
        }
    }
}
