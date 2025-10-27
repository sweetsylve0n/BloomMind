package com.dominio.bloommind.data.retrofit

import com.dominio.bloommind.data.interfaces.NinjaApiInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NinjaRetrofitInstance {
    private const val BASE_URL = "https://api.api-ninjas.com/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: NinjaApiInterface by lazy {
        retrofit.create(NinjaApiInterface::class.java)
    }
}
