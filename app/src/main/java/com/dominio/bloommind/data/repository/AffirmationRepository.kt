package com.dominio.bloommind.data.repository

import android.content.Context
import com.dominio.bloommind.R
import com.dominio.bloommind.data.LocalContentDataSource
import com.dominio.bloommind.data.dto.AffirmationDto
import com.dominio.bloommind.data.retrofit.BuddhaRetrofitInstance

class AffirmationRepository(context: Context) {

    private val apiService = BuddhaRetrofitInstance.api
    private val dataSource = LocalContentDataSource(context)
    private val appContext = context

    suspend fun getDailyAffirmation(): Result<AffirmationDto> {
        if (!dataSource.canFetchAffirmation()) {
            val cachedText = dataSource.getCachedAffirmation()
            val imageIndex = dataSource.getAffirmationImageIndex()
            return if (cachedText != null) {
                Result.success(AffirmationDto(cachedText, imageIndex))
            } else {
                Result.failure(Exception(appContext.getString(R.string.error_not_allowed_fetch_affirmation)))
            }
        }

        return try {
            val newAffirmationText = apiService.getAffirmation().text

            dataSource.saveAffirmation(newAffirmationText)

            val newImageIndex = dataSource.getAffirmationImageIndex()

            val resultDto = AffirmationDto(newAffirmationText, newImageIndex)
            
            Result.success(resultDto)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
