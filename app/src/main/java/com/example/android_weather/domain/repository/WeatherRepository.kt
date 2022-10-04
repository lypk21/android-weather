package com.example.android_weather.domain.repository

import com.example.android_weather.domain.util.Resource
import com.example.android_weather.domain.weather.WeatherInfo

interface WeatherRepository {
    suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo>
}