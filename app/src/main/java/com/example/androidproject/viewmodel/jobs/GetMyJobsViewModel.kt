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
import com.example.androidproject.viewmodel.jobs.paginate.GetMyJobsPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class GetMyJobsViewModel(private val apiService: ApiService, private val context: Context) : ViewModel() {


    private var currentPagingSource: GetMyJobsPagingSource? = null

    val jobsPagingData: Flow<PagingData<GetJobs>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GetMyJobsPagingSource(apiService) }
        ).flow.cachedIn(viewModelScope)


}
