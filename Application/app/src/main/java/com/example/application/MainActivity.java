package com.example.application;

import android.os.Bundle;
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
    private Button buttonAdd;
    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        editText = findViewById(R.id.editText);
        buttonAdd = findViewById(R.id.button_add);
        listView = findViewById(R.id.listView);

        // Initialize list and adapter
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);

        // Set up button click listener
        buttonAdd.setOnClickListener(v -> addFood());
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
            // Show error toast if EditText is empty
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }
    }
}
