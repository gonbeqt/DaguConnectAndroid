package com.example.androidproject.viewmodel.Tradesman_Profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log

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
import java.io.ByteArrayOutputStream

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
        _updateTradesmanProfileState.value = UpdateTradesmanProfileState.Idle
    }

    sealed class UpdateTradesmanProfileState{
        object Loading : UpdateTradesmanProfileState()
        object Idle : UpdateTradesmanProfileState()
        data class Success(val message: UpdateTradesmanProfileResponse) : UpdateTradesmanProfileState()
        data class Error(val message: String) : UpdateTradesmanProfileState()
    }
}


