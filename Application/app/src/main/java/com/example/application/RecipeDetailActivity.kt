package com.example.application

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RecipeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recipe_detail)

        // Get the data passed from ApiTime
        val recipeTitle = intent.getStringExtra("recipeTitle")
        val recipeInstructions = intent.getStringExtra("recipeInstructions")
        val imageUrl = intent.getStringExtra("imageUrl")

        // Set the views
        val recipeTitleView = findViewById<TextView>(R.id.recipeTitle)
        val recipeInstructionsView = findViewById<TextView>(R.id.recipeInstructions)
        val recipeImageView = findViewById<ImageView>(R.id.recipeImage)

        // Set the data
        recipeTitleView.text = recipeTitle
        recipeInstructionsView.text = recipeInstructions

        // Load the image using Picasso or Glide (Picasso is used here for simplicity)
        Picasso.get().load(imageUrl).into(recipeImageView)

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }
}