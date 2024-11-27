package com.example.application

import android.os.AsyncTask
import android.util.Log
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class PositionStackHelper {

    companion object {
        const val API_KEY = "5d2d4e5a867072090e08e76b20ca097f"

        // Function to get location based on the address
        fun getUserLocation(address: String, callback: (latitude: Double?, longitude: Double?) -> Unit) {
            // Background task to avoid blocking the UI thread
            GeocodeTask(address, callback).execute()
        }

        // AsyncTask to handle network operations
        private class GeocodeTask(val address: String, val callback: (latitude: Double?, longitude: Double?) -> Unit) :
            AsyncTask<Void, Void, Pair<Double, Double>>() {

            override fun doInBackground(vararg params: Void?): Pair<Double, Double> {
                var latitude = 0.0
                var longitude = 0.0

                try {
                    val url = "http://api.positionstack.com/v1/forward?access_key=$API_KEY&query=$address"
                    val connection = URL(url).openConnection() as HttpURLConnection
                    connection.connect()

                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(response)

                    val data = jsonResponse.getJSONArray("data")
                    if (data.length() > 0) {
                        val location = data.getJSONObject(0)
                        latitude = location.getDouble("latitude")
                        longitude = location.getDouble("longitude")
                    }
                } catch (e: Exception) {
                    Log.e("PositionStackHelper", "Error fetching location data", e)
                }

                return Pair(latitude, longitude)
            }

            override fun onPostExecute(result: Pair<Double, Double>) {
                super.onPostExecute(result)
                callback(result.first, result.second)
            }
        }
    }
}
