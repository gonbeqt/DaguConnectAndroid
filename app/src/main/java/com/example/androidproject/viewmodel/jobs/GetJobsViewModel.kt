package com.example.androidproject.viewmodel.jobs

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.GetJobs
import com.example.androidproject.model.JobsResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GetJobsViewModel (private val apiService: ApiService, private val context: Context): ViewModel(){
    private val _jobsState = MutableStateFlow<JobsState>(JobsState.Idle)
    val jobsState = _jobsState.asStateFlow() // Exposing the state as StateFlow

    fun getJobs(){
        viewModelScope.launch {
            _jobsState.value = JobsState.Loading
            try {
                val response = apiService.getJobs()
                val body = response.body()
                if (response.isSuccessful) {
                    if (body != null) {
                        _jobsState.value = JobsState.Success(body.jobs)
                    }
                } else {
                    _jobsState.value = JobsState.Error(response.message())
                }
            } catch (e: Exception) {
                _jobsState.value = JobsState.Error(e.localizedMessage ?: "Unknown error")
                }
            }
        }
    sealed class JobsState {
        object Idle : JobsState()
        object Loading : JobsState()
        data class Success(val data: List<GetJobs>) : JobsState()
        data class Error(val message: String) : JobsState()
    }
}
