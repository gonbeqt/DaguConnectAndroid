package com.example.androidproject.viewmodel.jobs

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.JsonErrorParser
import com.example.androidproject.model.UpdateJob
import com.example.androidproject.model.UpdateJobResponse
import com.example.androidproject.viewmodel.bookings.UpdateBookingTradesmanViewModel.UpdateWorkStatus
import com.example.androidproject.viewmodel.report.ReportTradesmanViewModel.ReportState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class PutJobViewModel (private val apiService: ApiService): ViewModel() {
    private val _putJobState = MutableStateFlow<PutJobState>(PutJobState.Idle)
    val putJobState: StateFlow<PutJobState> = _putJobState

    fun updateJobApplicationStatus(id: Int, updateJob: UpdateJob) {
        viewModelScope.launch {
            if (_putJobState.value is PutJobState.Loading) return@launch // Prevent multiple simultaneous reports
            _putJobState.value = PutJobState.Loading
            val put = apiService.updateJob(id, updateJob)
            try {
                if (put.isSuccessful) {
                    _putJobState.value = PutJobState.Success(put)
                } else {
                    // Handle error response
                    val errorJson = put.errorBody()?.string()
                    val errorMessage = JsonErrorParser.extractField(errorJson, "message") ?: "Unknown error"
                    _putJobState.value = PutJobState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _putJobState.value = PutJobState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun resetState() {
        _putJobState.value = PutJobState.Idle
    }

    sealed class PutJobState{
        data object Idle: PutJobState()
        data object Loading: PutJobState()
        data class Success(val data: Response<UpdateJobResponse>): PutJobState()
        data class Error(val message: String): PutJobState()

    }
}