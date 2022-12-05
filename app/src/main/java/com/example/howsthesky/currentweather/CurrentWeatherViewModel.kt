package com.example.howsthesky.currentweather

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

class CurrentWeatherViewModel(
    val database: WeatherDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val _navigateToRecentCities = MutableLiveData<Boolean>()
    val navigateToRecentCities: LiveData<Boolean>
        get() = _navigateToRecentCities

    fun onRecentCitiesButtonClicked() {
        _navigateToRecentCities.value = true
    }

    fun doneNavigatingToRecentCities() {
        _navigateToRecentCities.value = false
    }

    private val _navigateToWeatherDetails = MutableLiveData<Boolean>()
    val navigateToWeatherDetails: LiveData<Boolean>
        get() = _navigateToWeatherDetails

    fun onMoreDetailsClicked() {
        _navigateToWeatherDetails.value = true
    }

    fun doneNavigatingToWeatherDetails() {
        _navigateToWeatherDetails.value = false
    }

    private suspend fun insert(city: Weather) {
        return withContext(Dispatchers.IO) {
            database.insertCityWeather(city)
        }
    }

    val city = Weather()

    fun onCheckWeatherButtonClicked() {
        viewModelScope.launch {
            insert(city)
        }
    }

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _weatherData = MutableLiveData<WeatherProperty>()
    val weatherData: LiveData<WeatherProperty>
        get() = _weatherData

    private val _noInternetStatus = MutableLiveData<Boolean>()
    val noInternetStatus: LiveData<Boolean>
        get() = _noInternetStatus

    private val _wrongCityEntered = MutableLiveData<Boolean>()
    val wrongCityEntered: LiveData<Boolean>
        get() = _wrongCityEntered

    fun doneShowingNoInternetToast() {
        _noInternetStatus.value = false
    }

    fun doneShowingWrongCityEnteredToast() {
        _wrongCityEntered.value = false
    }

    private val noInfoErrorMessage = "HTTP 404 Not Found"

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
                    onCheckWeatherButtonClicked()
                }
            } catch (e: Exception) {
                if (e.message == noInfoErrorMessage) {
                    _wrongCityEntered.value = true
                } else {
                    _noInternetStatus.value = true
                }
            }
        }
    }

    val recentCity = database.getMostRecentCity()
}