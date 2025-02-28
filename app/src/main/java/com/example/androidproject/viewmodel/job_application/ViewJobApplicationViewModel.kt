package com.example.androidproject.viewmodel.job_application

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ViewJobApplicationViewModel (private val apiService: ApiService, context: Context) : ViewModel() {
    private val _viewJobApplicationState = MutableStateFlow<ViewJobApplicationState>(ViewJobApplicationState.Idle)
    val viewApplicationState = _viewJobApplicationState.asStateFlow()

    fun viewJobApplication(id: Int) {
        Log.d("Job application ID", id.toString())
        viewModelScope.launch {
            _viewJobApplicationState.value = ViewJobApplicationState.Loading
            try {
                val response = apiService.getJobById(id)
                val body = response.body()
                Log.d("Job ID: Response", body.toString())
                if (response.isSuccessful && body != null) {
                    _viewJobApplicationState.value = ViewJobApplicationState.Success(body)
                } else {
                    _viewJobApplicationState.value = ViewJobApplicationState.Error(response.message())
                }
            } catch (e: Exception) {
                _viewJobApplicationState.value = ViewJobApplicationState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    sealed class ViewJobApplicationState {
        object Idle : ViewJobApplicationState()
        object Loading : ViewJobApplicationState()
        data class Success(val data: Job) : ViewJobApplicationState()
        data class Error(val message: String) : ViewJobApplicationState()
    }
}