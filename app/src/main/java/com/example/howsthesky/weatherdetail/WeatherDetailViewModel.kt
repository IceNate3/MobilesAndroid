package com.example.howsthesky.weatherdetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.howsthesky.network.WeatherApi
import com.example.howsthesky.network.WeatherProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class WeatherDetailViewModel(
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val _navigateToCurrentWeather = MutableLiveData<Boolean>()
    val navigateToCurrentWeather: LiveData<Boolean>
        get() = _navigateToCurrentWeather

    fun onCurrentWeatherButtonClicked() {
        _navigateToCurrentWeather.value = true
    }

    fun doneNavigating() {
        _navigateToCurrentWeather.value = false
    }

    private val _weatherData = MutableLiveData<WeatherProperty>()
    val weatherData: LiveData<WeatherProperty>
        get() = _weatherData

    private val _noInternetStatus = MutableLiveData<Boolean>()
    val noInternetStatus: LiveData<Boolean>
        get() = _noInternetStatus

    fun doneShowingNoInternetToast() {
        _noInternetStatus.value = false
    }

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun getCompleteWeatherDetail(cityName: String) {
        coroutineScope.launch{
            var getWeatherDataDeferred = WeatherApi.retrofitService.getWeather(cityName)
            try {
                val weatherResult = getWeatherDataDeferred.await()
                weatherResult.let {
                    _weatherData.value = it
                }
            } catch (e: Exception) {
                    _noInternetStatus.value = true
            }
        }
    }
}