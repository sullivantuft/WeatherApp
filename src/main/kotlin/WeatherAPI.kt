package org.example

import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class WeatherAPI {
    //This function is responsible for retrieving all weather data from the Open-Meteo API.
    // It returns a WeatherData object which contains current as well as daily weather information
    fun retrieveWeatherData(latitude: Double, longitude: Double): WeatherData? {
        val request = apiRequests()                     //Initializes the API request object
        val url = buildURL(latitude, longitude)         //Builds a URL for the request.
        val jsonResponse = request.sendRequest(url)     //Sends the request.

        return if (jsonResponse != null) {
            parseWeatherData(jsonResponse)              //Parses the data if it receives a valid response.
        } else {
            null
        }
    }
    //This function is responsible for parsing the weather data from the JSON response.
    //It extracts the current and daily weather information.
    private fun parseWeatherData(jsonResponse: String): WeatherData {
        val jsonObject = JSONObject(jsonResponse)
        //Parses the current section
        val currentWeather = parseCurrentWeather(jsonObject.getJSONObject("current"))
        //Uses the key Daily to get  the daily weather information and daily_units to set the units such as Fahrenheit and mph.
        //Parses the Daily weather.
        val dailyWeather = parseDailyWeather(jsonObject.getJSONObject("daily")
            , jsonObject.getJSONObject("daily_units"))

        return WeatherData(currentWeather, dailyWeather)

    }
    //This function is responsible for parsing the current weather.
    // It takes in the current JSON object and returns a CurrentWeather object containing the
    // temperature, precipitation, and wind speed.

    private fun parseCurrentWeather(current: JSONObject): CurrentWeather {

        val temperature = current.getDouble("temperature_2m")
        val precipitation = current.getDouble("precipitation")
        val windSpeed = current.getDouble("wind_speed_10m")

        return CurrentWeather(temperature, precipitation, windSpeed)
    }
    //This function is responsible for parsing the daily weather. It takes in the daily weather and units
    //as an input and returns a list of Daily Weather objects. This object is the forecast for the next 5 days

    private fun parseDailyWeather(daily: JSONObject, dailyUnits: JSONObject): List<DailyWeather> {
        val dailyWeatherList = mutableListOf<DailyWeather>()

        val dates = daily.getJSONArray("time")
        val maxTemps = daily.getJSONArray("temperature_2m_max")
        val minTemps = daily.getJSONArray("temperature_2m_min")
        val sunrises = daily.getJSONArray("sunrise")
        val sunsets = daily.getJSONArray("sunset")

        //Creates a DailyWeather object for each day and adds it to the list
        for (i in 0 until dates.length()){
            dailyWeatherList.add(DailyWeather(
                date = dates.getString(i),
                maxTemp = maxTemps.getDouble(i),
                minTemp = minTemps.getDouble(i),
                sunrise = sunrises.getString(i),
                sunset = sunsets.getString(i)
            ))
        }

        return dailyWeatherList
    }

    //This function is responsible for building the URL that is unique to the city.
    private fun buildURL(latitude: Double, longitude: Double): String {

        return "https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude" +
                "&current=temperature_2m,precipitation,wind_speed_10m&daily=temperature_2m_max,temperature_2m_min," +
                "sunrise,sunset&temperature_unit=fahrenheit&wind_speed_unit=mph&precipitation_unit=inch"
    }

    //This function takes the weather data as an input and is responsible for printing all the weather data.
    fun printWeatherData(weatherData: WeatherData) {
        val currentWeather = weatherData.currentWeather

        println("Current Weather:")
        println("Temperature: ${currentWeather.temperature}°F")
        println("Precipitation: ${currentWeather.precipitation} inches")
        println("Wind Speed: ${currentWeather.windSpeed} mph")
        println()

        println("Forecast: ")
        weatherData.dailyWeather.forEach { daily ->
            println("Date: ${daily.date}")
            println("Max Temperature: ${daily.maxTemp}")
            println("Min Temperature: ${daily.minTemp}")
            println("Sunrise: ${daily.sunrise}")
            println("Sunset: ${daily.sunset}")
            println()

        }
    }
}