package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ApiTime extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_api_time);

        Intent intent = getIntent();
        ArrayList<String> selectedFoods = intent.getStringArrayListExtra("foods");
        HashMap<String, Object> foodPreferences = (HashMap<String, Object>) intent.getSerializableExtra("preferences");

        TextView textView = findViewById(R.id.showData);

        StringBuilder displayStringBuilder = new StringBuilder("Selected Foods:\n");
        if (selectedFoods != null && !selectedFoods.isEmpty()) {
            for (String food : selectedFoods) {
                displayStringBuilder.append(food).append("\n");
            }
        } else {
            displayStringBuilder.append("No foods selected.\n");
        }

        displayStringBuilder.append("\nFood Preferences:\n");

        if (foodPreferences != null) {
            String numberOfRecipes = (String) foodPreferences.get("numberOfRecipes");
            displayStringBuilder.append("Number of Recipes: ").append(numberOfRecipes).append("\n");

            Boolean maximizeIngredients = (Boolean) foodPreferences.get("maximizeIngredients");
            Boolean ignorePantry = (Boolean) foodPreferences.get("ignorePantry");

            displayStringBuilder.append("Maximize Ingredients: ").append(maximizeIngredients ? "Yes" : "No").append("\n");
            displayStringBuilder.append("Ignore Pantry: ").append(ignorePantry ? "Yes" : "No").append("\n");
        }

        textView.setText(displayStringBuilder.toString());
    }
}