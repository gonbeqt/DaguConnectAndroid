package com.example.androidproject.viewmodel.jobs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.GetJobs
import com.example.androidproject.model.GetMyJobs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class GetClientPostedJobsViewModel(private val apiService: ApiService) : ViewModel() {

    // State to hold the list of jobs or loading/error states
    private val _jobState = MutableStateFlow<JobState>(JobState.Loading)
    val jobState: StateFlow<JobState> = _jobState.asStateFlow()

    fun getClientPostedJobs(clientId: Int) {
        viewModelScope.launch {
            _jobState.value = JobState.Loading // Indicate loading state
            try {
                val response = apiService.getJobsPostedByClient(clientId)
                if (response.isSuccessful) {
                    val jobData = response.body() ?: GetMyJobs(emptyList(), 0, 0) // Default if null
                    _jobState.value = JobState.Success(jobData.jobs, jobData.currentPage, jobData.totalPages)
                } else {
                    _jobState.value = JobState.Error("Failed to fetch jobs: ${response.code()}")
                }
            } catch (e: IOException) {
                _jobState.value = JobState.Error("Network error: ${e.message}")
            } catch (e: HttpException) {
                _jobState.value = JobState.Error("Server error: ${e.message}")
            } catch (e: Exception) {
                _jobState.value = JobState.Error("Unexpected error: ${e.message}")
            }
        }
    }

    sealed class JobState {
        object Loading : JobState()
        data class Success(val jobs: List<GetJobs>, val currentPage: Int, val totalPages: Int) : JobState()
        data class Error(val message: String) : JobState()
    }
}