package com.dominio.bloommind.data.interfaces

import com.dominio.bloommind.data.dto.AdviceDto
import retrofit2.http.GET
import retrofit2.http.Header
interface NinjaApiInterface {
    @GET("v1/advice")
    suspend fun getAdvice(@Header("X-Api-Key") apiKey: String): AdviceDto
}
