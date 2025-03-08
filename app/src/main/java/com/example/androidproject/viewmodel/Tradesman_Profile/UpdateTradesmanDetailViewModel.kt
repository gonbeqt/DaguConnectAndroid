package com.example.androidproject.viewmodel.Tradesman_Profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.JsonErrorParser
import com.example.androidproject.model.client.UpdateTradesmanDetailsRequest
import com.example.androidproject.model.client.UpdateTradesmanDetailsResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UpdateTradesmanDetailViewModel(private val apiService : ApiService):ViewModel() {
    private val _updateTradesmanDetailState = MutableStateFlow<UpdateTradesmanDetailState>(UpdateTradesmanDetailState.Idle)
    val updateTradesmanDetailState : StateFlow<UpdateTradesmanDetailState> = _updateTradesmanDetailState

    fun updateTradesmanDetails(aboutMe: String,preferredWorkLocation:String,workFee: Int,phoneNumber:String) {
        _updateTradesmanDetailState.value = UpdateTradesmanDetailState.Loading
        viewModelScope.launch {
            try{
                val response = apiService.updateTradesmanDetail(UpdateTradesmanDetailsRequest(aboutMe,preferredWorkLocation,workFee,phoneNumber))
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        _updateTradesmanDetailState.value = UpdateTradesmanDetailState.Success(body)
                    }else{
                        _updateTradesmanDetailState.value = UpdateTradesmanDetailState.Error("Response body is null")
                    }
                }else{
                    val errorJson = response.errorBody()?.string()
                    println("Error response: $errorJson") // Debug log
                    val errorMessage = JsonErrorParser.extractField(errorJson, "message") ?: "Unknown error"
                    _updateTradesmanDetailState.value = UpdateTradesmanDetailState.Error(errorMessage)
                }
            }catch (e:Exception){
                _updateTradesmanDetailState.value = UpdateTradesmanDetailState.Error(e.message ?: "Unknown error")
            }
        }
    }
    fun resetState(){
        _updateTradesmanDetailState.value = UpdateTradesmanDetailState.Idle
    }


    sealed class UpdateTradesmanDetailState{
        object Loading : UpdateTradesmanDetailState()
        object Idle : UpdateTradesmanDetailState()
        data class Success(val data: UpdateTradesmanDetailsResponse): UpdateTradesmanDetailState()
        data class Error(val message: String): UpdateTradesmanDetailState()
    }
}