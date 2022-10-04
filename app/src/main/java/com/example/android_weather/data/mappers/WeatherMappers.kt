package com.example.android_weather.data.mappers

import com.example.android_weather.data.remote.WeatherDataDto
import com.example.android_weather.data.remote.WeatherDto
import com.example.android_weather.domain.weather.WeatherData
import com.example.android_weather.domain.weather.WeatherInfo
import com.example.android_weather.domain.weather.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private class IndexedWeatherData(
    val index: Int,
    val data: WeatherData
)

fun WeatherDataDto.toWeatherDataMap(): Map<Int, List<WeatherData>> {
    return time.mapIndexed { index, time ->
        IndexedWeatherData(
            index,
            data = WeatherData(
                time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
                temperatureCelsius = temperature[index],
                pressure = pressure[index],
                windSpeed = windSpeed[index],
                humidity = humidity[index],
                weatherType = WeatherType.fromWMO(weatherCodes[index])
            )
        )
    }.groupBy {
        it.index / 24
    }.mapValues {
        it.value.map {
            it.data
        }
    }
}

fun WeatherDto.toWeatherInfo(): WeatherInfo {
    val weatherDataMap = weatherData.toWeatherDataMap()
    val now = LocalDateTime.now()
    val currentWeatherData = weatherDataMap[0]?.find {
        val hour = if(now.minute < 30) now.hour else now.hour + 1
        it.time.hour == hour
    }
    return WeatherInfo(
        weatherDataPerDay = weatherDataMap,
        currentWeatherData =  currentWeatherData
    )
}