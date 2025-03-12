package com.example.androidproject.viewmodel.Tradesman_Profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.JsonErrorParser
import com.example.androidproject.model.client.UpdateActiveStatusRequest
import com.example.androidproject.model.client.UpdateActiveStatusResponse
import com.example.androidproject.viewmodel.Tradesman_Profile.UpdateTradesmanDetailViewModel.UpdateTradesmanDetailState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UpdateTradesmanActiveStatusViewModel(private val apiService: ApiService):ViewModel() {
    private val _UpdateStatusState = MutableStateFlow<UpdateStatusState>(UpdateStatusState.Idle)
    val updateStatusState : StateFlow<UpdateStatusState> = _UpdateStatusState

    fun updateStatusState(tradesmanStatus : Boolean){
        viewModelScope.launch {
            _UpdateStatusState.value = UpdateStatusState.Loading
            val response = apiService.updateTradesmanActiveStatus(UpdateActiveStatusRequest(tradesmanStatus))
            if(response.isSuccessful){
                val body = response.body()
                if(body != null){
                    _UpdateStatusState.value = UpdateStatusState.Success(body)
                }else{
                    _UpdateStatusState.value = UpdateStatusState.Error("Response body is null")
                }
            }else{
                val errorJson = response.errorBody()?.string()
                val errorMessage = JsonErrorParser.extractField(errorJson, "message") ?: "Unknown error"
                _UpdateStatusState.value = UpdateStatusState.Error(errorMessage)
            }
        }
    }
    fun resetState(){
        _UpdateStatusState.value = UpdateStatusState.Idle
    }

    sealed class UpdateStatusState{
        object Idle : UpdateStatusState()
        object Loading : UpdateStatusState()
        data class Success(val data: UpdateActiveStatusResponse) : UpdateStatusState()
        data class Error(val message: String) : UpdateStatusState()
    }
}