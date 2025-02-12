package com.example.androidproject.viewmodel.Resumes

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch

class GetResumesViewModel(private val apiService: ApiService, private val context: Context) : ViewModel() {
    private val _pagingSource = MutableStateFlow<GetResumePagingSource?>(null)
    private val _dismissedResumes = mutableStateOf(setOf<Int>())
    val dismissedResumes: State<Set<Int>> = _dismissedResumes

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
    ).flow.cachedIn(viewModelScope)

        fun dismissResume(resumeId: Int) {
            _dismissedResumes.value = _dismissedResumes.value + resumeId
        }

    // Function to invalidate the PagingSource
    fun invalidatePagingSource() {
        _pagingSource.value?.invalidate()
    }

}