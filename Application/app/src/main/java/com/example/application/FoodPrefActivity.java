package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class FoodPrefActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        Button button_next_API = findViewById(R.id.button_next_API);
        button_next_API.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CheckBox checkBoxSavory = findViewById(R.id.checkBoxSavory);
                CheckBox checkBoxSweet = findViewById(R.id.checkBoxSweet);
                CheckBox checkBoxVegan = findViewById(R.id.checkBoxVegan);

                HashMap<String, Boolean> foodPreferences = new HashMap<>();
                foodPreferences.put("Savory", checkBoxSavory.isChecked());
                foodPreferences.put("Sweet", checkBoxSweet.isChecked());
                foodPreferences.put("Vegan", checkBoxVegan.isChecked());

                Intent intent = new Intent(getApplicationContext(), ApiTime.class);
                intent.putExtra("preferences", foodPreferences);

                // Retrieve the selected foods from MainActivity
                ArrayList<String> selectedFoods = getIntent().getStringArrayListExtra("foods");
                intent.putStringArrayListExtra("foods", selectedFoods);

                startActivity(intent);
            }
        });

        Button button_previous = findViewById(R.id.button_prev_foodPref);
        button_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate back to MainActivity
                Intent intent = new Intent(FoodPrefActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
