package com.example.androidproject.api

import com.example.androidproject.data.preferences.TokenManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private const val BASE_URL = "http://192.168.248.159:8000/"

    // Add the logging interceptor
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(100,TimeUnit.SECONDS)
        .readTimeout(100,TimeUnit.SECONDS)
        .writeTimeout(100,TimeUnit.SECONDS)
        .addInterceptor(logging)
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = chain.request().newBuilder()
            if (originalRequest.header("Authorization") == null) {
                TokenManager.getToken()?.let { token ->
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }
            }
            chain.proceed(requestBuilder.build())
        }.build()

    private val gson = GsonBuilder()
        .setLenient() // Allows lenient parsing of JSON
        .create()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }
}