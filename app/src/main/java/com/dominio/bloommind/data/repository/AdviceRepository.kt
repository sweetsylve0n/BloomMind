package com.dominio.bloommind.data.repository

import android.content.Context
import com.dominio.bloommind.BuildConfig
import com.dominio.bloommind.data.dto.AdviceDto
import com.dominio.bloommind.data.retrofit.NinjaRetrofitInstance
import com.dominio.bloommind.R
import com.dominio.bloommind.data.LocalContentDataSource

class AdviceRepository(context: Context) {

    private val apiService = NinjaRetrofitInstance.api
    private val dataSource = LocalContentDataSource(context)
    private val appContext = context

    suspend fun getDailyAdvice(): Result<AdviceDto> {
        if (!dataSource.canFetchAdvice()) {
            val cachedAdvice = dataSource.getCachedAdvice()
            return if (cachedAdvice != null) {
                Result.success(AdviceDto(cachedAdvice))
            } else {
                Result.failure(Exception(appContext.getString(R.string.error_not_allowed_fetch_advice)))
            }
        }

        return try {
            val newAdvice = apiService.getAdvice(BuildConfig.NINJA_API_KEY)
            dataSource.saveAdvice(newAdvice.advice)
            Result.success(newAdvice)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
