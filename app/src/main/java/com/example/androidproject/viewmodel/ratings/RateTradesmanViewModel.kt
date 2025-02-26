package com.example.androidproject.viewmodel.ratings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.JsonErrorParser
import com.example.androidproject.model.client.rateTradesmanRequest
import com.example.androidproject.model.client.rateTradesmanResponse
import com.example.androidproject.viewmodel.bookings.BooktradesmanViewModel.BookTradesmanState
import com.example.androidproject.viewmodel.bookings.UpdateWorkStatusViewModel.UpdateWorkStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RateTradesmanViewModel(private val apiService: ApiService, context : Context):ViewModel() {
    private val _rateTradesmanState = MutableStateFlow<RateTradesman>(RateTradesman.Idle)
    val rateTradesmanState = _rateTradesmanState.asStateFlow()

    fun rateTradesman(message : String, rating : Int, tradesmanId : Int){
        viewModelScope.launch {
            _rateTradesmanState.value = RateTradesman.Loading
            try{
                val response = apiService.ratetradesman(rateTradesmanRequest(message,rating),tradesmanId)
                if(response.isSuccessful){
                    val body = response.body()
                    if(body!=null){
                        _rateTradesmanState.value = RateTradesman.Success(body)
                    }else{
                        _rateTradesmanState.value = RateTradesman.Error("No data received from the server")
                    }
                }else {
                    // Handle error response
                    val errorJson = response.errorBody()?.string()
                    val errorMessage = JsonErrorParser.extractField(errorJson, "message") ?: "Unknown error"
                    _rateTradesmanState.value = RateTradesman.Error(errorMessage)

                }
            }catch (e: Exception){
                _rateTradesmanState.value = RateTradesman.Error("An unexpected error occurred: ${e.message}")
            }

        }  // End of coroutine scope launching
    }


    fun resetState(){
        _rateTradesmanState.value = RateTradesman.Idle
    }

    sealed class RateTradesman{
        object Idle : RateTradesman()
        object Loading : RateTradesman()
        data class Success(val data: rateTradesmanResponse?): RateTradesman()
        data class Error(val message: String): RateTradesman()

    }
}