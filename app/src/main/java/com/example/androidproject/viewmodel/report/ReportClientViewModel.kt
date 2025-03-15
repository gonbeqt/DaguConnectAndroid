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
import com.example.androidproject.model.client.ReportClientResponse
import com.example.androidproject.viewmodel.report.ReportTradesmanViewModel.ReportState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class ReportClientViewModel(private val apiService: ApiService):ViewModel() {
    private val _reportClientState = MutableStateFlow<ReportClientState>(ReportClientState.Idle)
    val reportClientState : StateFlow<ReportClientState> = _reportClientState

    fun reportClient( clientId: Int, report_reason: String, report_details: String, report_attachment: Uri,context: Context){
        viewModelScope.launch {
            if (_reportClientState.value is ReportClientState.Loading) return@launch // Prevent multiple simultaneous reports
            _reportClientState.value = ReportClientState.Loading
            try {
                val reportReason = report_reason.toRequestBody("text/plain".toMediaType())
                val reportDetails = report_details.toRequestBody("text/plain".toMediaType())
                val reportAttachment = createMultipartBodyPart(context, report_attachment, "report_attachment")
                val response =apiService.reportClient(reportReason, reportDetails, reportAttachment, clientId)
                if (response.isSuccessful){
                    _reportClientState.value = ReportClientState.Success(response.body())
                }else{
                    val errorJson = response.errorBody()?.string()
                    val errorMessage = JsonErrorParser.extractField(errorJson, "message") ?: "Unknown error"
                    errorJson?.let { Log.d("tester", it) }
                    Log.d("tester", errorMessage)
                    _reportClientState.value = ReportClientState.Error(errorMessage)
                }
            }catch (e: Exception){
                _reportClientState.value = ReportClientState.Error("An unexpected error occurred: ${e.message}")
            }
        }
    }

    fun resetState() {
        _reportClientState.value = ReportClientState.Idle // Add an idle state
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

    sealed class ReportClientState{
        object Idle: ReportClientState()
        object Loading: ReportClientState()
        data class Success(val data: ReportClientResponse?): ReportClientState()
        data class Error(val message: String): ReportClientState()

    }
}