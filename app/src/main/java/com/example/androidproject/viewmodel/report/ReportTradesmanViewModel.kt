package com.example.androidproject.viewmodel.report

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.JsonErrorParser
import com.example.androidproject.model.client.ReportTradesmanResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class ReportTradesmanViewModel (private val apiService: ApiService): ViewModel() {
    private val _reportState = MutableStateFlow<ReportState>(ReportState.Idle)
    val reportState : StateFlow<ReportState> = _reportState

    fun report(report_reason: String, report_details: String, report_attachment : Uri, context: Context, tradesmanId: Int) {
        viewModelScope.launch {
            if (_reportState.value is ReportState.Loading) return@launch // Prevent multiple simultaneous reports
            _reportState.value = ReportState.Loading
            try{
                val reportReason = report_reason.toRequestBody("text/plain".toMediaType())
                val reportDetails = report_details.toRequestBody("text/plain".toMediaType())
                val reportAttachment = createMultipartBodyPart(context, report_attachment, "report_attachment")
                val response = apiService.reportTradesman(
                    reportReason,
                    reportDetails,
                    reportAttachment,
                    tradesmanId
                )
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null){
                        _reportState.value = ReportState.Success(responseBody)
                    }else{
                        _reportState.value = ReportState.Error("Response body is null")
                    }
                }else{
                    val errorJson = response.errorBody()?.string()
                    val errorMessage = JsonErrorParser.extractField(errorJson, "message") ?: "Unknown error"
                    errorJson?.let { Log.d("tester", it) }
                    Log.d("tester", errorMessage)
                    _reportState.value = ReportState.Error(errorMessage)
                }

            }catch (e: Exception){
                Log.e("ReportViewModel", "Exception: ${e.message}", e)
                _reportState.value = ReportState.Error("An unexpected error occurred: ${e.message}")
            }
        }
    }
    fun resetState() {
        _reportState.value = ReportState.Idle // Add an idle state
    }
    private fun createMultipartBodyPart(context: Context, uri: Uri, name: String): MultipartBody.Part {
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri) ?: "image/jpeg"
        Log.d("FileUpload", "URI: $uri, MIME Type: $mimeType")

        // Read the original stream
        val inputStream = contentResolver.openInputStream(uri)
        if (inputStream == null) {
            Log.e("FileUpload", "Failed to open input stream for URI: $uri")
            throw IllegalStateException("Unable to open file stream")
        }

        // Decode the bitmap with error handling
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()

        if (bitmap == null) {
            Log.e("FileUpload", "Failed to decode bitmap for URI: $uri")
            throw IllegalStateException("Unable to decode image")
        }

        // Compress the image
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        val byteArray = outputStream.toByteArray()
        Log.d("FileUpload", "Compressed byte array length: ${byteArray.size}")

        if (byteArray.isEmpty()) {
            Log.e("FileUpload", "Empty byte array after compression for URI: $uri")
            throw IllegalStateException("No data after compression")
        }
        if (byteArray.size > 10_000_000) {
            Log.e("FileUpload", "Compressed file size exceeds 10 MB: ${byteArray.size}")
            throw IllegalArgumentException("File size exceeds 10 MB limit even after compression")
        }

        val fileName = "profile_picture.jpg"
        val requestBody = byteArray.toRequestBody(mimeType.toMediaType())
        return MultipartBody.Part.createFormData(name, fileName, requestBody)
    }





    sealed class ReportState {
        object Idle : ReportState()
        object Loading : ReportState()
        data class Success(val data: ReportTradesmanResponse?): ReportState()
        data class Error(val message: String): ReportState()
    }
}