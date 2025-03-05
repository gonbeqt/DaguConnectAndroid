package com.example.androidproject

import android.content.Context
import android.util.Log
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

        fun getDateOnly(datetimeStr: String?): String? {
            // Return null if input is null or "null" string
            if (datetimeStr.isNullOrBlank() || datetimeStr == "null") return null

            return try {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val dateTime = LocalDateTime.parse(datetimeStr, formatter)
                dateTime.toLocalDate().toString()
            } catch (e: DateTimeParseException) {
                // Handle invalid date format - return null or a default value
                null
            }
        }

        fun isToday(createdAt: String, pattern: String = "yyyy-MM-dd HH:mm:ss"): Boolean {
            return try {
                Log.d("NotificationScreenView", "Raw createdAt: $createdAt")
                val date = LocalDate.parse(createdAt.substring(0, 10), DateTimeFormatter.ISO_DATE)
                val today = LocalDate.now()
                Log.d("NotificationScreenView", "Parsed date: $date, Today's date: $today")
                date == today
            } catch (e: Exception) {
                Log.e("NotificationScreenView", "Error parsing date $createdAt: ${e.message}")
                false
            }
        }

        fun isNotToday(createdAt: String, pattern: String = "yyyy-MM-dd HH:mm:ss"): Boolean {
            return try {
                val date = LocalDate.parse(createdAt.substring(0, 10), DateTimeFormatter.ISO_DATE)
                val today = LocalDate.now()
                Log.d("NotificationScreenView", "Checking if $date is before $today")
                date.isBefore(today)
            } catch (e: Exception) {
                Log.e("NotificationScreenView", "Error parsing date $createdAt: ${e.message}")
                false
            }
        }
    }
}