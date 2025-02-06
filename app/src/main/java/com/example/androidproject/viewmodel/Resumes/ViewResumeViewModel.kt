package com.example.androidproject.viewmodel.Resumes

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.data.preferences.TokenManager
import com.example.androidproject.model.client.resumesItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import viewResume

class ViewResumeViewModel (private val apiService: ApiService, private val context: Context) : ViewModel(){
    private val  _viewResumeState = MutableStateFlow<ViewResumeState>(ViewResumeState.Idle)
    val viewResumeState = _viewResumeState.asStateFlow()

    fun viewResume(id : Int){
        viewModelScope.launch{
            _viewResumeState.value = ViewResumeState.Loading
            try{
                val token = TokenManager.getToken()

                val response = apiService.getResumeById("Bearer $token",id)
                val body = response.body()
                if (response.isSuccessful && body != null){
                    _viewResumeState.value = ViewResumeState.Success(body)
                }else{
                    _viewResumeState.value = ViewResumeState.Error(response.message())
                }

            }catch (e:Exception){
                _viewResumeState.value = ViewResumeState.Error(e.message ?: "An error occurred.")
            }
        }
    }

    sealed class  ViewResumeState(){
        object Idle : ViewResumeState()
        object Loading : ViewResumeState()
        data class Success(val data: viewResume) : ViewResumeState()
        data class Error(val message: String) : ViewResumeState()

    }
}