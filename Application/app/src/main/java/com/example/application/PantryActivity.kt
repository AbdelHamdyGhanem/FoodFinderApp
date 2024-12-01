package com.example.application

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import com.example.application.PantryAdapter


class PantryActivity : AppCompatActivity() {
    private val FILE_NAME = "pantry_items.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantry)

        val pantryListView = findViewById<ListView>(R.id.pantryListView)
        val pantryItems = loadItemsFromFile().toMutableList()

        // Declare pantryAdapter here before usage
        lateinit var pantryAdapter: PantryAdapter

            pantryAdapter = PantryAdapter(this, pantryItems) { item ->
            // Remove the item and update the list
            pantryItems.remove(item)
            saveItemsToFile(pantryItems)
            pantryAdapter.notifyDataSetChanged()
        }

        pantryListView.adapter = pantryAdapter

        // Home Button Logic
        val buttonHome = findViewById<Button>(R.id.button_home)
        buttonHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // Food Preferences Button Logic
        val buttonFoodPreferences = findViewById<Button>(R.id.button_food_preferences)
        buttonFoodPreferences.setOnClickListener {
            val intent = Intent(this, FoodPrefActivity::class.java)
            startActivity(intent)
        }

        // Allergies Button Logic
        val buttonAllergies = findViewById<Button>(R.id.button_allergies)
        buttonAllergies.setOnClickListener {
            val intent = Intent(this, AllergiesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadItemsFromFile(): List<String> {
        val items = mutableListOf<String>()
        try {
            val file = File(filesDir, FILE_NAME)
            if (file.exists()) {
                file.forEachLine { line ->
                    if (line.isNotBlank() && !items.contains(line.trim())) { // Avoid duplicates
                        items.add(line.trim())
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return items
    }

    private fun saveItemsToFile(items: List<String>) {
        try {
            val file = File(filesDir, FILE_NAME)
            file.printWriter().use { writer ->
                items.forEach { writer.println(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
