package com.example.howsthesky.recentcities

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.howsthesky.helper.WeatherDao

class RecentCitiesViewModelFactory(
    private val dataSource: WeatherDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecentCitiesViewModel::class.java)) {
            return RecentCitiesViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}