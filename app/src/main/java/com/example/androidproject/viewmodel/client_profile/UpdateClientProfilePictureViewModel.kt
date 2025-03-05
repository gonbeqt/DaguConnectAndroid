package com.example.androidproject.viewmodel.client_profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.UpdateProfilePicture
import com.example.androidproject.model.UpdateProfilePictureResponse
import com.example.androidproject.model.UpdateStatus
import com.example.androidproject.model.UpdateStatusResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class UpdateClientProfilePictureViewModel(private val apiService: ApiService): ViewModel() {
    private val _updateClientProfileState = MutableStateFlow<UpdateClientProfilePictureState>(
        UpdateClientProfilePictureState.Idle)
    val updateClientProfileState: StateFlow<UpdateClientProfilePictureState> = _updateClientProfileState

    fun updateClientProfile(profilePicture: String) {
        viewModelScope.launch {
            _updateClientProfileState.value = UpdateClientProfilePictureState.Loading
            val data  = UpdateProfilePicture(profilePicture)
            val put = apiService.updateClientProfilePicture(data)
            try {
                if (put.isSuccessful) {
                    _updateClientProfileState.value = UpdateClientProfilePictureState.Success(put)
                } else {
                    _updateClientProfileState.value = UpdateClientProfilePictureState.Error(put.message())
                }
            } catch (e: Exception) {
                _updateClientProfileState.value = UpdateClientProfilePictureState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun resetState() {
        _updateClientProfileState.value = UpdateClientProfilePictureState.Idle
    }

    sealed class UpdateClientProfilePictureState{
        data object Idle: UpdateClientProfilePictureState()
        data object Loading: UpdateClientProfilePictureState()
        data class Success(val data: Response<UpdateProfilePictureResponse>): UpdateClientProfilePictureState()
        data class Error(val message: String): UpdateClientProfilePictureState()

    }
}