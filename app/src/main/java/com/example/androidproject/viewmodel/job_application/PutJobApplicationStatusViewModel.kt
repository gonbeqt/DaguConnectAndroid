package com.example.androidproject.viewmodel.job_application

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.JsonErrorParser
import com.example.androidproject.model.UpdateStatus
import com.example.androidproject.model.UpdateStatusResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class PutJobApplicationStatusViewModel(private val apiService: ApiService, private val context: Context): ViewModel() {
    private val _putJobApplicationStatusState = MutableStateFlow<PutJobApplicationState>(
        PutJobApplicationState.Idle)
    val putJobApplicationState: StateFlow<PutJobApplicationState> = _putJobApplicationStatusState

    fun updateJobApplicationStatus(id: Int, status: String, reason: String) {
        viewModelScope.launch {
            _putJobApplicationStatusState.value = PutJobApplicationState.Loading
            val data  = UpdateStatus(status, reason)
            val put = apiService.updateJobApplicationStatus(id, data)
            try {
                if (put.isSuccessful) {
                    _putJobApplicationStatusState.value = PutJobApplicationState.Success(put, status)
                } else {
                    val errorJson = put.errorBody()?.string()
                    println("Error response: $errorJson") // Debug log
                    val errorMessage = JsonErrorParser.extractField(errorJson, "message") ?: "Unknown error"
                    _putJobApplicationStatusState.value = PutJobApplicationState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _putJobApplicationStatusState.value = PutJobApplicationState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun resetState() {
        _putJobApplicationStatusState.value = PutJobApplicationState.Idle
    }

    sealed class PutJobApplicationState{
        data object Idle: PutJobApplicationState()
        data object Loading: PutJobApplicationState()
        data class Success(val data: Response<UpdateStatusResponse>,val status: String): PutJobApplicationState()
        data class Error(val message: String): PutJobApplicationState()

    }
}