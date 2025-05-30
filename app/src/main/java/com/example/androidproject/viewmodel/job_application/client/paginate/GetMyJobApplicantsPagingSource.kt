package com.example.androidproject.viewmodel.job_application.client.paginate

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.JobApplicantData

class GetMyJobApplicantsPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, JobApplicantData>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, JobApplicantData> {


        return try {
            val page = params.key ?: 1
            val response = apiService.getMyJobApplicants(page = page, limit = params.loadSize)
            if (response.isSuccessful) {
                val responseBody = response.body()
                val applicants = responseBody?.applicants ?: emptyList() // Extract applications

                LoadResult.Page(
                    data = applicants,
                    prevKey = if (page == 1) null else page - 1, // No previous key if first page
                    nextKey = if (applicants.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, JobApplicantData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}

