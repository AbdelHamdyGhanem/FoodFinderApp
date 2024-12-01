package com.example.application

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class FavoritesActivity : AppCompatActivity() {

    private lateinit var favoritesRecyclerView: RecyclerView
    private lateinit var favoritesAdapter: FavoritesAdapter
    private val favoritesList: MutableList<String> by lazy {
        intent.getStringArrayListExtra("favoritesList")?.toMutableList() ?: mutableListOf()
    }

    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView)
        favoritesRecyclerView.layoutManager = LinearLayoutManager(this)
        val favoritesList = sharedPreferences.getStringSet("favorites", emptySet())?.toMutableList()
            ?: mutableListOf()
        favoritesAdapter = FavoritesAdapter(favoritesList)
        favoritesRecyclerView.adapter = favoritesAdapter

        findViewById<View>(R.id.button_back).setOnClickListener {
            finish() // Go back to the previous screen
        }
    }

    inner class FavoritesAdapter(private val items: MutableList<String>) :
        RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

        inner class FavoritesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val foodName: TextView = view.findViewById(R.id.foodName)
            val foodDescription: TextView = view.findViewById(R.id.foodDescription)
            val favoriteIcon: TextView = view.findViewById(R.id.favoriteIcon)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_layout, parent, false)
            return FavoritesViewHolder(view)
        }

        override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
            val item = items[position]
            holder.foodName.text = item
            holder.favoriteIcon.text = "❤️" // Initially set as favorite

            // Load nutrition data for each food item
            fetchNutritionData(item, holder.foodDescription)

            // Toggle favorite on click
            holder.favoriteIcon.setOnClickListener {
                // Remove from favorites
                items.removeAt(holder.adapterPosition)
                notifyItemRemoved(holder.adapterPosition)
                Toast.makeText(this@FavoritesActivity, "$item removed from favorites", Toast.LENGTH_SHORT).show()
            }
        }

        override fun getItemCount(): Int = items.size

        private fun fetchNutritionData(foodTitle: String, foodDescription: TextView) {
            Thread {
                try {
                    val apiUrl = "https://api.api-ninjas.com/v1/nutrition?query=" + URLEncoder.encode(foodTitle, "UTF-8")
                    val url = URL(apiUrl)

                    val conn = url.openConnection() as HttpURLConnection
                    conn.requestMethod = "GET"
                    conn.setRequestProperty("X-Api-Key", "kLB2Kaq64UizpgnzYYxoiQ==fZSjgLIWhOmKqByu")

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
                        runOnUiThread {
                            if (jsonArray.length() > 0) {
                                val foodItem = jsonArray.getJSONObject(0)
                                val calories = foodItem.optString("calories", "N/A")
                                val fatTotal = foodItem.optString("fat_total_g", "N/A")
                                val sugar = foodItem.optString("sugar_g", "N/A")
                                val protein = foodItem.optString("protein_g", "N/A")

                                foodDescription.text = "Calories: $calories\nFat: $fatTotal g\nSugar: $sugar g\nProtein: $protein g"
                            } else {
                                foodDescription.text = "Nutrition data not available."
                            }
                        }
                    } else {
                        runOnUiThread {
                            foodDescription.text = "Failed to load nutrition data."
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        foodDescription.text = "Error loading nutrition data."
                    }
                }
            }.start()
        }
    }
}
