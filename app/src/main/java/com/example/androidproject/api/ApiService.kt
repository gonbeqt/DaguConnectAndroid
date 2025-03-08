package com.example.androidproject.api

import com.example.androidproject.model.ChangePasswordRequest
import com.example.androidproject.model.ChangePasswordResponse
import com.example.androidproject.model.ClientProfile
import com.example.androidproject.model.ForgotPasswordRequest
import com.example.androidproject.model.ForgotPasswordResponse
import com.example.androidproject.model.GetChats
import com.example.androidproject.model.GetJobApplicantsData
import com.example.androidproject.model.GetJobApplicationData
import com.example.androidproject.model.GetMessages
import com.example.androidproject.model.GetMyJobs
import com.example.androidproject.model.GetNotification
import com.example.androidproject.model.Job
import com.example.androidproject.model.JobApplicantData
import com.example.androidproject.model.JobsResponse
import com.example.androidproject.model.LoginRequest
import com.example.androidproject.model.LoginResponse
import com.example.androidproject.model.PostJobApplication
import com.example.androidproject.model.PostJobApplicationResponse
import com.example.androidproject.model.PostJobResponse
import com.example.androidproject.model.RegisterRequest
import com.example.androidproject.model.RegisterResponse
import com.example.androidproject.model.PostJobs
import com.example.androidproject.model.UpdateAddress
import com.example.androidproject.model.UpdateAddressResponse
import com.example.androidproject.model.UpdateJob
import com.example.androidproject.model.UpdateJobResponse
import com.example.androidproject.model.UpdateProfilePictureResponse
import com.example.androidproject.model.ResetPasswordRequest
import com.example.androidproject.model.ResetPasswordResponse
import com.example.androidproject.model.UpdateStatus
import com.example.androidproject.model.UpdateStatusResponse
import com.example.androidproject.model.ViewJobApplicationResponse
import com.example.androidproject.model.client.BookTradesmanRequest
import com.example.androidproject.model.client.BookTradesmanResponse
import com.example.androidproject.model.client.ClientWorkStatusRequest
import com.example.androidproject.model.client.ClientWorkStatusResponse
import com.example.androidproject.model.client.GetClientsBookingResponse
import com.example.androidproject.model.client.GetTradesmanBookingResponse
import com.example.androidproject.model.client.ReportRequest
import com.example.androidproject.model.client.ReportResponse
import com.example.androidproject.model.client.ResumesResponse
import com.example.androidproject.model.client.SubmitResumeResponse
import com.example.androidproject.model.client.TradesmanWorkStatusRequest
import com.example.androidproject.model.client.TradesmanWorkStatusResponse
import com.example.androidproject.model.client.UpdateTradesmanDetailsRequest
import com.example.androidproject.model.client.UpdateTradesmanDetailsResponse
import com.example.androidproject.model.client.UpdateTradesmanProfileResponse
import com.example.androidproject.model.client.ViewClientBooking
import com.example.androidproject.model.client.rateTradesmanRequest
import com.example.androidproject.model.client.rateTradesmanResponse
import com.example.androidproject.model.client.ratingsItem
import com.example.androidproject.model.client.viewResume
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("user/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("user/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("/user/jobs")
    suspend fun getJobs(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): Response<JobsResponse>

    @GET("/user/jobs/recent")
    suspend fun getRecentJobs(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): Response<JobsResponse>

    @GET("/client/jobs/view/my_jobs")
    suspend fun getJobsByUserId(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): Response<GetMyJobs>

    @GET("client/profile")
    suspend fun getClientProfile(): Response<ClientProfile>

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
    suspend fun getClientBooking(@Query("page") page: Int = 1, @Query("limit") limit: Int = 10): Response<GetClientsBookingResponse>

    @GET("/user/client/viewbooking/{resumeId}")
    suspend fun getCleintBookingById(@Path("resumeId") resumeId: Int): Response<ViewClientBooking>

    @GET("/user/chat/get")
    suspend fun getChat(@Query("page") page: Int = 1, @Query("limit") limit: Int = 10): Response<GetChats>

    @POST("/user/client/booktradesman/{tradesman_Id}")
    suspend fun booktradesman(
        @Body request: BookTradesmanRequest,
        @Path("tradesman_Id") tradesman_Id: Int
    ): Response<BookTradesmanResponse>

    @POST("/user/client/reporttradesman/{tradesmanId}")
    suspend fun report(
        @Body request: ReportRequest,
        @Path("tradesmanId") tradesmanId: Int
    ): Response<ReportResponse>

    @GET("/user/client/view/tradesman/rating/{tradesmanId}")
    suspend fun getRatingsById(@Path("tradesmanId") resumeId: Int): Response<List<ratingsItem>>

    @PUT("/user/client/work/status/{booking_id}")
    suspend fun updateBookingTradesmanStatus(@Body request : TradesmanWorkStatusRequest, @Path("booking_id") bookingId: Int): Response<TradesmanWorkStatusResponse>
    @PUT("/user/tradesman/bookings/status/{booking_id}")
    suspend fun updateBookingClientStatus(@Body request : ClientWorkStatusRequest, @Path("booking_id") bookingId: Int): Response<ClientWorkStatusResponse>

    @POST("/user/client/rate/tradesman/{tradesman_id}")
    suspend fun ratetradesman(@Body request : rateTradesmanRequest, @Path("tradesman_id") bookingId: Int): Response<rateTradesmanResponse>

    @POST("/user/client/job/apply/{jobId}")
    suspend fun postJobApplication(
        @Body request: PostJobApplication,
        @Path("jobId") jobId: Int
    ): Response<PostJobApplicationResponse>

    @GET("/user/tradesman/job-applications")
    suspend fun getMyJobApplications(@Query("page") page: Int = 1, @Query("limit") limit: Int = 10 ): Response<GetJobApplicationData>

    @PUT("/user/tradesman/job-applications/change_status/{jobId}")
    suspend fun updateJobApplicationStatus(@Path("jobId") jobId: Int, @Body request: UpdateStatus): Response<UpdateStatusResponse>

    @GET("/user/client/job-applications")
    suspend fun getMyJobApplicants(@Query("page") page: Int = 1, @Query("limit") limit: Int = 10 ): Response<GetJobApplicantsData>

    @GET("/user/message/{chatId}")
    suspend fun getConversation(@Path("chatId") chatId: Int, @Query("page") page: Int = 1, @Query("limit") limit: Int = 10): Response<List<GetMessages>>

    @GET("/user/tradesman/getResume/Details")
    suspend fun getTradesmanResume(): Response<viewResume>

    @Multipart
    @POST("/user/tradesman/submit/resume")
    suspend fun submitResume(
        @Part("specialty") specialty: RequestBody,
        @Part("about_me") aboutme: RequestBody,
        @Part("work_fee") workfee: RequestBody,
        @Part("prefered_location") preferedLocation: RequestBody,
        @Part validIdFront: MultipartBody.Part, // File upload
        @Part validIdBack: MultipartBody.Part,// File upload
        @Part document: MultipartBody.Part // File upload

    ): Response<SubmitResumeResponse>

    @GET("/user/job-applications/view/{jobId}")
    suspend fun viewJobApplication(@Path("jobId") jobId: Int): Response<ViewJobApplicationResponse>

    @GET("/user/tradesman/getbooking")
    suspend fun getTradesmanBooking(@Query("page") page: Int = 1, @Query("limit") limit: Int = 10) : Response <GetTradesmanBookingResponse>

    @POST("/user/forgot/otpsend")
    suspend fun forgotPass(@Body request: ForgotPasswordRequest) : Response <ForgotPasswordResponse>

    @PUT("/user/forgot/resetpassword")
    suspend fun resetPass(@Body request : ResetPasswordRequest) : Response <ResetPasswordResponse>

    @PUT("/user/change/password")
    suspend fun updatePass(@Body request : ChangePasswordRequest) : Response <ChangePasswordResponse>

    @PUT("/client/jobs/update/{jobId}")
    suspend fun updateJob(@Path("jobId") jobId: Int, @Body request: UpdateJob): Response<UpdateJobResponse>

    @GET("/user/notification")
    suspend fun getNotifications(@Query("page") page: Int = 1, @Query("limit") limit: Int = 10): Response<GetNotification>

    @Multipart
    @POST("/client/update/profile_picture")
    suspend fun updateClientProfilePicture(@Part profilePic: MultipartBody.Part): Response<UpdateProfilePictureResponse>

    @PUT("/client/update/profile_address")
    suspend fun updateClientAddress(@Body request: UpdateAddress): Response<UpdateAddressResponse>

    @Multipart
    @POST("/user/tradesman/update/profile")
    suspend fun updateTradesmanProfile(@Part profilePic: MultipartBody.Part): Response<UpdateTradesmanProfileResponse>


    @PUT("/user/tradesman/update/resume/details")
    suspend fun  updateTradesmanDetail(@Body request: UpdateTradesmanDetailsRequest) : Response<UpdateTradesmanDetailsResponse>

}
