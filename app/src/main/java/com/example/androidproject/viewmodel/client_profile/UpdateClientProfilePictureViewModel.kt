package com.example.androidproject.viewmodel.client_profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.JsonErrorParser
import com.example.androidproject.model.UpdateProfilePictureResponse
import com.example.androidproject.model.UpdateStatus
import com.example.androidproject.model.UpdateStatusResponse
import com.example.androidproject.viewmodel.Tradesman_Profile.UpdateTradesmanProfileViewModel.UpdateTradesmanProfileState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

class UpdateClientProfilePictureViewModel(private val apiService: ApiService): ViewModel() {
    private val _updateClientProfileState = MutableStateFlow<UpdateClientProfilePictureState>(
        UpdateClientProfilePictureState.Idle)
    val updateClientProfileState: StateFlow<UpdateClientProfilePictureState> = _updateClientProfileState

    fun updateClientProfile(profilePicture: Uri,context: Context) {
        viewModelScope.launch {

            try {
                //Converts into URI
                val profPicture = createMultipartBodyPart(context, profilePicture, "profile_picture")
                val response = apiService.updateClientProfilePicture(profilePic = profPicture)
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        _updateClientProfileState.value = UpdateClientProfilePictureState.Success(response)
                    }else{
                        _updateClientProfileState.value = UpdateClientProfilePictureState.Error("Response body is null")
                    }
                }else{
                    val errorJson = response.errorBody()?.string()
                    println("Error response: $errorJson") // Debug log
                    val errorMessage = JsonErrorParser.extractField(errorJson, "message") ?: "Unknown error"
                    _updateClientProfileState.value = UpdateClientProfilePictureState.Error(errorMessage)
                }
            }catch (e: Exception){
                _updateClientProfileState.value = UpdateClientProfilePictureState.Error(e.message ?: "Unknown error")
            }


        }
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

    fun resetState() {
        _updateClientProfileState.value = UpdateClientProfilePictureState.Idle
    }

    sealed class UpdateClientProfilePictureState{
        data object Idle: UpdateClientProfilePictureState()
        data object Loading: UpdateClientProfilePictureState()
        data class Success(val data: Response<UpdateProfilePictureResponse>): UpdateClientProfilePictureState()
        data class Error(val message: String): UpdateClientProfilePictureState()

    }
}