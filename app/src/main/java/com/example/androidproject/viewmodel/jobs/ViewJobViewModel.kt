package com.example.androidproject.viewmodel.jobs

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.Job
import com.example.androidproject.model.ViewJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ViewJobViewModel(private val apiService: ApiService, context: Context) : ViewModel() {
    private val _jobState = MutableStateFlow<JobState>(JobState.Idle)
    val jobState = _jobState.asStateFlow()

    fun getJobById(id: Int) {
        Log.d("Job ID", id.toString())
        viewModelScope.launch {
            _jobState.value = JobState.Loading
            try {
                val response = apiService.getJobById(id)
                val body = response.body()
                Log.d("Job ID: Response", body.toString())
                if (response.isSuccessful && body != null) {
                    _jobState.value = JobState.Success(body)
                } else {
                    _jobState.value = JobState.Error(response.message())
                }
            } catch (e: Exception) {
                _jobState.value = JobState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    sealed class JobState {
        object Idle : JobState()
        object Loading : JobState()
        data class Success(val data: Job) : JobState()
        data class Error(val message: String) : JobState()
    }
}