package com.example.androidproject.viewmodel.Resumes

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.data.preferences.TokenManager
import com.example.androidproject.model.client.resumesItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GetResumesViewModel(private val apiService: ApiService, private val context: Context):ViewModel() {
    private val _resumeState = MutableStateFlow<ResumeState>(ResumeState.Idle)
    val resumeState = _resumeState.asStateFlow()


    fun getResumes(){
        viewModelScope.launch {
            _resumeState.value = ResumeState.Loading
            try {
                val token = TokenManager.getToken()
                val response = apiService.getResumes("Bearer $token" )
                val body = response.body()
                if (response.isSuccessful) {
                    if (body != null) {
                        _resumeState.value = ResumeState.Success(body)
                    } else {
                        _resumeState.value = ResumeState.Error("No resumes found.")
                    }
                } else {
                    _resumeState.value = ResumeState.Error("Failed to retrieve resumes.")
                }
            } catch (e: Exception) {
                _resumeState.value = ResumeState.Error(e.message ?: "An error occurred.")
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