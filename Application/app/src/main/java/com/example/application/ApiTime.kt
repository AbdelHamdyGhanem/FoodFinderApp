package com.example.application

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class ApiTime : AppCompatActivity() {
    private var foodContainer: LinearLayout? = null
    private var selectedFoods: ArrayList<String>? = null
    private var foodPreferences: HashMap<String, Any>? = null
    private var numberOfRecipes: String? = null
    private var ignorePantry = false
    private var maximizeIngredients = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_api_time)

        val intent = intent
        selectedFoods = intent.getStringArrayListExtra("foods")
        foodPreferences = intent.getSerializableExtra("preferences") as HashMap<String, Any>?

        foodContainer = findViewById(R.id.foodContainer)
        numberOfRecipes = foodPreferences!!["numberOfRecipes"] as String?
        maximizeIngredients = if (foodPreferences!!["maximizeIngredients"] == "Yes") 2 else 1
        ignorePantry = foodPreferences!!["ignorePantry"] == "No"

        sendData()
    }

    fun sendData() {
        Thread {
            try {
                val ingredients = java.lang.String.join(",", selectedFoods)
                val url = URL(
                    "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/findByIngredients?ingredients=" + URLEncoder.encode(
                        ingredients,
                        "UTF-8"
                    )
                            + "&number=" + numberOfRecipes + "&ignorePantry=" + ignorePantry + "&ranking=" + maximizeIngredients
                )

                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                conn.setRequestProperty(
                    "x-rapidapi-key",
                    "a2ae691b53msh393e153de705864p186a6cjsnbdf23b779fc9"
                )
                conn.setRequestProperty(
                    "x-rapidapi-host",
                    "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com"
                )

                val responseCode = conn.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val `in` = BufferedReader(InputStreamReader(conn.inputStream))
                    var inputLine: String?
                    val content = StringBuilder()
                    while ((`in`.readLine().also { inputLine = it }) != null) {
                        content.append(inputLine)
                    }
                    `in`.close()
                    val jsonArray = JSONArray(content.toString())
                    runOnUiThread { displayFoodItems(jsonArray) }
                } else {
                    Log.e("API Error", "Response Code: $responseCode")
                }
            } catch (e: Exception) {
                Log.e("Error", e.toString())
            }
        }.start()
    }

    private fun displayFoodItems(jsonArray: JSONArray) {
        for (i in 0 until jsonArray.length()) {
            try {
                val recipe = jsonArray.getJSONObject(i)
                val title = recipe.getString("title")
                val description = recipe.optString("description", "No description available.")

                // Inflate the card layout
                val cardView = layoutInflater.inflate(R.layout.card_layout, null)
                val foodName = cardView.findViewById<TextView>(R.id.foodName)
                val foodDescription = cardView.findViewById<TextView>(R.id.foodDescription)
                val favoriteIcon =
                    cardView.findViewById<TextView>(R.id.favoriteIcon) // Make sure this is a TextView

                foodName.text = title
                foodDescription.text = description

                // Initially set the heart emoji as unfilled
                favoriteIcon.text = "♡" // Empty heart
                favoriteIcon.tag = false // Tag to track filled state

                // Set click listener for the heart emoji
                favoriteIcon.setOnClickListener { v ->
                    val isFavorite = v.tag as Boolean
                    if (!isFavorite) {
                        (v as TextView).text = "❤️" // Filled heart
                        v.setTag(true) // Update tag to filled
                        Toast.makeText(
                            this@ApiTime,
                            "$title has been added to the favorites tab",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        (v as TextView).text = "♡" // Back to empty heart
                        v.setTag(false) // Update tag to unfilled
                        Toast.makeText(
                            this@ApiTime,
                            "$title has been removed from favorites",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                foodContainer!!.addView(cardView)
            } catch (e: Exception) {
                Log.e("JSON Parsing Error", e.toString())
            }
        }
    }

    fun selectFood(view: View?) {
        val intent = Intent(this@ApiTime, scrollFood::class.java)
        startActivity(intent)
    }
}
