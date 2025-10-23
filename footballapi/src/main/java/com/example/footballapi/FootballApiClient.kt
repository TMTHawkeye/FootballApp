package com.example.footballapi

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

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

        val redirectInterceptor = Interceptor { chain ->
            val response = chain.proceed(chain.request())
            val body = response.body

            if (body != null) {
                val bodyString = body.string()

                // ✅ Check if redirect JSON is present
                if (bodyString.contains("__N_REDIRECT")) {
                    val redirectUrl = Regex("\"__N_REDIRECT\"\\s*:\\s*\"(.*?)\"")
                        .find(bodyString)
                        ?.groupValues?.get(1)

                    if (!redirectUrl.isNullOrBlank()) {
                        // Build absolute URL if needed
                        val fullUrl = if (redirectUrl.startsWith("http")) {
                            redirectUrl
                        } else {
                            "https://www.livescore.com$redirectUrl"
                        }

                        response.close() // close old one

                        // 🔁 Re-run request with redirected URL
                        val newRequest = chain.request().newBuilder()
                            .url(fullUrl)
                            .build()
                        return@Interceptor chain.proceed(newRequest)
                    }
                }

                // Rebuild original response since .string() consumes it
                return@Interceptor response.newBuilder()
                    .body(ResponseBody.create(body.contentType(), bodyString))
                    .build()
            }

            response
        }

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS) // wait up to 1 min to connect
            .readTimeout(180, TimeUnit.SECONDS)   // wait up to 3 mins for response
            .writeTimeout(180, TimeUnit.SECONDS)  // wait up to 3 mins for request body
            .retryOnConnectionFailure(true)
            .addInterceptor(authInterceptor)
            .addInterceptor(redirectInterceptor) // 🧠 add redirect handler
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(FootballApiService::class.java)
    }
}
