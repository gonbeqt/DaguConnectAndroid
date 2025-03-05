package com.example.androidproject

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.RetrofitInstance
import com.example.androidproject.viewmodel.factories.jobs.GetJobsViewModelFactory
import com.example.androidproject.viewmodel.jobs.GetJobsViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class ViewModelSetups {
    companion object {
        fun setupGetJobsViewModel(owner: ViewModelStoreOwner): GetJobsViewModel {
            val apiService = RetrofitInstance.create(ApiService::class.java)
            val viewModelFactory = GetJobsViewModelFactory(apiService)
            return ViewModelProvider(owner, viewModelFactory)[GetJobsViewModel::class.java]
        }

        fun formatDateTime(dateTime: String?): String {
            if (dateTime.isNullOrBlank()) return "Invalid Date"

            val outputFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy")  // Ex: "January 31, 2025"

            return try {
                // Try parsing as full datetime first
                val inputFormatterWithTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val parsedDateTime = LocalDateTime.parse(dateTime, inputFormatterWithTime)
                parsedDateTime.format(outputFormatter)
            } catch (e: DateTimeParseException) {
                try {
                    // If that fails, try parsing just the date
                    val inputFormatterDateOnly = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val parsedDate = LocalDate.parse(dateTime, inputFormatterDateOnly)
                    parsedDate.format(outputFormatter)
                } catch (e: DateTimeParseException) {
                    "Invalid Date"
                }
            }
        }

        fun isToday(createdAt: String, pattern: String = "yyyy-MM-dd HH:mm:ss"): Boolean {
            return try {
                val formatter = DateTimeFormatter.ofPattern(pattern)
                val date = LocalDate.parse(createdAt.substring(0, 10), DateTimeFormatter.ISO_DATE) // Extracts "yyyy-MM-dd"
                date == LocalDate.now()
            } catch (e: Exception) {
                false // Handle parsing errors
            }
        }

        fun isNotToday(createdAt: String, pattern: String = "yyyy-MM-dd HH:mm:ss"): Boolean {
            return try {
                val formatter = DateTimeFormatter.ofPattern(pattern)
                val date = LocalDate.parse(createdAt.substring(0, 10), DateTimeFormatter.ISO_DATE) // Extract "yyyy-MM-dd"
                date.isBefore(LocalDate.now()) // Checks if the date is before today
            } catch (e: Exception) {
                false // Handle parsing errors
            }
        }
    }
}