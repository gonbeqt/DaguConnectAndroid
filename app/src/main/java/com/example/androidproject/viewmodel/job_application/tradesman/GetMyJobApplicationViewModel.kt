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
import com.example.androidproject.viewmodel.factories.job_application.tradesman.GetMyJobApplicationViewModelFactory
import com.example.androidproject.viewmodel.job_application.client.paginate.GetMyJobApplicantsPagingSource
import com.example.androidproject.viewmodel.job_application.client.paginate.GetMyJobApplicationPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class GetMyJobApplicationViewModel(private val apiService: ApiService, private val context: Context) : ViewModel() {
    private val refreshTrigger = MutableStateFlow(Unit)

    val jobApplicantsPagingData: Flow<PagingData<JobApplicationData>> = refreshTrigger.flatMapLatest {
        Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GetMyJobApplicationPagingSource(apiService) }
        ).flow.cachedIn(viewModelScope)
    }

    // Call this to force a refresh
    fun refreshJobApplicants() {
        refreshTrigger.value = Unit
    }
}
