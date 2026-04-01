package com.nikhil.weatherapp.data.model

data class WeatherResponse(
    val main : Main,
    val weather : List<Weather>
)

data class Main(
    val temp : Double,
    val feels_like : Double,
    val temp_min : Double,
    val temp_max : Double,
)

data class Weather(
    val description : String,
    val icon : String
)
