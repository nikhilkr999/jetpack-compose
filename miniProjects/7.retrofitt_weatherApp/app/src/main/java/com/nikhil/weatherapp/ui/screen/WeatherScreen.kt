package com.nikhil.weatherapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nikhil.weatherapp.util.UiState
import com.nikhil.weatherapp.viewModel.WeatherViewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel, modifier: Modifier = Modifier) {

    var city by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Enter City") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { viewModel.getWeather(city) }
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(20.dp))

        when (uiState) {
            is UiState.Loading -> {
                CircularProgressIndicator()
            }

            is UiState.Success -> {
                val data = (uiState as UiState.Success).weatherResponse
                val icon = data.weather[0].icon
                val imageUrl = "https://openweathermap.org/img/wn/${icon}@2x.png"

                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(100.dp)
                )

                Text(text = "Temperature: ${data.main.temp}")
                Text(text = "Temperature min: ${data.main.temp_min}")
                Text(text = "Temperature max: ${data.main.temp_max}")
                Text(text = "Description: ${data.weather[0].description}")
            }

            is UiState.Error -> {
                Text(text = (uiState as UiState.Error).message)
            }
        }
    }
}
