package com.example.androidproject.viewmodel.notifications
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClearNotificationViewModel(private val apiService: ApiService) : ViewModel() {
    private val _clearNotificationResult = MutableStateFlow<Boolean?>(null)
    val clearNotificationResult: StateFlow<Boolean?> = _clearNotificationResult.asStateFlow()

    fun clearNotification(jobId: Int){
        viewModelScope.launch {
            try {
                val response = apiService.deleteJob(jobId)
                _clearNotificationResult.value = response.isSuccessful
            } catch (e: Exception){
                _clearNotificationResult.value = false
                Log.e("ClearNotificationViewModel", "Error clearing notification", e)
            }
        }
    }
}

