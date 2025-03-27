package com.example.androidproject.viewmodel.job_application.tradesman

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.JobApplicationData
import com.example.androidproject.viewmodel.job_application.tradesman.paginate.GetMyJobApplicationPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class GetMyJobApplicationViewModel(private val apiService: ApiService, private val context: Context) : ViewModel() {

    private val _pagingSource = MutableStateFlow<GetMyJobApplicationPagingSource?>(null)

    val jobApplicationPagingData: Flow<PagingData<JobApplicationData>> =
        Pager(
            config = PagingConfig(
                pageSize = 50,
                initialLoadSize = 50,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                GetMyJobApplicationPagingSource(apiService).also { _pagingSource.value = it }
            }
        ).flow.cachedIn(viewModelScope)
    }





