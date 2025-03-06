package com.example.androidproject.viewmodel.Resumes

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.GetJobs
import com.example.androidproject.model.client.resumesItem
import com.example.androidproject.viewmodel.Resumes.paginate.GetResumePagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class GetResumesViewModel(private val apiService: ApiService) : ViewModel() {
    private val _pagingSource = MutableStateFlow<GetResumePagingSource?>(null)
    private val _dismissedResumes = mutableStateOf(setOf<Int>())
    val dismissedResumes: State<Set<Int>> = _dismissedResumes
    private val refreshTrigger = MutableStateFlow(Unit)
/*
    val resumePagingData: Flow<PagingData<resumesItem>> = Pager(

        config = PagingConfig(
            pageSize = 10,
            initialLoadSize = 10,
            prefetchDistance = 2,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            GetResumePagingSource(apiService).also { _pagingSource.value = it }
        }
    ).flow.cachedIn(viewModelScope)*/

    val resumePagingData:Flow<PagingData<resumesItem>> = refreshTrigger.flatMapLatest {
        Pager(

            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GetResumePagingSource(apiService)}
        ).flow.cachedIn(viewModelScope)
    }

    // Call this to force a refresh
    fun refreshResumes() {
        refreshTrigger.value = Unit
    }

    // Function to invalidate the PagingSource
    fun invalidatePagingSource() {
        _pagingSource.value?.invalidate()
    }

    fun dismissResume(resumeId: Int) {
        _dismissedResumes.value = _dismissedResumes.value + resumeId
        invalidatePagingSource() // Force reload after dismissal
    }

}