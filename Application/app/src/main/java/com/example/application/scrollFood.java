package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class scrollFood extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_food);

        // Get the list of foods passed from the previous activity
        ArrayList<String> foodList = getIntent().getStringArrayListExtra("foodList");

        // Get the LinearLayout where the food items will be displayed
        LinearLayout foodContainer = findViewById(R.id.foodContainer);

        // Dynamically create TextViews for each food item
        if (foodList != null) {
            for (String food : foodList) {
                TextView foodTextView = new TextView(this);
                foodTextView.setText(food);
                foodTextView.setPadding(16, 16, 16, 16);
                foodTextView.setTextSize(18);
                foodContainer.addView(foodTextView);
            }
        }
    }
}

