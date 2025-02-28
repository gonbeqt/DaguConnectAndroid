package com.example.androidproject.viewmodel.job_application

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.PostJobApplicationResponse
import com.example.androidproject.model.UpdateStatus
import com.example.androidproject.model.UpdateStatusResponse
import com.example.androidproject.viewmodel.job_application.PostJobApplicationViewModel.PostJobApplicationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class PutJobApplicationStatusViewModel(private val apiService: ApiService, private val context: Context): ViewModel() {
    private val _PutJobApplicationStatusState = MutableStateFlow<PutJobApplicationState>(
        PutJobApplicationState.Idle)
    val postJobApplicationState: StateFlow<PutJobApplicationState> = _PutJobApplicationStatusState

    fun putJobApplicationStatus(id: Int, status: String, reason: String?) {
        viewModelScope.launch {
            _PutJobApplicationStatusState.value = PutJobApplicationState.Loading
            val data  = UpdateStatus(status, reason)
            val put = apiService.updateJobApplicationStatus(id, data)
            try {
                if (put.isSuccessful) {
                    _PutJobApplicationStatusState.value = PutJobApplicationState.Success(put)
                } else {
                    _PutJobApplicationStatusState.value = PutJobApplicationState.Error(put.message())
                }
            } catch (e: Exception) {
                _PutJobApplicationStatusState.value = PutJobApplicationState.Error(e.message ?: "An error occurred")
            }
        }
    }

    sealed class PutJobApplicationState{
        data object Idle: PutJobApplicationState()
        data object Loading: PutJobApplicationState()
        data class Success(val data: Response<UpdateStatusResponse>): PutJobApplicationState()
        data class Error(val message: String): PutJobApplicationState()

    }
}