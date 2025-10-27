package com.dominio.bloommind.data.retrofit

import com.dominio.bloommind.data.interfaces.BuddhaApiInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BuddhaRetrofitInstance {
    private const val BASE_URL = "https://buddha-api.com/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: BuddhaApiInterface by lazy {
        retrofit.create(BuddhaApiInterface::class.java)
    }
}
