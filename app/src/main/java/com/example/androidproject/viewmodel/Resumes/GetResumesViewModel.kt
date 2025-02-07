package com.example.androidproject.viewmodel.Resumes

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.data.preferences.TokenManager
import com.example.androidproject.model.GetJobs
import com.example.androidproject.model.client.resumesItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GetResumesViewModel(private val apiService: ApiService, private val context: Context):ViewModel() {
    private val _resumeState = MutableStateFlow<ResumeState>(ResumeState.Idle)
    val resumeState = _resumeState.asStateFlow()

    private var isFetching = false
    private var currentPage = 1
    private val resumeList = mutableListOf<resumesItem>()
    private var isLastPage = false

    fun getResumes(){
        if (isFetching || isLastPage) return // Stop if already fetching or at the last page
        viewModelScope.launch {
            _resumeState.value = ResumeState.Loading
            try {
                val token = TokenManager.getToken()
                val response = apiService.getResumes("Bearer $token",page = currentPage )
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.isNotEmpty()) {
                        resumeList.addAll(body)
                        _resumeState.value = ResumeState.Success(body.toList())
                        currentPage++ // Move to the next page
                    } else {
                        _resumeState.value = ResumeState.Error("No resumes found.")
                    }
                } else {
                    _resumeState.value = ResumeState.Error("Failed to retrieve resumes.")
                }
            } catch (e: Exception) {
                _resumeState.value = ResumeState.Error(e.message ?: "An error occurred.")
            }finally {
                isFetching = false // Reset flag
            }
        }
    }


    sealed class ResumeState(){
        object Idle : ResumeState()
        object Loading : ResumeState()
        data class Success(val data: List<resumesItem>) : ResumeState()
        data class Error(val message: String) : ResumeState()

    }
}