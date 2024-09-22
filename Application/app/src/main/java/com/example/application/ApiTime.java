package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class ApiTime extends AppCompatActivity {

    private LinearLayout foodContainer;
    private ArrayList<String> selectedFoods;
    private HashMap<String, Object> foodPreferences;
    private String numberOfRecipes;
    private boolean ignorePantry;
    private int maximizeIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_api_time);

        Intent intent = getIntent();
        selectedFoods = intent.getStringArrayListExtra("foods");
        foodPreferences = (HashMap<String, Object>) intent.getSerializableExtra("preferences");

        foodContainer = findViewById(R.id.foodContainer);
        numberOfRecipes = (String) foodPreferences.get("numberOfRecipes");
        maximizeIngredients = foodPreferences.get("maximizeIngredients").equals("Yes") ? 2 : 1;
        ignorePantry = foodPreferences.get("ignorePantry").equals("No");

        sendData();
    }

    public void sendData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String ingredients = String.join(",", selectedFoods);
                    URL url = new URL("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/findByIngredients?ingredients=" + URLEncoder.encode(ingredients, "UTF-8")
                            + "&number=" + numberOfRecipes + "&ignorePantry=" + ignorePantry + "&ranking=" + maximizeIngredients);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("x-rapidapi-key", "a2ae691b53msh393e153de705864p186a6cjsnbdf23b779fc9");
                    conn.setRequestProperty("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com");

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        StringBuilder content = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            content.append(inputLine);
                        }
                        in.close();
                        JSONArray jsonArray = new JSONArray(content.toString());
                        runOnUiThread(() -> displayFoodItems(jsonArray));
                    } else {
                        Log.e("API Error", "Response Code: " + responseCode);
                    }
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }
            }
        }).start();
    }

    private void displayFoodItems(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject recipe = jsonArray.getJSONObject(i);
                String title = recipe.getString("title");
                String description = recipe.optString("description", "No description available.");

                // Inflate the card layout
                View cardView = getLayoutInflater().inflate(R.layout.card_layout, null);
                TextView foodName = cardView.findViewById(R.id.foodName);
                TextView foodDescription = cardView.findViewById(R.id.foodDescription);
                TextView favoriteIcon = cardView.findViewById(R.id.favoriteIcon); // Make sure this is a TextView

                foodName.setText(title);
                foodDescription.setText(description);

                // Initially set the heart emoji as unfilled
                favoriteIcon.setText("♡"); // Empty heart
                favoriteIcon.setTag(false); // Tag to track filled state

                // Set click listener for the heart emoji
                favoriteIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isFavorite = (boolean) v.getTag();
                        if (!isFavorite) {
                            ((TextView) v).setText("❤️"); // Filled heart
                            v.setTag(true); // Update tag to filled
                            Toast.makeText(ApiTime.this, title + " has been added to the favorites tab", Toast.LENGTH_SHORT).show();
                        } else {
                            ((TextView) v).setText("♡"); // Back to empty heart
                            v.setTag(false); // Update tag to unfilled
                            Toast.makeText(ApiTime.this, title + " has been removed from favorites", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                foodContainer.addView(cardView);
            } catch (Exception e) {
                Log.e("JSON Parsing Error", e.toString());
            }
        }
    }

    public void selectFood(View view) {
        Intent intent = new Intent(ApiTime.this, scrollFood.class);
        startActivity(intent);
    }
}
