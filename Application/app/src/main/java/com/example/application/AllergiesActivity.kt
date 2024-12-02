package com.example.application

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class AllergiesActivity : AppCompatActivity() {
    private val FILE_NAME = "allergies.txt"
    private lateinit var allergiesList: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allergies)

        val allergyInput = findViewById<EditText>(R.id.allergyInput)
        val addButton = findViewById<Button>(R.id.button_add_allergy)
        val allergiesListView = findViewById<ListView>(R.id.allergiesListView)

        // Load allergies from file
        allergiesList = loadAllergiesFromFile()

        // Set up adapter for ListView
        adapter = ItemAdapter(this, allergiesList, FILE_NAME)
        allergiesListView.adapter = adapter

        // Add allergy to list and save
        addButton.setOnClickListener {
            val allergy = allergyInput.text.toString().trim()

            if (allergy.isNotBlank()) {
                val allergiesToAdd = mutableListOf(allergy)

                // Function to get the singular form of a word (basic implementation)
                fun getSingularForm(word: String): String {
                    if (word.endsWith("s") && word.length > 1) {
                        return word.substring(0, word.length - 1)
                    }
                    return word
                }

                // Add the singular form if it's different from the original
                val singularForm = getSingularForm(allergy)
                if (singularForm != allergy && !allergiesList.contains(singularForm)) {
                    allergiesToAdd.add(singularForm)
                }

                // Add allergies to the list and update UI
                allergiesList.addAll(allergiesToAdd.filterNot { allergiesList.contains(it) })
                adapter.notifyDataSetChanged()
                saveAllergiesToFile()
                allergyInput.text.clear()
                Toast.makeText(this, "Allergy added!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Invalid entry!", Toast.LENGTH_SHORT).show()
            }
        }

        // Remove allergy on item long press
        allergiesListView.setOnItemLongClickListener { _, _, position, _ ->
            allergiesList.removeAt(position)
            adapter.notifyDataSetChanged()
            saveAllergiesToFile()
            Toast.makeText(this, "Allergy removed!", Toast.LENGTH_SHORT).show()
            true
        }

        val buttonHomepage = findViewById<Button>(R.id.button_homepage)
        buttonHomepage.setOnClickListener {
            finish()
        }
    }

    private fun loadAllergiesFromFile(): ArrayList<String> {
        val allergies = ArrayList<String>()
        val file = File(filesDir, FILE_NAME)
        if (file.exists()) {
            file.readLines().forEach { line ->
                if (line.isNotBlank()) allergies.add(line.trim())
            }
        }
        return allergies
    }

    private fun saveAllergiesToFile() {
        val file = File(filesDir, FILE_NAME)
        file.writeText(allergiesList.joinToString("\n"))
    }
}
