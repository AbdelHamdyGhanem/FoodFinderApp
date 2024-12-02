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
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.Properties

class FavoritesActivity : AppCompatActivity() {

    private lateinit var favoritesRecyclerView: RecyclerView
    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var nutritionData: String
    private lateinit var ingredients: String
    private lateinit var instructions: String

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
            val recipeDetails = JSONObject(item)

            val title = recipeDetails.getString("title")
            val nutritionData = recipeDetails.getString("nutritionData")
            val ingredients = recipeDetails.getString("ingredients")
            val instructions = recipeDetails.getString("instructions")

            holder.foodName.text = title
            holder.foodDescription.text = "$ingredients\n\n$nutritionData\n\n$instructions"
            holder.favoriteIcon.text = "❤️"

            holder.favoriteIcon.setOnClickListener {
                // Remove from favorites
                val newItems = items.toMutableList()
                newItems.removeAt(holder.adapterPosition)
                val editor = sharedPreferences.edit()
                editor.putStringSet("favorites", newItems.toSet()).apply()

                Toast.makeText(this@FavoritesActivity, "$title removed from favorites", Toast.LENGTH_SHORT).show()
                notifyItemRemoved(holder.bindingAdapterPosition)
            }
        }

        override fun getItemCount(): Int = items.size

        private fun fetchNutritionData(foodTitle: String, foodDescription: TextView) {
            Thread {
                foodDescription.text = ingredients + "\n" + nutritionData + "\n" + instructions

            }.start()
        }
    }
}
