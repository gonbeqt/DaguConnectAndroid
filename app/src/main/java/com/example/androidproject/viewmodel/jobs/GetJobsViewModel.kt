package com.example.androidproject.viewmodel.jobs

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.GetJobs
import com.example.androidproject.model.JobsResponse
import com.example.androidproject.viewmodel.jobs.paginate.GetJobsPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GetJobsViewModel (private val apiService: ApiService, private val context: Context): ViewModel(){
    val jobsPagingData: Flow<PagingData<GetJobs>> = Pager(
        config = PagingConfig(
            pageSize = 10, // Keep it consistent with API
            initialLoadSize = 10, // Prevents large first requests
            prefetchDistance = 2, // Reduces prefetching aggressiveness
            enablePlaceholders = false // Helps prevent extra loads
        ),
        pagingSourceFactory = { GetJobsPagingSource(apiService) }
    ).flow.cachedIn(viewModelScope)
}
