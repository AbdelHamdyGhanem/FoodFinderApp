package com.example.application

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ShoppingListActivity : AppCompatActivity() {

    private var AddFood: EditText? = null
    private var foodList: MutableList<String> = mutableListOf()
    private lateinit var adapter: FoodAdapter
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var micButton: ImageButton
    private val RECORD_AUDIO_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        AddFood = findViewById(R.id.AddFood)
        val buttonAdd = findViewById<Button>(R.id.button_add)
        val listView = findViewById<ListView>(R.id.listView)
        micButton = findViewById(R.id.micButton)
        val backButton = findViewById<Button>(R.id.backButton)

        // Initialize FoodAdapter with onDeleteClick lambda function
        adapter = FoodAdapter(this, R.layout.list_item, foodList) { item ->
            removeFood(item)
        }
        listView.adapter = adapter

        // Load the shopping list from SharedPreferences when the activity is created
        loadShoppingList()

        // Set up button click listener for adding food
        buttonAdd.setOnClickListener { addFood() }

        // Set up SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        // Check for microphone permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), RECORD_AUDIO_REQUEST_CODE)
        }

        micButton.setOnClickListener {
            startListening()
        }

        // Set up Back button click listener
        backButton.setOnClickListener {
            finish() // Go back to the previous activity
        }

        // Set up item click listener to delete food from the list
        listView.setOnItemClickListener { _, _, position, _ ->
            val item = foodList[position]
            removeFood(item)
        }
    }

    private fun addFood() {
        val food = AddFood!!.text.toString().trim()

        if (food.isNotBlank() && !foodList.contains(food)) {
            foodList.add(food)
            adapter.notifyDataSetChanged()
            AddFood!!.setText("")
            saveShoppingList()
            Toast.makeText(this, "Food item added", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Item is empty or already exists", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startListening() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {}

            override fun onError(error: Int) {
                Toast.makeText(this@ShoppingListActivity, "Error recognizing speech", Toast.LENGTH_SHORT).show()
            }

            override fun onResults(results: Bundle?) {
                val data = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!data.isNullOrEmpty()) {
                    AddFood!!.setText(data[0])
                    addFood()
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {}

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        speechRecognizer.startListening(intent)
    }

    // Save shopping list to SharedPreferences
    private fun saveShoppingList() {
        val sharedPreferences = getSharedPreferences("ShoppingListPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val foodListString = foodList.joinToString(",")
        editor.putString("shopping_list", foodListString)
        editor.apply()
    }

    // Load shopping list from SharedPreferences
    private fun loadShoppingList() {
        val sharedPreferences = getSharedPreferences("ShoppingListPrefs", MODE_PRIVATE)
        val foodListString = sharedPreferences.getString("shopping_list", "") ?: ""

        if (foodListString.isNotEmpty()) {
            foodList.clear()
            foodList.addAll(foodListString.split(","))
        }

        adapter.notifyDataSetChanged()
    }

    private fun removeFood(item: String) {
        foodList.remove(item)
        adapter.notifyDataSetChanged() // Notify adapter to refresh the list
        saveShoppingList() // Save the updated list to SharedPreferences
        Toast.makeText(this, "Food item removed", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }
}