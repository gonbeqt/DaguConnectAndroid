package com.example.androidproject.viewmodel.factories.report

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.bookings.BooktradesmanViewModel
import com.example.androidproject.viewmodel.report.ReportViewModel

class ReportViewModelFactory(private val apiService: ApiService): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReportViewModel::class.java)) {
            return ReportViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}