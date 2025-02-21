package com.example.androidproject.viewmodel.jobs

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.PostJobResponse
import com.example.androidproject.model.RequestJobs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostJobViewModel(private val apiService: ApiService) : ViewModel()  {
    private val _postJobState = MutableStateFlow<PostJobState>(PostJobState.Idle)
    val postJobState = _postJobState.asStateFlow()

    fun postJob(salary: Double, applicantLimitCount: Int, jobType: String, jobDescription: String, address: String, status: String, deadline: String) {
        viewModelScope.launch {
            _postJobState.value = PostJobState.Loading
            try {
                val response = apiService.postJob(RequestJobs(salary, applicantLimitCount, jobType, jobDescription, address, status, deadline))
                if (response.isSuccessful) {
                    val message = response.body()
                    if (message != null) {
                        _postJobState.value = PostJobState.Success(message)
                    } else {
                        _postJobState.value = PostJobState.Error("Response body is null")
                        Log.e("PostJobViewModel", "Response body is null")
                    }
                } else {
                    _postJobState.value = PostJobState.Error("Error: ${response.code()}")
                    Log.e("PostJobViewModel", "Exception: ${response.code()}")
                }
            } catch (e: Exception) {
                _postJobState.value = PostJobState.Error("Exception: ${e.message}")
                Log.e("PostJobViewModel", "Exception: ${e.message}", e)
            }
        }
    }

    sealed class PostJobState {
        data object Idle : PostJobState()
        data object Loading : PostJobState()
        data class Success(val message: PostJobResponse) : PostJobState()
        data class Error(val message: String) : PostJobState()
    }
}