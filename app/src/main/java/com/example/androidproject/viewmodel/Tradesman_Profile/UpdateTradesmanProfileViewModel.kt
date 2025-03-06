package com.example.androidproject.viewmodel.Tradesman_Profile

import android.content.Context
import android.net.Uri

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.JsonErrorParser
import com.example.androidproject.model.client.UpdateTradesmanProfileResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class UpdateTradesmanProfileViewModel (private val apiService: ApiService) :ViewModel(){
    private val _updateTradesmanProfileState  = MutableStateFlow<UpdateTradesmanProfileState>(
        UpdateTradesmanProfileState.Idle
    )
    val updateTradesmanProfileState : StateFlow<UpdateTradesmanProfileState> =_updateTradesmanProfileState

    fun updateTradesmanProfile(profilePic: Uri, context:Context){
        viewModelScope.launch {
            _updateTradesmanProfileState.value = UpdateTradesmanProfileState.Loading
            try{
                //Converts into URI
                val profilePicture = createMultipartBodyPart(context, profilePic, "profile_pic")

                val response = apiService.updateTradesmanProfile( profilePic = profilePicture)
                if (response.isSuccessful){
                    val body = response.body()
                    if(body!= null){
                        _updateTradesmanProfileState.value =
                            UpdateTradesmanProfileState.Success(body)
                    }else{
                        _updateTradesmanProfileState.value =
                            UpdateTradesmanProfileState.Error("Response body is null")
                    }
                }else {
                    val errorJson = response.errorBody()?.string()
                    println("Error response: $errorJson") // Debug log
                    val errorMessage = JsonErrorParser.extractField(errorJson, "message") ?: "Unknown error"
                    _updateTradesmanProfileState.value = UpdateTradesmanProfileState.Error(errorMessage)
                }
            }catch (e:Exception){
                _updateTradesmanProfileState.value =
                    UpdateTradesmanProfileState.Error(e.message.toString())
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
        _updateTradesmanProfileState.value = UpdateTradesmanProfileState.Idle
    }

    sealed class UpdateTradesmanProfileState{
        object Loading : UpdateTradesmanProfileState()
        object Idle : UpdateTradesmanProfileState()
        data class Success(val message: UpdateTradesmanProfileResponse) : UpdateTradesmanProfileState()
        data class Error(val message: String) : UpdateTradesmanProfileState()
    }
}


