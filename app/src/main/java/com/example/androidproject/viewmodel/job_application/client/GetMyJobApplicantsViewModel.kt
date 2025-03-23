package com.example.androidproject.viewmodel.job_application.client

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.JobApplicantData
import com.example.androidproject.model.JobApplicationData
import com.example.androidproject.viewmodel.job_application.client.paginate.GetMyJobApplicantsPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class GetMyJobApplicantsViewModel(private val apiService: ApiService, private val context: Context) : ViewModel() {

    private val _pagingSource = MutableStateFlow<GetMyJobApplicantsPagingSource?>(null)

    val jobApplicantsPagingData: Flow<PagingData<JobApplicantData>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                GetMyJobApplicantsPagingSource(apiService).also { _pagingSource.value = it }
            }
        ).flow.cachedIn(viewModelScope)




}
