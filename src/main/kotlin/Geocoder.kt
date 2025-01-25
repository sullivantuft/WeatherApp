package org.example

import org.json.JSONArray

//This class is responsible for converting a city name into longitude and latitude coordinates. This is done using the OpenStreetMap API
class Geocoder {
    //This function gets the coordinates of a given city
    fun getCoordinates(city: String) : City?{
        val requests = apiRequests() //initializes new request object

        val url = "https://nominatim.openstreetmap.org/search?q=$city&format=json" //OpenStreetMap API URL, returns JSON format of the coordinates
        val jsonResponse = requests.sendRequest(url) //Sends a request and gets a JSON response as a string
        return if (jsonResponse != null) {
            parseCoordinates(jsonResponse, city) //If the JSON Response is not null, it returns the coordinates for the city
        }else{
            null
        }
    }
    //This function sends an HTTP GET request to OpenStreetMap API and returns the response as a string

    //This function parses the JSON response and returns the coordinates as a City object
    private fun parseCoordinates(jsonResponse: String, city:String) : City?
    {
        return try{
            val jsonArray = JSONArray(jsonResponse) //Creates an array from the response string

            if(jsonArray.length() > 0) {
                val firstResult = jsonArray.getJSONObject(0) //Uses the first result from the JSON response
                val latitude = firstResult.getDouble("lat") //Gets Latitude and Longitude from the first response
                val longitude = firstResult.getDouble("lon")

                City(city, latitude, longitude) //Create and return city object with latitude and longitude
            }else{
                println("No results found")
                null
            }
        }catch(e: Exception) {
            println("Error sending request: ${e.message}") //Displays an error message if not found
            return null
        }

    }
}
