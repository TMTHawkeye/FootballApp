package com.example.footballapi

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FootballApiClient(
    private val baseUrl: String,
    private val apiKey: String
) {
    fun createService(): FootballApiService {
        val authInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-Secret-Key", apiKey)
                .build()
            chain.proceed(request)
        }

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()) // ✅ switched to Gson
//            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        return retrofit.create(FootballApiService::class.java)
    }
}
