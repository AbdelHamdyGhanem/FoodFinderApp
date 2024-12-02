package com.example.application

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
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
    private lateinit var gestureDetector: GestureDetector
    private var favoritesList = mutableListOf<String>()
    private var Ingredients = ""
    private var instructions = ""
    private var nutritionData = ""

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

        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                val cardView = findViewAtLocation(e)
                cardView?.let {
                    val title = it.findViewById<TextView>(R.id.foodName).text.toString()
                    val favoriteIcon = it.findViewById<TextView>(R.id.favoriteIcon)
                    toggleFavorite(favoriteIcon, title)
                }
                return true
            }
        })
    }

    private fun findViewAtLocation(event: MotionEvent): View? {
        val x = event.x.toInt()
        val y = event.y.toInt()
        for (i in 0 until foodContainer!!.childCount) {
            val child = foodContainer!!.getChildAt(i)
            if (x >= child.left && x <= child.right && y >= child.top && y <= child.bottom) {
                return child
            }
        }
        return null
    }

    private fun sendData() {
        Thread {
            try {
                Ingredients = java.lang.String.join(",", selectedFoods)
                val url = URL(
                    "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/findByIngredients?ingredients=" + URLEncoder.encode(
                        Ingredients,
                        "UTF-8"
                    ) + "&number=" + numberOfRecipes + "&ignorePantry=" + ignorePantry + "&ranking=" + maximizeIngredients
                )

                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                conn.setRequestProperty(
                    "x-rapidapi-key",
                    BuildConfig.API_KEY
                )
                conn.setRequestProperty(
                    "x-rapidapi-host",
                    BuildConfig.API_HOST
                )

                val responseCode = conn.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val `in` = BufferedReader(InputStreamReader(conn.inputStream))
                    val content = StringBuilder()
                    var inputLine: String?
                    while (`in`.readLine().also { inputLine = it } != null) {
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
                val recipeId = recipe.getInt("id")
                val imageUrl = recipe.getString("image")

                val cardView = layoutInflater.inflate(R.layout.card_layout, null)
                val foodName = cardView.findViewById<TextView>(R.id.foodName)
                val foodDescription = cardView.findViewById<TextView>(R.id.foodDescription)
                val favoriteIcon = cardView.findViewById<TextView>(R.id.favoriteIcon)

                val allergens = loadAllergiesFromFile() // Load user's allergens

                for (i in 0 until jsonArray.length()) {
                    val usedIngredients = recipe.getJSONArray("usedIngredients")
                    val missedIngredients = recipe.getJSONArray("missedIngredients")


                    // Find allergens in the recipe
                    val allergensFound = allergens.filter { allergen ->
                        (0 until usedIngredients.length()).any {
                            usedIngredients.getJSONObject(it).getString("name").contains(allergen, true)
                        } ||
                                (0 until missedIngredients.length()).any {
                                    missedIngredients.getJSONObject(it).getString("name").contains(allergen, true)
                                }
                    }
                    Log.d("Allergens Found", allergensFound.toString())

                    // Allergen warning text
                    val allergenWarning = if (allergensFound.isNotEmpty()) {
                        "\n⚠ Contains known allergens: ${allergensFound.joinToString(", ")}"
                    } else ""

                    foodName.text = title
                    cardView.setOnClickListener {
                        fetchRecipeDetails(recipeId, title, imageUrl)
                    }
                    favoriteIcon.text = "♡"
                    favoriteIcon.tag = false

                    favoriteIcon.setOnClickListener { v ->
                        toggleFavorite(v as TextView, title)
                    }

                    foodDescription.text = "Ingredients used: ${usedIngredients.length()}${allergenWarning}"

                    foodContainer!!.addView(cardView)
                    fetchNutritionData(title, foodDescription)
                }

            } catch (e: Exception) {
                Log.e("JSON Parsing Error", e.toString())
            }
        }
    }

    // Helper to load allergies from file
    private fun loadAllergiesFromFile(): List<String> {
        val file = File(filesDir, "allergies.txt")
        return if (file.exists()) {
            file.readLines().map { it.trim() }
        } else {
            emptyList()
        }
    }

    private fun fetchRecipeDetails(recipeId: Int, title: String, imageUrl: String) {
        Thread {
            try {
                // Construct the API URL to get detailed recipe information
                val url = URL("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/$recipeId/information")

                // Open a connection to the API
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                conn.setRequestProperty(
                    "x-rapidapi-key",
                    BuildConfig.API_KEY
                )
                conn.setRequestProperty(
                    "x-rapidapi-host",
                    BuildConfig.API_HOST
                )

                // Check if the response code is HTTP OK (200)
                val responseCode = conn.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read the response from the API
                    val reader = BufferedReader(InputStreamReader(conn.inputStream))
                    val content = StringBuilder()
                    var inputLine: String?
                    while (reader.readLine().also { inputLine = it } != null) {
                        content.append(inputLine)
                    }
                    reader.close()

                    // Parse the response JSON object
                    val jsonObject = JSONObject(content.toString())

                    // Extract the recipe instructions and any other relevant data
                    instructions = jsonObject.optString("instructions", "Instructions not available")
                    val ingredients = jsonObject.optJSONArray("extendedIngredients")
                    val ingredientList = StringBuilder()

                    // Check for ingredients array and iterate
                    if (ingredients != null) {
                        for (i in 0 until ingredients.length()) {
                            val ingredient = ingredients.getJSONObject(i)
                            // Safely check for "originalString" and append it
                            val originalString = ingredient.optString("originalString", "No ingredient description")
                            ingredientList.append(originalString).append("\n")
                        }
                    } else {
                        ingredientList.append("No ingredients available")
                    }

                    runOnUiThread {
                        val intent = Intent(this, RecipeDetailActivity::class.java)
                        intent.putExtra("recipeTitle", title)
                        intent.putExtra("recipeInstructions", instructions)
                        intent.putExtra("ingredients", Ingredients)
                        intent.putExtra("imageUrl", imageUrl)
                        startActivity(intent)
                    }
                } else {
                    Log.e("API Error", "Response Code: $responseCode")
                }
            } catch (e: Exception) {
                Log.e("Error", e.toString())
            }
            Log.d("Image URL", imageUrl)
        }.start()
    }


    private fun fetchNutritionData(foodTitle: String, foodDescription: TextView) {
        Thread {
            try {
                val url = URL(
                    "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/guessNutrition?title=" + URLEncoder.encode(foodTitle, "UTF-8")
                )

                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                conn.setRequestProperty(
                    "x-rapidapi-key",
                    BuildConfig.API_KEY
                )
                conn.setRequestProperty(
                    "x-rapidapi-host",
                    BuildConfig.API_HOST
                )

                val responseCode = conn.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val `in` = BufferedReader(InputStreamReader(conn.inputStream))
                    val content = StringBuilder()
                    var inputLine: String?
                    while (`in`.readLine().also { inputLine = it } != null) {
                        content.append(inputLine)
                    }
                    `in`.close()
                    val jsonObject = JSONObject(content.toString())
                    runOnUiThread {
                        val calories = if (jsonObject.has("calories") && !jsonObject.isNull("calories")) {
                            jsonObject.getJSONObject("calories").optString("value", "N/A")
                        } else {
                            "N/A"
                        }
                        val fatTotal = if (jsonObject.has("fat") && !jsonObject.isNull("fat")) {
                            jsonObject.getJSONObject("fat").optString("value", "N/A")
                        } else {
                            "N/A"
                        }
                        val protein = if (jsonObject.has("protein") && !jsonObject.isNull("protein")) {
                            jsonObject.getJSONObject("protein").optString("value", "N/A")
                        } else {
                            "N/A"
                        }
                        val carbs = if (jsonObject.has("carbs") && !jsonObject.isNull("carbs")) {
                            jsonObject.getJSONObject("carbs").optString("value", "N/A")
                        } else {
                            "N/A"
                        }
                        nutritionData = "${foodDescription.text}\n\nCalories: $calories\nFat: $fatTotal g\nSugar: $carbs g\nProtein: $protein g"

                        foodDescription.text = nutritionData
                    }
                } else {
                    Log.e("API Error", "Response Code: $responseCode")
                }
            } catch (e: Exception) {
                Log.e("Error", e.toString())
            }
        }.start()
    }

    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    private fun toggleFavorite(favoriteIcon: TextView, title: String) {
        val isFavorite = favoriteIcon.tag as Boolean
        val editor = sharedPreferences.edit()

        if (!isFavorite) {
            favoriteIcon.text = "❤️"
            favoriteIcon.tag = true

            // Create a JSON string to store recipe details
            val recipeDetails = JSONObject().apply {
                put("title", title)
                put("nutritionData", nutritionData)
                put("instructions", instructions)
                put("ingredients", Ingredients)
            }.toString()

            favoritesList.add(recipeDetails)
            editor.putStringSet("favorites", favoritesList.toSet()).apply()

            Toast.makeText(this, "$title has been added to your favorites", Toast.LENGTH_SHORT).show()
        } else {
            favoriteIcon.text = "♡"
            favoriteIcon.tag = false

            // Remove the recipe by title
            favoritesList.removeIf { JSONObject(it).getString("title") == title }
            editor.putStringSet("favorites", favoritesList.toSet()).apply()

            Toast.makeText(this, "$title has been removed from favorites", Toast.LENGTH_SHORT).show()
        }
    }

    fun showFavorites(view: View) {
        val intent = Intent(this, FavoritesActivity::class.java)
        intent.putStringArrayListExtra("favoritesList", ArrayList(favoritesList))
        intent.putExtra("nutritionData", nutritionData)
        intent.putExtra("ingredients", Ingredients)
        intent.putExtra("instructions", instructions)
        startActivity(intent)
    }

    fun navigateToHomepage(view: View) {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }


}
