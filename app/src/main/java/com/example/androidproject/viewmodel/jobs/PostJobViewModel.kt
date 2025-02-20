package com.example.androidproject.viewmodel.jobs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.PostJobResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostJobViewModel(private val apiService: ApiService) : ViewModel()  {
    private val _postJobState = MutableStateFlow<PostJobState>(PostJobState.Idle)
    val postJobState = _postJobState.asStateFlow()

    fun postJob(salary: Double, applicantLimitCount: Int, jobType: String, jobDescription: String, address: String, status: String, deadline: String) {
        viewModelScope.launch {
            _postJobState.value = PostJobState.Loading
            try {

            }
        }
    }

    sealed class PostJobState {
        data object Idle : PostJobState()
        data object Loading : PostJobState()
        data class Success(val message: PostJobResponse) : PostJobState()
        data class Error(val message: String) : PostJobState()
    }
}