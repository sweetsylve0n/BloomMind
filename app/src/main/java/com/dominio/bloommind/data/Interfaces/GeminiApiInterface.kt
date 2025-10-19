package com.dominio.bloommind.data.Interfaces

import com.dominio.bloommind.data.dto.GeminiRequestDto
import com.dominio.bloommind.data.dto.GeminiResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
// Interfaz de la API de Gemini
interface GeminiApiInterface {
    @POST("models/gemini-2.0-flash:generateContent") //endpoint de la API
    fun sendMessage(
        @Query("key") apiKey: String,
        @Body request: GeminiRequestDto
    ): Call<GeminiResponseDto> //esto es una llamada de retrofit que devuelve un objeto GeminiResponseDto
}