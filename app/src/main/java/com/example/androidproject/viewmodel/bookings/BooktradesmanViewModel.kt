package com.example.androidproject.viewmodel.bookings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.JsonErrorParser
import com.example.androidproject.data.preferences.TokenManager
import com.example.androidproject.model.client.BookTradesmanRequest
import com.example.androidproject.model.client.BookTradesmanResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BooktradesmanViewModel (private val apiService: ApiService): ViewModel(){
    private val _BookTradesmanState = MutableStateFlow<BookTradesmanState>(BookTradesmanState.Idle)
    val bookTradesmanState = _BookTradesmanState.asStateFlow()

    fun BookTradesman(phone_number: String, address: String, tasktype: String, taskdescription: String, booking_date: String, tradesmanId: Int) {
        viewModelScope.launch {
            _BookTradesmanState.value = BookTradesmanState.Loading
            try {
                val token = TokenManager.getToken()
                val response = apiService.booktradesman(
                    "Bearer $token",
                    BookTradesmanRequest(address, booking_date, phone_number, taskdescription, tasktype),
                    tradesmanId
                )
                if (response.isSuccessful) {
                    _BookTradesmanState.value = BookTradesmanState.Success(response.body())
                    val registerResponse = response.body()
                    if (registerResponse != null) {
                        Log.d("LoginViewModel", "Login state:: ${_BookTradesmanState.value}")
                    }
                } else {
                    val errorJson = response.errorBody()?.string()
                    val errorMessage = JsonErrorParser.extractField(errorJson, "message")
                    _BookTradesmanState.value = BookTradesmanState.Error(errorMessage)
                }
                // Handle the response here
            } catch (e: Exception) {
                // Handle the exception here
            }
        }
    }
    sealed class BookTradesmanState {
        object Idle : BookTradesmanState()
        object Loading : BookTradesmanState()
        data class Success(val data: BookTradesmanResponse?): BookTradesmanState()
        data class Error(val message: String): BookTradesmanState()
    }
}