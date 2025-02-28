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
import android.net.Uri

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

                // Convert URIs to MultipartBody.Part
                val documentPart = createMultipartBodyPart(context, documents, "document")
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
                    println("Error response: $errorJson") // Debug log
                    val errorMessage = JsonErrorParser.extractField(errorJson, "message") ?: "Unknown error"
                    _submitResumeState.value = SubmitResumeState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _submitResumeState.value = SubmitResumeState.Error("An unexpected error occurred: ${e.message}")
            }
        }
    }

    fun resetState() {
        _submitResumeState.value = SubmitResumeState.Idle
    }

    private fun createMultipartBodyPart(context: Context, uri: Uri, name: String): MultipartBody.Part {
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"
        println("Uploading $name with MIME type: $mimeType") // Debug log
        val fileStream = contentResolver.openInputStream(uri)
        val byteArray = fileStream?.readBytes() ?: byteArrayOf()
        fileStream?.close()

        // Determine a proper filename based on the field name and MIME type
        val fileExtension = when (mimeType) {
            "application/pdf" -> ".pdf"
            "application/msword" -> ".doc"
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> ".docx"
            "image/jpeg" -> ".jpg"
            "image/png" -> ".png"
            else -> "" // Fallback, though validation should prevent this
        }
        val fileName = "$name$fileExtension" // e.g., "document.pdf", "valid_id_front.png"

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