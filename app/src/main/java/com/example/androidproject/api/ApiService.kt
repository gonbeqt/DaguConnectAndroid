package com.example.androidproject.api

import okhttp3.Request
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("user/register")
    suspend fun register(@Body request: Register)
}