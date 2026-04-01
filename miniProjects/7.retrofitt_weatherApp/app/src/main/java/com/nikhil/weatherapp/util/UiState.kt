package com.nikhil.weatherapp.util

import com.nikhil.weatherapp.data.model.WeatherResponse

sealed class UiState {
    object Loading : UiState()
    data class Success(val weatherResponse : WeatherResponse) : UiState()
    data class Error(val message : String) : UiState()
}