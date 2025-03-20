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
import com.example.androidproject.viewmodel.jobs.paginate.GetJobsPagingSource
import com.example.androidproject.viewmodel.jobs.paginate.GetRecentJobsPagingSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class GetRecentJobsViewModel (private val apiService: ApiService, private val context: Context): ViewModel() {
    private val refreshTrigger = MutableStateFlow(Unit)

    @OptIn(ExperimentalCoroutinesApi::class)
    val jobsPagingData: Flow<PagingData<GetJobs>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GetRecentJobsPagingSource(apiService) }
        ).flow.cachedIn(viewModelScope)


    fun refreshJobs() {
        refreshTrigger.value = Unit
    }
}