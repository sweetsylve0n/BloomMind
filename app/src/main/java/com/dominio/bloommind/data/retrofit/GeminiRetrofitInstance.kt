package com.dominio.bloommind.data.retrofit
import com.dominio.bloommind.data.Interfaces.GeminiApiInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GeminiRetrofitInstance {
    // Configurar Retrofit
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/v1beta/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    // Crear una instancia de la interfaz de la API
    val apiInterface: GeminiApiInterface by lazy {
        retrofit.create(GeminiApiInterface::class.java)
    }
}