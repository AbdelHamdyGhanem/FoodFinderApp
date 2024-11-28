package com.example.application

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class PantryActivity : AppCompatActivity() {
    private val FILE_NAME = "pantry_items.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantry)

        val pantryListView = findViewById<ListView>(R.id.pantryListView)
        val pantryItems = loadItemsFromFile()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, pantryItems)
        pantryListView.adapter = adapter
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
}
