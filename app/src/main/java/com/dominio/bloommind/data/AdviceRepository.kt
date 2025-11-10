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
                Result.failure(Exception(appContext.getString(R.string.error_not_allowed_fetch_advice)))
            }
        }

        return try {
            val newAdvice = apiService.getAdvice(BuildConfig.NINJA_API_KEY)
            quotaRepository.saveAdvice(newAdvice.advice)
            Result.success(newAdvice)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
