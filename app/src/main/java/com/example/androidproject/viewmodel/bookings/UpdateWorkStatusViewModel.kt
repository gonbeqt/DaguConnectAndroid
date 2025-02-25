package com.example.androidproject.viewmodel.bookings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.JsonErrorParser
import com.example.androidproject.model.client.workstatusRequest
import com.example.androidproject.model.client.workstatusResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UpdateWorkStatusViewModel(private val apiService: ApiService,context : Context): ViewModel() {
    private val _workStatusState = MutableStateFlow<UpdateWorkStatus>(UpdateWorkStatus.Idle)
    val workStatusState = _workStatusState.asStateFlow()

    fun updateWorkStatus( work_status : String,cancel_reason :String ,bookingId : Int){
        viewModelScope.launch {
            _workStatusState.value = UpdateWorkStatus.Loading
            try{
                val response = apiService.updateworkStatus(workstatusRequest(work_status,cancel_reason),bookingId)
                if(response.isSuccessful){
                    val body = response.body()
                    if(body!=null){
                        _workStatusState.value = UpdateWorkStatus.Success(body)
                    }else{
                        _workStatusState.value = UpdateWorkStatus.Error("No data received from the server")
                    }
                }else {
                    // Handle error response
                    val errorJson = response.errorBody()?.string()
                    val errorMessage = JsonErrorParser.extractField(errorJson, "message") ?: "Unknown error"
                    _workStatusState.value = UpdateWorkStatus.Error(errorMessage)
                }
            }catch (e: Exception){
                _workStatusState.value = UpdateWorkStatus.Error("An unexpected error occurred: ${e.message}")
            }
        }



    }

    fun resetState() {
        _workStatusState.value = UpdateWorkStatus.Idle // Add an idle state
    }

    sealed class UpdateWorkStatus{
        object Idle : UpdateWorkStatus()
        object Loading : UpdateWorkStatus()
        data class Success(val data: workstatusResponse?): UpdateWorkStatus()
        data class Error(val message: String): UpdateWorkStatus()
    }
}