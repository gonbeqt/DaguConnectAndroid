package com.example.androidproject.viewmodel.jobs

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.JsonErrorParser
import com.example.androidproject.model.CreateJob
import com.example.androidproject.model.CreateJobResponse
import com.example.androidproject.model.PostJobResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostJobViewModel(private val apiService: ApiService, private val context: Context) : ViewModel() {
    private val _postJobState = MutableStateFlow<PostJobState>(PostJobState.Idle)
    val postJobState = _postJobState.asStateFlow()

    fun postJob(clientFullName: String,  salary: Double, jobType: String, jobDescription: String, location: String, status: String, deadline: String) {
        val postJobRequest = CreateJob(clientFullName, salary, jobType, jobDescription, location, status, deadline)
        viewModelScope.launch {
            _postJobState.value = PostJobState.Loading
            try {
                val response = apiService.postJobs(postJobRequest)
                if (response.isSuccessful) {
                    _postJobState.value = response.body()?.let { PostJobState.Success(it) }!!
                } else {
                    val errorJson = response.errorBody()?.string()
                    val errorMessage = JsonErrorParser.extractField(errorJson, "message")
                    _postJobState.value = PostJobState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _postJobState.value = PostJobState.Error(e.localizedMessage ?: "Unknown error")
                }
            }
        }
    fun resetState() {
        _postJobState.value = PostJobState.Idle
    }
    sealed class PostJobState {
        object Idle : PostJobState()
        object Loading : PostJobState()
        data class Success(val data: CreateJobResponse) : PostJobState()
        data class Error(val message: String) : PostJobState()
    }
}
