package com.nikhil.weatherapp.data.remote

import com.nikhil.weatherapp.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/weather")
    suspend fun getWeather(

        @Query("q") city : String,

        @Query("appid") apikey : String,

        @Query("units") units : String

    ): WeatherResponse
}