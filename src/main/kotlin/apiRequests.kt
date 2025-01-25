package org.example

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class apiRequests {
    fun sendRequest(url: String): String?{

        return try {
            val connection = URL(url).openConnection() as HttpURLConnection //Opens connection to specified URL

            connection.requestMethod = "GET" //Set request method to get
            connection.setRequestProperty("User-Agent", "Kotlin-WeatherApp") //Adds a header to avoid being denied the HTTP request

            val reader = BufferedReader(InputStreamReader(connection.inputStream)) //Creates a BufferedReader to read the input stream of the connection
            val responseBuilder = StringBuilder() //Accumulates response data
            var line: String?

            while (reader.readLine().also { line = it } != null) { //Read each line from the response and append it to the response builder
                responseBuilder.append(line)
            }
            reader.close()

            responseBuilder.toString()
        }catch (e: Exception) {
            println("Error sending request: ${e.message}") //Sends error message if not completed
            null
        }
    }
}