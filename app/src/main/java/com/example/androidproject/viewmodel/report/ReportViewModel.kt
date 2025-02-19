package com.example.androidproject.viewmodel.report

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.JsonErrorParser
import com.example.androidproject.data.preferences.TokenManager
import com.example.androidproject.model.client.ReportRequest
import com.example.androidproject.model.client.ReportResponse
import com.example.androidproject.viewmodel.bookings.BooktradesmanViewModel.BookTradesmanState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReportViewModel (private val apiService: ApiService): ViewModel() {
    private val _reportState = MutableStateFlow<ReportState>(ReportState.Idle)
    val reportState = _reportState.asStateFlow()

    fun report(report_reason: String, report_details: String, tradesmanId: Int) {
        viewModelScope.launch {
            _reportState.value = ReportState.Loading
            try{
                val token = TokenManager.getToken()
                val response = apiService.report(
                    "Bearer $token",
                    ReportRequest(report_reason, report_details),
                    tradesmanId
                )
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null){
                        _reportState.value = ReportState.Success(responseBody)
                    }else{
                        _reportState.value = ReportState.Error("Response body is null")
                    }
                }else{
                    val errorJson = response.errorBody()?.string()
                    val errorMessage = JsonErrorParser.extractField(errorJson, "message") ?: "Unknown error"
                    errorJson?.let { Log.d("tester", it) }
                    Log.d("tester", errorMessage)
                    _reportState.value = ReportState.Error(errorMessage)
                }

            }catch (e: Exception){
                Log.e("ReportViewModel", "Exception: ${e.message}", e)
                _reportState.value = ReportState.Error("An unexpected error occurred: ${e.message}")
            }
        }
    }
    fun resetState() {
        _reportState.value = ReportState.Idle // Add an idle state
    }




    sealed class ReportState {
        object Idle : ReportState()
        object Loading : ReportState()
        data class Success(val data: ReportResponse?): ReportState()
        data class Error(val message: String): ReportState()
    }
}