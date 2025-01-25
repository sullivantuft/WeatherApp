package org.example

fun main() {
    //Initializes objects for the Geocoder and WeatherAPI
    val geocoder = Geocoder()
    val api = WeatherAPI()


    println("Input a city")
    val cityName = readlnOrNull().toString()
    val city = geocoder.getCoordinates(cityName)    //Takes the city as a keyboard input and calls the geocoder to return the coordinates of the city

    if (city != null) {
        val cityWeather = api.retrieveWeatherData(city.latitude, city.longitude) //If the city exists, it will retrieve the weather data for the city

        if (cityWeather != null) {
            api.printWeatherData(cityWeather)
        }
        else{
            println("Weather not found")
        }
    } else {
        println("Failed to retrieve coordinates for $cityName.")
    }
}
