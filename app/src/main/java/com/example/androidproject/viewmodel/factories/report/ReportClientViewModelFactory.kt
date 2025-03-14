package com.example.androidproject.viewmodel.factories.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.report.ReportClientViewModel


class ReportClientViewModelFactory(private val apiService: ApiService): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReportClientViewModel::class.java)) {
            return ReportClientViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}