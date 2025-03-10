package com.example.androidproject.viewmodel.Resumes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.JsonErrorParser
import com.example.androidproject.model.client.SubmitResumeResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import java.io.ByteArrayOutputStream

class SubmitResumeViewModel(private val apiService: ApiService) : ViewModel() {
    private val _submitResumeState = MutableStateFlow<SubmitResumeState>(SubmitResumeState.Idle)
    val submitResumeState = _submitResumeState.asStateFlow()

    fun submitResume(
        specialty: String,
        aboutme: String,
        workfee: Int,
        preferedworklocation: String,
        documents: Uri,
        valididfront: Uri,
        valididback: Uri,
        context: Context
    ) {
        viewModelScope.launch {
            _submitResumeState.value = SubmitResumeState.Loading

            // Validate document format
            val documentValidationError = validateDocumentFormat(context, documents)
            if (documentValidationError != null) {
                _submitResumeState.value = SubmitResumeState.Error(documentValidationError)
                return@launch
            }

            try {
                // Convert strings to RequestBody
                val specialtyBody = specialty.toRequestBody("text/plain".toMediaType())
                val aboutMeBody = aboutme.toRequestBody("text/plain".toMediaType())
                val workFeeBody = workfee.toString().toRequestBody("text/plain".toMediaType())
                val locationBody = preferedworklocation.toRequestBody("text/plain".toMediaType())

                // Convert URIs to MultipartBody.Part based on type
                val documentPart = createMultipartBodyPartForDocument(context, documents, "document")
                val validIdFrontPart = createMultipartBodyPart(context, valididfront, "valid_id_front")
                val validIdBackPart = createMultipartBodyPart(context, valididback, "valid_id_back")

                val response = apiService.submitResume(
                    specialty = specialtyBody,
                    aboutme = aboutMeBody,
                    workfee = workFeeBody,
                    preferedLocation = locationBody,
                    document = documentPart,
                    validIdFront = validIdFrontPart,
                    validIdBack = validIdBackPart
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _submitResumeState.value = SubmitResumeState.Success(body)
                    } else {
                        _submitResumeState.value = SubmitResumeState.Error("Response body is null")
                    }
                } else {
                    val errorJson = response.errorBody()?.string()
                    Log.e("ProfileUpdate", "Error response: $errorJson") // Use Log.e for errors
                    val errorMessage = JsonErrorParser.extractField(errorJson, "message") ?: "Unknown error"
                    _submitResumeState.value = SubmitResumeState.Error(errorMessage)
                }
            } catch (e: Exception) {
                Log.e("ProfileUpdate", "Upload failed: ${e.message}", e)
                _submitResumeState.value = SubmitResumeState.Error("An unexpected error occurred: ${e.message}")
            }
        }
    }
    fun resetState() {
        _submitResumeState.value = SubmitResumeState.Idle
    }

    private fun createMultipartBodyPartForDocument(context: Context, uri: Uri, name: String): MultipartBody.Part {
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"
        Log.d("FileUpload", "URI: $uri, MIME Type: $mimeType")

        val inputStream = contentResolver.openInputStream(uri)
        if (inputStream == null) {
            Log.e("FileUpload", "Failed to open input stream for URI: $uri")
            throw IllegalStateException("Unable to open file stream")
        }

        val byteArray = inputStream.use { it.readBytes() }
        inputStream.close()

        Log.d("FileUpload", "Byte array length: ${byteArray.size}")
        if (byteArray.isEmpty()) {
            Log.e("FileUpload", "Empty byte array for URI: $uri")
            throw IllegalStateException("No data read from file")
        }
        if (byteArray.size > 10_000_000) {
            Log.e("FileUpload", "File size exceeds 10 MB: ${byteArray.size}")
            throw IllegalArgumentException("File size exceeds 10 MB limit")
        }

        val fileName = "document.${mimeType.split("/").last()}" // e.g., document.pdf
        val requestBody = byteArray.toRequestBody(mimeType.toMediaType())
        return MultipartBody.Part.createFormData(name, fileName, requestBody)
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

    private fun validateDocumentFormat(context: Context, document: Uri): String? {
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(document) ?: "application/octet-stream"
        val validDocumentTypes = listOf(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        )
        return if (!validDocumentTypes.contains(mimeType)) {
            "Invalid certificate format. Only PDF, DOC, and DOCX files are allowed."
        } else {
            null
        }
    }

    sealed class SubmitResumeState {
        object Idle : SubmitResumeState()
        object Loading : SubmitResumeState()
        data class Success(val data: SubmitResumeResponse) : SubmitResumeState()
        data class Error(val message: String) : SubmitResumeState()
    }
}