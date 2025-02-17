package com.example.androidproject.viewmodel.Resumes.paginate

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.GetJobs
import com.example.androidproject.model.client.resumesItem

class GetResumePagingSource(private val apiService: ApiService) : PagingSource<Int, resumesItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, resumesItem> {
        return try {
            val page = params.key ?: 1
            Log.d("PagingSource", "Loading page $page")
            val response = apiService.getResumes(page, params.loadSize)
            val resumes = response.body()?.resumes ?: emptyList()
            Log.d("PagingSource", "Loaded ${resumes.size} items")
            LoadResult.Page(
                data = resumes,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (resumes.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Log.e("PagingSource", "Error loading data", e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, resumesItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}


