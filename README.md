# WeatherApp
Viikko 5

Mitä Retrofit tekee
- Se on kirjasto mikä yksinkertaistaa HTTP requestien tekemisen Androidilla. Se hoitaa requestin, sen lähettämisen serverille ja responsen muuttamisen Kotlin olioiksi.

Miten JSON muutetaan dataluokiksi
- Retrofit käyttää Gson converteria millä muutetaan JSON response Kotlin dataluokiksi.

Miten coroutines toimii tässä
- API-kutsu tehdään taustasäikeessä jotta se ei estä main UI threadia. ViewModel sitten päivittää UI-tilan kun data on tullut perille.

Miten UI-tila toimii
- ViewModelissa on WeatherUiState class mikä sisältää datan liittyen UI:seen:

```kotlin
data class WeatherUiState(
    val city: String = "",
    val temperature: Double? = null,
    val description: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
```
Miten API-key on tallennettu
- API-key on tallennettu local.properties tiedostoon josta se siirtyy sitten generoituun BuildConfig tiedostoon mistä Retrofit löytää avaimen.
