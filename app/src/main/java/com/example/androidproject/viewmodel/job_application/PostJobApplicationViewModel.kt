package com.example.androidproject.viewmodel.job_application

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.PostJobApplication
import com.example.androidproject.model.PostJobApplicationResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class PostJobApplicationViewModel(private val apiService: ApiService, private val context: Context): ViewModel()  {
    private val _postJobApplicationState = MutableStateFlow<PostJobApplicationState>(PostJobApplicationState.Idle)
    val postJobApplicationState: StateFlow<PostJobApplicationState> = _postJobApplicationState


    fun postJobApplication(jobId: Int, qualificationSummary: String) {
        viewModelScope.launch {
            val post = PostJobApplication(qualificationSummary)
            _postJobApplicationState.value = PostJobApplicationState.Loading
            try {
                val response = apiService.postJobApplication(post, jobId)

                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        _postJobApplicationState.value = PostJobApplicationState.Success(response)
                        Log.d("PostJobApplicationViewModel Response", "Response: $responseBody")
                    } else {
                        // Handle case where response is a plain string instead of JSON
                        val rawMessage = response.errorBody()?.string() ?: "Application successful!"
                        _postJobApplicationState.value = PostJobApplicationState.SuccessRaw(rawMessage)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBody)
                    _postJobApplicationState.value = PostJobApplicationState.Error(errorMessage)                }

            } catch (e: Exception) {
                _postJobApplicationState.value = PostJobApplicationState.Error(e.message ?: "Unknown error")
            }
        }
    }
    private fun extractErrorMessage(errorBody: String?): String {
        return try {
            val jsonObject = org.json.JSONObject(errorBody ?: "{}") // Convert to JSON
            jsonObject.optString("message", "Something went wrong") // Extract message key
        } catch (e: Exception) {
            "Something went wrong"
            }
        }

    fun resetState() {
        _postJobApplicationState.value = PostJobApplicationState.Idle
    }

    sealed class PostJobApplicationState {
        data object Idle : PostJobApplicationState()
        data object Loading : PostJobApplicationState()
        data class Success(val data: Response<PostJobApplicationResponse>) : PostJobApplicationState()
        data class SuccessRaw(val message: String) : PostJobApplicationState()
        data class Error(val message: String) : PostJobApplicationState()
    }

}