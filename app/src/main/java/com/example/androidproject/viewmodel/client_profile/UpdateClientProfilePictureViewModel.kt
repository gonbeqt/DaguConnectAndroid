package com.example.androidproject.viewmodel.client_profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.JsonErrorParser
import com.example.androidproject.model.UpdateProfilePictureResponse
import com.example.androidproject.model.UpdateStatus
import com.example.androidproject.model.UpdateStatusResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.lang.IllegalStateException

class UpdateClientProfilePictureViewModel(private val apiService: ApiService) : ViewModel() {
    private val _updateClientProfileState = MutableStateFlow<UpdateClientProfilePictureState>(
        UpdateClientProfilePictureState.Idle
    )
    val updateClientProfileState: StateFlow<UpdateClientProfilePictureState> = _updateClientProfileState

    fun updateClientProfile(profilePicture: Uri, context: Context) {
        viewModelScope.launch {
            try {
                val profPicture = createMultipartBodyPart(context, profilePicture, "profile_picture")
                Log.d("ProfileUpdate", "Uploading profile picture...")
                val response = apiService.updateClientProfilePicture(profilePic = profPicture)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _updateClientProfileState.value = UpdateClientProfilePictureState.Success(response)
                    } else {
                        _updateClientProfileState.value = UpdateClientProfilePictureState.Error("Response body is null")
                    }
                } else {
                    val errorJson = response.errorBody()?.string()
                    Log.e("ProfileUpdate", "Error response: $errorJson")
                    val errorMessage = JsonErrorParser.extractField(errorJson, "message") ?: "Unknown error"
                    _updateClientProfileState.value = UpdateClientProfilePictureState.Error(errorMessage)
                }
            } catch (e: Exception) {
                Log.e("ProfileUpdate", "Upload failed: ${e.message}", e)
                _updateClientProfileState.value = UpdateClientProfilePictureState.Error(e.message ?: "Unknown error")
            }
        }
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

    fun resetState() {
        _updateClientProfileState.value = UpdateClientProfilePictureState.Idle
    }

    sealed class UpdateClientProfilePictureState {
        data object Idle : UpdateClientProfilePictureState()
        data object Loading : UpdateClientProfilePictureState()
        data class Success(val data: Response<UpdateProfilePictureResponse>) : UpdateClientProfilePictureState()
        data class Error(val message: String) : UpdateClientProfilePictureState()
    }
}