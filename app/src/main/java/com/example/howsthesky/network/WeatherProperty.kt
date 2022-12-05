package com.example.howsthesky.network

import com.squareup.moshi.Json

data class WeatherProperty(
    @Json(name = "clouds") val cloudInfo: Clouds,
    @Json(name = "main") val mainWeatherData: Main,
    @Json(name = "name") val cityName: String,
    @Json(name = "visibility") val visibility: Int,
    @Json(name = "weather") val weatherDescList: List<Weather>,
    @Json(name = "wind") val wind: Wind
)

data class Main(
    @Json(name = "feels_like") val feelsLike: Double,
    @Json(name = "humidity") val humidity: Int,
    @Json(name = "temp") val temp: Double,
    @Json(name = "temp_max") val tempMax: Double,
    @Json(name = "temp_min") val tempMin: Double
)

data class Weather(
    @Json(name = "description") val description: String,
    @Json(name = "icon") val iconId: String
)

data class Clouds(
    @Json(name = "all") val cloudiness: Int
)

data class Wind(
    @Json(name = "speed") val speed: Double
)