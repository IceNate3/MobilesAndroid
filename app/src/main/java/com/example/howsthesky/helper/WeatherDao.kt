package com.example.howsthesky.helper

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCityWeather(weather: Weather)

    @Update
    fun update(weather: Weather)

    @Query("SELECT * FROM weather_data ORDER BY city_id DESC")
    fun getAllCitiesWeather(): LiveData<List<Weather>>

    @Query("SELECT * FROM weather_data ORDER BY city_id DESC LIMIT 1")
    fun getMostRecentCity(): LiveData<Weather>

}