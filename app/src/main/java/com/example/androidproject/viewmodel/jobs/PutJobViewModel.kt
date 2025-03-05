package com.example.androidproject.viewmodel.jobs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.UpdateJob
import com.example.androidproject.model.UpdateJobResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class PutJobViewModel (private val apiService: ApiService): ViewModel() {
    private val _putJobState = MutableStateFlow<PutJobState>(
        PutJobState.Idle)
    val putJobState: StateFlow<PutJobState> = _putJobState

    fun updateJobApplicationStatus(id: Int, updateJob: UpdateJob) {
        viewModelScope.launch {
            _putJobState.value = PutJobState.Loading
            val put = apiService.updateJob(id, updateJob)
            try {
                if (put.isSuccessful) {
                    _putJobState.value = PutJobState.Success(put)
                } else {
                    _putJobState.value = PutJobState.Error(put.message())
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