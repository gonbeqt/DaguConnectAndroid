package com.example.androidproject.viewmodel.Resumes.paginate

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.client.resumesItem

class GetResumePagingSource(private val apiService: ApiService) : PagingSource<Int, resumesItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, resumesItem> {
        return try {
            val page = params.key ?: 1
            val pageSize = params.loadSize

            Log.d("PagingDebug", "Loading page $page with size $pageSize")

            val response = apiService.getResumes(page = page, limit = pageSize)

            if (response.isSuccessful) {
                val responseBody = response.body()
                val resumes = responseBody?.resumes ?: emptyList()
                val totalPages = responseBody?.totalPages ?: 1 // Adjust based on your API response structure

                Log.d("PagingDebug", "Loaded ${resumes.size} items for page $page, total pages: $totalPages")

                LoadResult.Page(
                    data = resumes,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (page >= totalPages) null else page + 1 // Use total_pages to determine if there's more
                )
            } else {
                Log.e("PagingDebug", "Error loading page $page: ${response.code()} - ${response.message()}")
                LoadResult.Error(Exception("API error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("PagingDebug", "Exception loading page: ${e.message}")
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