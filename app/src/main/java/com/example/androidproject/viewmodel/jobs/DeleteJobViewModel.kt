package com.example.androidproject.viewmodel.jobs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DeleteJobViewModel(private val apiService: ApiService) : ViewModel()  {
    private val _deleteJobResult = MutableStateFlow<Boolean?>(null)
    val deleteJobResult: StateFlow<Boolean?> = _deleteJobResult.asStateFlow()

    fun deleteJob(jobId: Int){
        viewModelScope.launch {
            try {
                val response = apiService.deleteJob(jobId)
                _deleteJobResult.value = response.isSuccessful
            } catch (e: Exception){
                _deleteJobResult.value = false
            }
        }
    }
}