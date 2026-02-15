package com.epitkane19.weatherapp.viewmodel

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epitkane19.weatherapp.BuildConfig
import kotlinx.coroutines.launch
import com.epitkane19.weatherapp.data.remote.RetrofitClient
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue



data class WeatherUiState(
    val city: String = "",
    val temperature: Double? = null,
    val description: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class WeatherViewModel : ViewModel() {

    var uiState by mutableStateOf(WeatherUiState())
        private set

    fun onSearchQueryChange(query: String) {
        uiState = uiState.copy(city = query)
    }

    fun searchWeather() {
        val city = uiState.city
        if (city.isBlank()) return

        // viewModelScope.launch käynnistää coroutinen taustasäikeessä
        viewModelScope.launch {
            try {
                uiState = uiState.copy(isLoading = true, error = null)
                val response = RetrofitClient.weatherApiService.getWeather(
                    city = city,
                    apiKey = BuildConfig.OPENWEATHER_API_KEY
                )

                if (response.isSuccessful) {
                    val body = response.body()!!
                    uiState = uiState.copy(
                        temperature = body.main.temp,
                        description = body.weather.firstOrNull()?.description,
                        isLoading = false
                    )
                } else {
                    uiState = uiState.copy(
                        error = "Kaupunkia ei löytynyt",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                uiState = uiState.copy(
                    error = "Error sään hakemisessa",
                    isLoading = false
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,                    // Nykyinen hakuteksti
    onQueryChange: (String) -> Unit,  // Kutsutaan kun teksti muuttuu
    onSearch: () -> Unit          // Kutsutaan kun käyttäjä painaa "Hae"
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            label = { Text("Kaupunki") },
            modifier = Modifier.weight(1f),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search    // Näppäimistön "Hae"-painike
            ),
            keyboardActions = KeyboardActions(
                onSearch = { onSearch() }       // Enter = hae
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onSearch) {
            Icon(Icons.Default.Search, "Search")
        }
    }
}