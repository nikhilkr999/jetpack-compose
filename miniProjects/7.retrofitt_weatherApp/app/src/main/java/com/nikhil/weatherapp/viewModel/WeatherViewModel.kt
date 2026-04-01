package com.nikhil.weatherapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhil.weatherapp.data.repository.WeatherRepository
import com.nikhil.weatherapp.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val repository = WeatherRepository()

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState : StateFlow<UiState> = _uiState

    fun getWeather(city : String){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = repository.getWeather(city)
                _uiState.value = UiState.Success(response)
            }catch (e : Exception){
                _uiState.value = UiState.Error("fail to load weather")
            }
        }
    }
}