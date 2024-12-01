package com.example.application

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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

        // Back button functionality
        val backButton = findViewById<Button>(R.id.button)
        backButton.setOnClickListener {
            finish()
        }

        // Facebook Share Button
        val shareButton = findViewById<Button>(R.id.shareButton)
        shareButton.setOnClickListener {
            shareRecipeOnFacebook(recipeTitle, recipeInstructions)
        }
    }

    private fun shareRecipeOnFacebook(title: String?, instructions: String?) {
        val shareText = "Check out this recipe: $title\n\nInstructions:\n$instructions"

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
            setPackage("com.facebook.katana") // Ensures Facebook app is used if installed
        }

        // Check if the Facebook app is installed
        if (shareIntent.resolveActivity(packageManager) != null) {
            startActivity(shareIntent)
        } else {
            // Fallback: Open the default share dialog
            val fallbackIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(fallbackIntent, "Share via"))
        }
    }
}
