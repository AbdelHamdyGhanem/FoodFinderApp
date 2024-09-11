package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Initialize variables
    private EditText AddFood;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AddFood = findViewById(R.id.AddFood);
        Button buttonAdd = findViewById(R.id.button_add);
        ListView listView = findViewById(R.id.listView);

        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);

        // Set up button click listener
        buttonAdd.setOnClickListener(v -> addFood());

        Button button_next_foodPref = findViewById(R.id.button_next_foodPref);
        button_next_foodPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FoodPrefActivity.class);
                // Pass the list of foods to FoodPrefActivity
                intent.putStringArrayListExtra("foods", arrayList);
                startActivity(intent);
            }
        });
    }

    private void addFood() {
        String food = AddFood.getText().toString().trim();

        if (!food.isEmpty() && !arrayList.contains(food)) {
            arrayList.add(food);
            adapter.notifyDataSetChanged();
            AddFood.setText("");
            Toast.makeText(this, "Food item added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Item is empty or already exists", Toast.LENGTH_SHORT).show();
        }
    }
}
