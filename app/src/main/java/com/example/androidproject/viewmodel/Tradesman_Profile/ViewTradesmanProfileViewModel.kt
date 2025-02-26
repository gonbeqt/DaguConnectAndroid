package com.example.androidproject.viewmodel.Tradesman_Profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.client.resumesItem
import com.example.androidproject.viewmodel.Resumes.ViewResumeViewModel.ViewResumeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import viewResume

class ViewTradesmanProfileViewModel(private val ApiService: ApiService, private val context: Context) : ViewModel() {
    val _viewTradesmanProfileResumeState = MutableStateFlow<ViewTradesmanProfileState>(ViewTradesmanProfileState.Idle)
    val viewTradesmanProfileResumeState = _viewTradesmanProfileResumeState.asStateFlow()

    fun viewTradesmanProfile(){
        viewModelScope.launch{
            try {
                val response = ApiService.getTradesmanResume()
                if (response.isSuccessful){
                    val body = response.body()
                    if (body != null){
                        _viewTradesmanProfileResumeState.value = ViewTradesmanProfileState.Success(body)
                    }else{
                        _viewTradesmanProfileResumeState.value = ViewTradesmanProfileState.Error(response.message())
                    }
                }
            }catch (e: Exception){
                _viewTradesmanProfileResumeState.value = ViewTradesmanProfileState.Error(e.message ?: "An error occurred.")
            }

        }
    }


    sealed class ViewTradesmanProfileState{
        object Idle : ViewTradesmanProfileState()
        object Loading : ViewTradesmanProfileState()
        data class Success(val data: viewResume) : ViewTradesmanProfileState()
        data class Error(val message: String) : ViewTradesmanProfileState()


    }
}