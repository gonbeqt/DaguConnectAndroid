package com.example.androidproject.api

import com.example.androidproject.model.Job
import com.example.androidproject.model.JobsResponse
import com.example.androidproject.model.LoginRequest
import com.example.androidproject.model.LoginResponse
import com.example.androidproject.model.RegisterRequest
import com.example.androidproject.model.RegisterResponse

import com.example.androidproject.model.client.GetClientsBooking
import com.example.androidproject.model.client.resumesItem


import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import viewResume

interface ApiService {

    @POST("user/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("user/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("/user/jobs")
    suspend fun getJobs(@Query("page") page: Int = 1, @Query("limit") limit: Int = 10): Response<JobsResponse>

    @GET("/user/job/view/{id}")
    suspend fun getJobById(@Path("id") id: Int): Response<Job>

    @DELETE("/user/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<Unit>


    @GET("/user/getresumes")
    suspend fun getResumes(@Header("Authorization") token: String): Response<List<resumesItem>>

    @GET("/user/getresume/{resumeId}")
    suspend fun getResumeById(@Header("Authorization") token: String, @Path("resumeId") resumeId: Int): Response<viewResume>

    @GET("/user/client/getbooking")
    suspend fun getClientBooking(@Header("Authorization") token: String): Response<List<GetClientsBooking>>


}