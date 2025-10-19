package com.dominio.bloommind.data
import com.dominio.bloommind.data.internet.GeminiService

class GeminiRepository {
    //instanciamos el servicio
    private val geminiService = GeminiService()
    suspend fun sendMessage(userMessage: String): Result<String>  {
        return geminiService.sendMessage(userMessage) //delegamos la llamada al servicio
    }
}