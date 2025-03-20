package com.example.androidproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.JsonErrorParser
import com.example.androidproject.model.ReportConcernRequest
import com.example.androidproject.model.ReportConcernResponse
import com.example.androidproject.viewmodel.RegisterViewModel.RegisterState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReportConcernViewModel(private val apiService: ApiService) : ViewModel()  {
    private val _reportConcernState = MutableStateFlow<ReportConcernState>(ReportConcernState.Idle)
    val reportConcernState = _reportConcernState.asStateFlow()

    fun reportConcern(email: String, report_message: String, reportProblem: String) {
        viewModelScope.launch {
            _reportConcernState.value = ReportConcernState.Loading
            val response = apiService.reportConcern(ReportConcernRequest(email,report_message,reportProblem))
            if (response.isSuccessful) {
                _reportConcernState.value = ReportConcernState.Success(response.body())
            } else {
                val errorJson = response.errorBody()?.string()
                val errorMessage = JsonErrorParser.extractField(errorJson, "message")
                _reportConcernState.value = ReportConcernState.Error(errorMessage)
            }
        }

    }

    sealed class ReportConcernState{
        object Idle : ReportConcernState()
        object Loading : ReportConcernState()
        data class Success(val data: ReportConcernResponse?) : ReportConcernState()
        data class Error(val message: String) : ReportConcernState()

    }
}
