package com.example.androidproject.api

import com.example.androidproject.model.LoginRequest
import com.example.androidproject.model.LoginResponse
import com.example.androidproject.model.RegisterRequest
import com.example.androidproject.model.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

interface ApiService {

    @POST("user/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("user/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}