package com.epitkane19.weatherapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.epitkane19.weatherapp.viewmodel.SearchBar
import com.epitkane19.weatherapp.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {
    val state = viewModel.uiState

    Column(Modifier.fillMaxSize()) {

        SearchBar(
            query = state.city,
            onQueryChange = { viewModel.onSearchQueryChange(it) },
            onSearch = { viewModel.searchWeather() }
        )

        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            state.error != null -> {
                ErrorScreen(
                    message = state.error,
                )
            }
            state.temperature != null -> {
                WeatherContent(
                    temperature = state.temperature,
                    description = state.description ?: ""
                )
            }
        }
    }
}

@Composable
fun ErrorScreen(message: String) {
    Text(message)
}

@Composable
fun WeatherContent(temperature: Double, description: String) {
    Text("Lämpötila: $temperature °C")
    Text("Säätila: $description")
}

