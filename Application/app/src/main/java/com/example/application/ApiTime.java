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

    // This is going to be changed to send this data to an API instead of printing it to the screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_api_time);

        Intent intent = getIntent();
        ArrayList<String> selectedFoods = intent.getStringArrayListExtra("foods");
        HashMap<String, Boolean> foodPreferences = (HashMap<String, Boolean>) intent.getSerializableExtra("preferences");

        TextView textView = findViewById(R.id.showData);

        // Display the foods
        StringBuilder foodsStringBuilder = new StringBuilder("Selected Foods:\n");
        if (selectedFoods != null) {
            for (String food : selectedFoods) {
                foodsStringBuilder.append(food).append("\n");
            }
        }
        foodsStringBuilder.append("\nFood Preferences:\n");

        // Display the preferences
        if (foodPreferences != null) {
            for (String key : foodPreferences.keySet()) {
                if (Boolean.TRUE.equals(foodPreferences.get(key))) {
                    foodsStringBuilder.append(key).append("\n");
                }
            }
        }

        textView.setText(foodsStringBuilder.toString());
    }
}
