package com.example.androidproject.viewmodel.job_application

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.PostJobApplication
import com.example.androidproject.model.PostJobApplicationResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class PostJobApplicationViewModel(private val apiService: ApiService, private val context: Context): ViewModel()  {
    private val _postJobApplicationState = MutableStateFlow<PostJobApplicationState>(PostJobApplicationState.Idle)
    val postJobApplicationState = _postJobApplicationState.asStateFlow()

    fun postJobApplication(jobId: Int, qualificationSummary: String){
        viewModelScope.launch {
            val post = PostJobApplication(qualificationSummary)
            _postJobApplicationState.value = PostJobApplicationState.Loading
            try {
                val response = apiService.postJobApplication(post, jobId)
                _postJobApplicationState.value = PostJobApplicationState.Success(response)
            }catch (e: Exception){
                _postJobApplicationState.value = PostJobApplicationState.Error(e.message ?: "Unknown error")
            }
        }
    }

    sealed class PostJobApplicationState{
        object Idle: PostJobApplicationState()
        object Loading: PostJobApplicationState()
        data class Success(val data: Response<PostJobApplicationResponse>): PostJobApplicationState()
        data class Error(val message: String): PostJobApplicationState()
    }
}