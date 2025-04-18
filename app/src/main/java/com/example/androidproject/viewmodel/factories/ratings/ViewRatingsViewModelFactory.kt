package com.example.androidproject.viewmodel.factories.ratings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.ratings.ViewRatingsViewModel


class ViewRatingsViewModelFactory(private val apiService: ApiService, private val context : Context) : ViewModelProvider.Factory  {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewRatingsViewModel::class.java)) {
            return ViewRatingsViewModel(apiService,context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}