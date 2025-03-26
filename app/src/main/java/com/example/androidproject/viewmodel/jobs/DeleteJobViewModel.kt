package com.example.androidproject.viewmodel.jobs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.jobs.PostJobViewModel.PostJobState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DeleteJobViewModel(private val apiService: ApiService) : ViewModel() {



    private val _deleteJobResult = MutableStateFlow<DeleteJobResult>(DeleteJobResult.Idle)
    val deleteJobResult = _deleteJobResult.asStateFlow()

    fun deleteJob(jobId: Int) {
        viewModelScope.launch {
            _deleteJobResult.value = DeleteJobResult.Loading
            try {
                val response = apiService.deleteJob(jobId)
                _deleteJobResult.value = if (response.isSuccessful) {
                    DeleteJobResult.Success
                } else {
                    DeleteJobResult.Error
                }
            } catch (e: Exception) {
                _deleteJobResult.value = DeleteJobResult.Error
            }
        }
    }

    fun resetState() {
        _deleteJobResult.value = DeleteJobResult.Idle
    }
    sealed class DeleteJobResult {
        object Success : DeleteJobResult()
        object Error : DeleteJobResult()
        data object Loading : DeleteJobResult()
        object Idle : DeleteJobResult()  // Represents the reset state
    }
}
