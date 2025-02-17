package com.example.androidproject.viewmodel.jobs

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.GetJobs
import com.example.androidproject.model.RecentJobs
import com.example.androidproject.viewmodel.jobs.paginate.GetJobsPagingSource
import com.example.androidproject.viewmodel.jobs.paginate.GetRecentJobsPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class GetRecentJobsViewModel (private val apiService: ApiService, private val context: Context): ViewModel() {
    private val _pagingSource = MutableStateFlow<GetRecentJobsPagingSource?>(null)
    val jobsPagingData: Flow<PagingData<RecentJobs>> = Pager(
        config = PagingConfig(
            pageSize = 10, // Keep it consistent with API
            initialLoadSize = 10, // Prevents large first requests
            prefetchDistance = 2, // Reduces prefetching aggressiveness
            enablePlaceholders = false // Helps prevent extra loads
        ),
        pagingSourceFactory = { GetRecentJobsPagingSource(apiService).also { _pagingSource.value = it } }
    ).flow.cachedIn(viewModelScope)

    fun invalidatePagingSource() {
        _pagingSource.value?.invalidate()
    }
}