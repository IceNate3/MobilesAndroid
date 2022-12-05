package com.example.howsthesky.recentcities

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.howsthesky.formatTextToCapitalize
import com.example.howsthesky.helper.Weather
import com.example.howsthesky.helper.WeatherDao
import com.example.howsthesky.network.WeatherApi
import com.example.howsthesky.network.WeatherProperty
import kotlinx.coroutines.*

class RecentCitiesViewModel(
    val database: WeatherDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    val cities = database.getAllCitiesWeather()

    private val _navigateToCurrentWeather = MutableLiveData<Boolean>()
    val navigateToCurrentWeather: LiveData<Boolean>
        get() = _navigateToCurrentWeather

    fun onCurrentWeatherButtonClicked() {
        _navigateToCurrentWeather.value = true
    }

    fun doneNavigating() {
        _navigateToCurrentWeather.value = false
    }

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private suspend fun insert(city: Weather) {
        return withContext(Dispatchers.IO) {
            database.insertCityWeather(city)
        }
    }

    val city = Weather()

    fun onCheckRecentCityClicked() {
        viewModelScope.launch {
            insert(city)
        }
    }

    private val _weatherData = MutableLiveData<WeatherProperty>()
    val weatherData: LiveData<WeatherProperty>
        get() = _weatherData

    private val _noInternetStatus = MutableLiveData<Boolean>()
    val noInternetStatus: LiveData<Boolean>
        get() = _noInternetStatus

    fun getWeatherDetail(cityName: String) {
        coroutineScope.launch {
            var getWeatherDataDeferred = WeatherApi.retrofitService.getWeather(cityName)
            try {
                val weatherResult = getWeatherDataDeferred.await()
                weatherResult.let {
                    city.cityName = formatTextToCapitalize(it.cityName)
                    city.temperature = it.mainWeatherData.temp
                    city.weatherDescription =
                        formatTextToCapitalize(it.weatherDescList[0].description)
                    city.appIconId = it.weatherDescList[0].iconId
                    _weatherData.value = it
                    onCheckRecentCityClicked()
                    _navigateToCurrentWeather.value = true
                }
            } catch (e: Exception) {
                _noInternetStatus.value = true
            }
        }
    }

    fun doneShowingNoInternetToast() {
        _noInternetStatus.value = false
    }
}