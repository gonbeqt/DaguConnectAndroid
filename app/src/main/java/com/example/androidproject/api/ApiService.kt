package com.example.androidproject.api

import com.example.androidproject.model.GetChats
import com.example.androidproject.model.Job
import com.example.androidproject.model.JobsResponse
import com.example.androidproject.model.LoginRequest
import com.example.androidproject.model.LoginResponse
import com.example.androidproject.model.PostJobResponse
import com.example.androidproject.model.RegisterRequest
import com.example.androidproject.model.RegisterResponse
import com.example.androidproject.model.PostJobs
import com.example.androidproject.model.client.BookTradesmanRequest
import com.example.androidproject.model.client.BookTradesmanResponse

import com.example.androidproject.model.client.GetClientsBooking
import com.example.androidproject.model.client.ReportRequest
import com.example.androidproject.model.client.ReportResponse
import com.example.androidproject.model.client.ResumesResponse
import com.example.androidproject.model.client.ViewClientBooking


import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
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

    @GET("/user/jobs/recent")
    suspend fun getRecentJobs(@Query("page") page: Int = 1, @Query("limit") limit: Int = 10): Response<JobsResponse>

    @GET("/client/jobs/view/{userId}")
    suspend fun getJobsByUserId(@Path("userId") userId: Int): Response<JobsResponse>

    @GET("/user/job/view/{id}")
    suspend fun getJobById(@Path("id") id: Int): Response<Job>

    @POST("/user/client/create-job")
    suspend fun postJob(@Body request: PostJobs): Response<PostJobResponse>

    @DELETE("/user/logout")
    suspend fun logout(): Response<Unit>

    @GET("/user/getresumes")
    suspend fun getResumes(@Query("page") page: Int = 1, @Query("limit") limit: Int = 10 ): Response<ResumesResponse>

    @GET("/user/getresume/{resumeId}")
    suspend fun getResumeById(@Path("resumeId") resumeId: Int): Response<viewResume>

    @GET("/user/client/getbooking")
    suspend fun getClientBooking(): Response<List<GetClientsBooking>>

    @GET("/user/client/viewbooking/{resumeId}")
    suspend fun getCleintBookingById(@Path("resumeId") resumeId: Int): Response<ViewClientBooking>

    @GET("/user/chat/get")
    suspend fun getChat(): Response <GetChats>

    @POST("/user/client/booktradesman/{tradesman_Id}")
    suspend fun booktradesman( @Body request: BookTradesmanRequest, @Path("tradesman_Id") tradesman_Id: Int): Response<BookTradesmanResponse>

    @POST("/user/client/reporttradesman/{tradesmanId}")
    suspend fun report( @Body request: ReportRequest, @Path("tradesmanId") tradesmanId: Int): Response<ReportResponse>
}