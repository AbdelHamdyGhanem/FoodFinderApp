package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class FoodPrefActivity extends AppCompatActivity {

    private EditText numberofRecipes;
    private Switch maximizeIngredients;
    private Switch ignorePantry;
    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        Button button_next_API = findViewById(R.id.button_next_API);

        numberofRecipes = findViewById(R.id.number_of_recipes);
        maximizeIngredients = findViewById(R.id.maximizeIngredients);
        ignorePantry = findViewById(R.id.ignorePantry);
        arrayList = getIntent().getStringArrayListExtra("foods");

        HashMap<Integer, Object> preferences = new HashMap<>();

        // pass data
        button_next_API.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passDataToApiTime();
            }
        });
    }

    private void passDataToApiTime() {
        String numberOfRecipes = this.numberofRecipes.getText().toString();
        Boolean maximizeIngredients = this.maximizeIngredients.isChecked();
        Boolean ignorePantry = this.ignorePantry.isChecked();

        HashMap preferences = new HashMap();
        preferences.put("numberOfRecipes", numberOfRecipes);
        preferences.put("maximizeIngredients", maximizeIngredients);
        preferences.put("ignorePantry", ignorePantry);

        Intent intent = new Intent(this, ApiTime.class);
        intent.putExtra("foods", arrayList);
        intent.putExtra("preferences", preferences);
        startActivity(intent);

        startActivity(intent);
    }
}
