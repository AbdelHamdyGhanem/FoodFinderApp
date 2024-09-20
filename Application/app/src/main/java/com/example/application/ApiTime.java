package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ApiTime extends AppCompatActivity {

    private TextView textView;
    private StringBuilder displayStringBuilder;
    private String numberOfRecipes;
    private int maximizeIngredients;
    private boolean ignorePantry;
    private ArrayList<String> selectedFoods;
    private HashMap<String, Object> foodPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_api_time);

        Button button_home = findViewById(R.id.button_home);
        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        selectedFoods = intent.getStringArrayListExtra("foods");
        foodPreferences = (HashMap<String, Object>) intent.getSerializableExtra("preferences");

        textView = findViewById(R.id.showData);

        displayStringBuilder = new StringBuilder("Selected Foods:\n");
        if (selectedFoods != null && !selectedFoods.isEmpty()) {
            for (String food : selectedFoods) {
                displayStringBuilder.append(food).append("\n");
            }
        } else {
            displayStringBuilder.append("No foods selected.\n");
        }

        displayStringBuilder.append("\nFood Preferences:\n");

        if (foodPreferences != null) {
            numberOfRecipes = (String) foodPreferences.get("numberOfRecipes");
            displayStringBuilder.append("Number of Recipes: ").append(numberOfRecipes).append("\n");

            maximizeIngredients = foodPreferences.get("maximizeIngredients").equals("Yes") ? 2 : 1;
            ignorePantry = foodPreferences.get("ignorePantry").equals("No");

            displayStringBuilder.append("Maximize Ingredients: ").append(maximizeIngredients).append("\n");
            displayStringBuilder.append("Ignore Pantry: ").append(ignorePantry ? "No" : "Yes").append("\n\n--------------------------------------------------------------------\n");
        }

        String data = sendData();
    }

    public String sendData() {
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
                        for (int i = 0; i < Integer.parseInt(numberOfRecipes); i++) {
                            JSONObject recipe = jsonArray.getJSONObject(i);
                            String title = recipe.getString("title");
                            JSONArray usedIngredients = recipe.getJSONArray("usedIngredients");
                            displayStringBuilder.append("Recipe:\nTitle: ").append(title).append("\n\n");
                        }
                    } else {
                        displayStringBuilder.append("Fail 1");
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(displayStringBuilder.toString());
                        }
                    });
                } catch (Exception e) {
                    displayStringBuilder.append(e.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(displayStringBuilder.toString());
                        }
                    });
                }
            }
        }).start();

        return " ";
    }

    // Add this method for the "Next" button click
    public void selectFood(View view) {
        Intent intent = new Intent(ApiTime.this, scrollFood.class);
        startActivity(intent);
    }
}
