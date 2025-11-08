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
        // If we CANNOT fetch a new one, get the cached version.
        if (!quotaRepository.canFetchAffirmation()) {
            val cachedText = quotaRepository.getCachedAffirmation()
            val imageIndex = quotaRepository.getAffirmationImageIndex()
            return if (cachedText != null) {
                Result.success(AffirmationDto(cachedText, imageIndex))
            } else {
                // This is a rare case where the user can't fetch, but there's no cache.
                Result.failure(Exception(appContext.getString(R.string.error_not_allowed_fetch_affirmation)))
            }
        }

        // If we CAN fetch a new one, do the full flow.
        return try {
            // 1. Fetch new affirmation text from the API.
            val newAffirmationText = apiService.getAffirmation().text

            // 2. Save the new text. This process also generates and saves the new random image index for the day.
            quotaRepository.saveAffirmation(newAffirmationText)

            // 3. Get the NEW image index that was just saved.
            val newImageIndex = quotaRepository.getAffirmationImageIndex()

            // 4. Return the FRESH data directly, without reading from cache again.
            val resultDto = AffirmationDto(newAffirmationText, newImageIndex)
            
            Result.success(resultDto)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
