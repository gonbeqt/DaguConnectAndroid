package com.example.androidproject.viewmodel.ratings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.client.ratingsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class ViewRatingsViewModel(private val apiService: ApiService, private val context: Context) : ViewModel() {
    private val _viewRatingsState = MutableStateFlow<ViewRatingsState>(ViewRatingsState.Idle)
    val viewRatingsState = _viewRatingsState.asStateFlow()

    fun viewRatings(tradesman_id: Int){
        viewModelScope.launch {
            _viewRatingsState.value = ViewRatingsState.Loading

            try {
                val response = apiService.getRatingsById(tradesman_id)
                val body = response.body()

                if (response.isSuccessful && body != null) {
                    _viewRatingsState.value = ViewRatingsState.Success(body)
                } else {
                    // Extract error message from JSON response body
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let {
                        // Parse JSON manually (Simple way)
                        JSONObject(it).optString("message", "An error occurred.")
                    } ?: response.message()

                    _viewRatingsState.value = ViewRatingsState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _viewRatingsState.value = ViewRatingsState.Error(e.message ?: "An error occurred.")
            }
        }
    }

    sealed class ViewRatingsState(){
        object Idle : ViewRatingsState()
        object Loading : ViewRatingsState()
        data class Success(val data: List<ratingsItem>) : ViewRatingsState()
        data class Error(val message: String) : ViewRatingsState()
    }
}