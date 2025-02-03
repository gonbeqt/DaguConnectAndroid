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
    private var isFetching = false
    private var currentPage = 1
    private val jobsList = mutableListOf<GetJobs>()
    private var isLastPage = false
    fun getJobs(){
        if (isFetching || isLastPage) return // Prevent multiple calls at once

        viewModelScope.launch {
            isFetching = true // Set fetching flag
            _jobsState.value = JobsState.Loading
            try {
                val response = apiService.getJobs(page = currentPage)
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    if (body.jobs.isEmpty()){
                        isLastPage = true
                    } else {
                        jobsList.addAll(body.jobs) // Append new jobs to the list
                        _jobsState.value = JobsState.Success(jobsList.toList())
                        currentPage++ // Move to the next page
                    }
                } else {
                    _jobsState.value = JobsState.Error(response.message())
                }
            } catch (e: Exception) {
                _jobsState.value = JobsState.Error(e.localizedMessage ?: "Unknown error")
                isFetching = false
            }
            isFetching = false // Reset flag after request completes
        }
        }
    sealed class JobsState {
        object Idle : JobsState()
        object Loading : JobsState()
        data class Success(val data: List<GetJobs>) : JobsState()
        data class Error(val message: String) : JobsState()
    }
}
