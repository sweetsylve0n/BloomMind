package com.dominio.bloommind.data.interfaces

import com.dominio.bloommind.data.dto.AffirmationDto
import retrofit2.http.GET

interface BuddhaApiInterface {
    @GET("api/today")
    suspend fun getAffirmation(): AffirmationDto
}
