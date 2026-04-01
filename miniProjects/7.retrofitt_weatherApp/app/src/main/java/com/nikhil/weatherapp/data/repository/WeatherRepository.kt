package com.nikhil.weatherapp.data.repository

import com.nikhil.weatherapp.data.model.WeatherResponse
import com.nikhil.weatherapp.data.remote.RetrofitInstance

class WeatherRepository {
    private val apikey = "1481881d83545e7b8d1c39970a0f8183"
    private val units = "metric"

    suspend fun getWeather(city : String) : WeatherResponse {
        return RetrofitInstance.api.getWeather(city = city, apikey = apikey, units = units)
    }
}