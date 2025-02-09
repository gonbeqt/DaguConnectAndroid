package com.example.androidproject.viewmodel.jobs

import android.content.Context
import android.util.Log
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
    val jobsState = _jobsState.asStateFlow() // Exposing as StateFlow

    private val _isFetching = MutableStateFlow(false)
    val isFetching = _isFetching.asStateFlow()
    private var currentPage = 1
    private val jobsList = mutableListOf<GetJobs>()
    private var isLastPage = false

    fun getJobs() {
        if (_isFetching.value || isLastPage) return // Prevent duplicate calls

        viewModelScope.launch {
            _isFetching.value = true
            _jobsState.value = JobsState.Loading
            try {
                val response = apiService.getJobs(page = currentPage)
                if (response.isSuccessful) {
                    val body = response.body()
                    val newJobs = body?.jobs ?: emptyList()
                    Log.d("API Response", newJobs.toString())
                    if (newJobs.isEmpty()) {
                        isLastPage = true // Stop fetching if no new jobs
                    }
                    jobsList.addAll(newJobs)
                    _jobsState.value = JobsState.Success(jobsList.toList()) // ✅ Always update UI
                    currentPage++ // Move to the next page
                } else {
                    _jobsState.value = JobsState.Error("Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("GetJobsViewModel", "Error fetching jobs", e)
                _jobsState.value = JobsState.Error("Failed to load jobs. Please try again.")
            } finally {
                _isFetching.value = false
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
