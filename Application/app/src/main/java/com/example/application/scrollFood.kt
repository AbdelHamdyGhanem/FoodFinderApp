package com.example.application

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class scrollFood : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_food)

        // Get the list of foods passed from the previous activity
        val foodList = intent.getStringArrayListExtra("foodList")

        // Get the LinearLayout where the food items will be displayed
        val foodContainer = findViewById<LinearLayout>(R.id.foodContainer)

        // Dynamically create TextViews for each food item
        if (foodList != null) {
            for (food in foodList) {
                val foodTextView = TextView(this)
                foodTextView.text = food
                foodTextView.setPadding(16, 16, 16, 16)
                foodTextView.textSize = 18f
                foodContainer.addView(foodTextView)
            }
        }
    }
}

