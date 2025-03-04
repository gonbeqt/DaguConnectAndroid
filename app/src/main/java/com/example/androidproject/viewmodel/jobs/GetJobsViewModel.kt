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
import com.example.androidproject.viewmodel.jobs.paginate.GetMyJobsPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class GetJobsViewModel (private val apiService: ApiService): ViewModel(){
    private val refreshTrigger = MutableStateFlow(Unit)

    val jobsPagingData: Flow<PagingData<GetJobs>> = refreshTrigger.flatMapLatest {
        Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GetJobsPagingSource(apiService) }
        ).flow.cachedIn(viewModelScope)
    }

    // Call this to force a refresh
    fun refreshJobs() {
        refreshTrigger.value = Unit
    }
}
