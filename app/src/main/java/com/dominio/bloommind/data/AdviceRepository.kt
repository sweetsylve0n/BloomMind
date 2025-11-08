package com.dominio.bloommind.data

import android.content.Context
import com.dominio.bloommind.BuildConfig
import com.dominio.bloommind.data.dto.AdviceDto
import com.dominio.bloommind.data.retrofit.NinjaRetrofitInstance
import com.dominio.bloommind.R

class AdviceRepository(context: Context) {

    private val apiService = NinjaRetrofitInstance.api
    private val quotaRepository = RequestQuotaRepository(context)
    private val appContext = context

    suspend fun getDailyAdvice(): Result<AdviceDto> {
        if (!quotaRepository.canFetchAdvice()) {
            val cachedAdvice = quotaRepository.getCachedAdvice()
            return if (cachedAdvice != null) {
                Result.success(AdviceDto(cachedAdvice))
            } else {
                // This case happens if the user can't fetch but there's no cache. 
                // Should be rare, but we need to handle it.
                Result.failure(Exception(appContext.getString(R.string.error_not_allowed_fetch_advice)))
            }
        }

        // If we can fetch, get a new advice from the API.
        return try {
            val newAdvice = apiService.getAdvice(BuildConfig.NINJA_API_KEY)
            // Save the new advice to the cache for today.
            quotaRepository.saveAdvice(newAdvice.advice)
            // Return the new advice.
            Result.success(newAdvice)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
