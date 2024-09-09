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
    private EditText editText;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        editText = findViewById(R.id.editText);
        Button buttonAdd = findViewById(R.id.button_add);
        ListView listView = findViewById(R.id.listView);

        // Initialize list and adapter
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);

        // Set up button click listener
        buttonAdd.setOnClickListener(v -> addFood());

        // Navigates the user to the next activity
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
        // Get text from EditText
        String food = editText.getText().toString().trim();

        // Check if the text is not empty
        if (!food.isEmpty() && !arrayList.contains(food)) {
            // Add item to list and update ListView
            arrayList.add(food);
            adapter.notifyDataSetChanged();
            // Clear the EditText
            editText.setText("");
            // Show confirmation toast
            Toast.makeText(this, "Food item added", Toast.LENGTH_SHORT).show();
        } else {
            // Show error toast if EditText is empty or food item already exists
            Toast.makeText(this, "Item is empty or already exists", Toast.LENGTH_SHORT).show();
        }
    }
}
