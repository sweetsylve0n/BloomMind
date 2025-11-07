package com.dominio.bloommind.data

import android.content.Context
import com.dominio.bloommind.R
import com.dominio.bloommind.data.dto.AffirmationDto
import com.dominio.bloommind.data.retrofit.BuddhaRetrofitInstance

class AffirmationRepository(context: Context) {

    private val apiService = BuddhaRetrofitInstance.api
    private val quotaRepository = RequestQuotaRepository(context)
    private val appContext = context

    suspend fun getDailyAffirmation(): Result<AffirmationDto> {
        if (!quotaRepository.canFetchAffirmation()) {
            val cachedText = quotaRepository.getCachedAffirmation()
            val imageIndex = quotaRepository.getAffirmationImageIndex()
            return if (cachedText != null) {
                Result.success(AffirmationDto(cachedText, imageIndex))
            } else {
                Result.failure(Exception(appContext.getString(R.string.error_not_allowed_fetch_affirmation)))
            }
        }

        return try {
            val affirmationDto = apiService.getAffirmation()
            affirmationDto.imageIndex = quotaRepository.getAffirmationImageIndex()
            quotaRepository.saveAffirmation(affirmationDto.text)
            
            Result.success(affirmationDto)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
