package com.example.howsthesky.helper

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "weather_data", indices = [Index(value = ["city_name"], unique = true)])
data class Weather(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "city_id")
    var cityId: Long = 0L,

    @ColumnInfo(name = "city_name")
    var cityName: String = "",

    @ColumnInfo(name = "temperature")
    var temperature: Double = 25.0,

    @ColumnInfo(name = "weather_description")
    var weatherDescription: String = "Thunderstorm",

    @ColumnInfo(name = "app_icon_id")
    var appIconId: String = "11d"
)