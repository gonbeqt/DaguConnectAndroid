package com.example.androidproject

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.RetrofitInstance
import com.example.androidproject.viewmodel.factories.jobs.GetJobsViewModelFactory
import com.example.androidproject.viewmodel.jobs.GetJobsViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ViewModelSetups {
    companion object {
        fun setupGetJobsViewModel(owner: ViewModelStoreOwner): GetJobsViewModel {
            val apiService = RetrofitInstance.create(ApiService::class.java)
            val viewModelFactory = GetJobsViewModelFactory(apiService, owner as Context)
            return ViewModelProvider(owner, viewModelFactory)[GetJobsViewModel::class.java]
        }

        fun formatDateTime(dateTime: String?): String {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val outputFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy")  // Ex: "January 31, 2025"

            val parsedDate = LocalDateTime.parse(dateTime, inputFormatter)
            return parsedDate.format(outputFormatter)
        }

    }
}