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
                val response = apiService.booktradesman(
                    BookTradesmanRequest(address, booking_date, phone_number, taskdescription, tasktype),
                    tradesmanId
                )

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _BookTradesmanState.value = BookTradesmanState.Success(responseBody)
                    } else {
                        _BookTradesmanState.value = BookTradesmanState.Error("Response body is null")
                    }
                } else {
                    // Handle error response
                    val errorJson = response.errorBody()?.string()
                    val errorMessage = JsonErrorParser.extractField(errorJson, "message") ?: "Unknown error"
                    _BookTradesmanState.value = BookTradesmanState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _BookTradesmanState.value = BookTradesmanState.Error("An unexpected error occurred: ${e.message}")
            }
        }

    }

    fun resetState() {
        _BookTradesmanState.value = BookTradesmanState.Idle // Add an idle state
    }

    sealed class BookTradesmanState {
        object Idle : BookTradesmanState()
        object Loading : BookTradesmanState()
        data class Success(val data: BookTradesmanResponse?): BookTradesmanState()
        data class Error(val message: String): BookTradesmanState()
    }
}