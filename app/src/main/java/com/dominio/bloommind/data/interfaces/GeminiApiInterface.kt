package com.dominio.bloommind.data.interfaces

import com.dominio.bloommind.data.dto.GeminiRequestDto
import com.dominio.bloommind.data.dto.GeminiResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApiInterface {
    @POST("models/gemini-2.0-flash:generateContent")
    fun sendMessage(
        @Query("key") apiKey: String,
        @Body request: GeminiRequestDto
    ): Call<GeminiResponseDto>
}
