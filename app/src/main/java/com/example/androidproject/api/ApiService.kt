package com.example.androidproject.api

import com.example.androidproject.model.CreateJob
import com.example.androidproject.model.CreateJobResponse
import com.example.androidproject.model.GetChats
import com.example.androidproject.model.GetJobs
import com.example.androidproject.model.Job
import com.example.androidproject.model.JobsResponse
import com.example.androidproject.model.LoginRequest
import com.example.androidproject.model.LoginResponse
import com.example.androidproject.model.RegisterRequest
import com.example.androidproject.model.RegisterResponse
import com.example.androidproject.model.client.BookTradesmanRequest
import com.example.androidproject.model.client.BookTradesmanResponse

import com.example.androidproject.model.client.GetClientsBooking
import com.example.androidproject.model.client.ResumesResponse
import com.example.androidproject.model.client.ViewClientBooking
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
    suspend fun getResumes(@Query("page") page: Int = 1, @Query("limit") limit: Int = 10 ): Response<ResumesResponse>

    @GET("/user/getresume/{resumeId}")
    suspend fun getResumeById(@Path("resumeId") resumeId: Int): Response<viewResume>

    @GET("/user/client/getbooking")
    suspend fun getClientBooking(@Header("Authorization") token: String): Response<List<GetClientsBooking>>

    @GET("/user/client/viewbooking/{resumeId}")
    suspend fun getCleintBookingById(@Path("resumeId") resumeId: Int): Response<ViewClientBooking>

    @GET("/user/chat/get")
    suspend fun getChat(): Response <GetChats>

    @POST("/user/client/create-job")
    suspend fun postJobs(@Body request: CreateJob): Response<CreateJobResponse>
    @POST("/user/client/booktradesman/{tradesman_Id}")
    suspend fun booktradesman(@Header("Authorization") token: String, @Body request: BookTradesmanRequest, @Path("tradesman_Id") tradesman_Id: Int): Response<BookTradesmanResponse>

}