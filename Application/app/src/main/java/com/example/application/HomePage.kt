package com.example.application

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)

        val imageView = findViewById<ImageView>(R.id.logo)
        imageView.setImageResource(R.drawable.bros_cooking_logo) // Replace 'logo' with your image name

        // Button to navigate to MainActivity (Add Foods)
        val buttonAddFoods = findViewById<Button>(R.id.button_add_foods)
        buttonAddFoods.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Button to navigate to Food Preferences
        val buttonFoodPreferences = findViewById<Button>(R.id.button_pantry)
        buttonFoodPreferences.setOnClickListener {
            val intent = Intent(this, PantryActivity::class.java)
            startActivity(intent)
        }

        // Button to navigate to Shopping List
        val buttonShoppingList = findViewById<Button>(R.id.button_shopping_list)
        buttonShoppingList.setOnClickListener {
            val intent = Intent(this, ShoppingListActivity::class.java)
            startActivity(intent)
        }

        // Button to navigate to Favorites
        val buttonFavorites = findViewById<Button>(R.id.button_favorites)
        buttonFavorites.setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
        }
    }
}
