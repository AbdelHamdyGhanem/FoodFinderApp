package com.example.application

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var addFoodEditText: EditText
    private lateinit var micButton: ImageButton
    private lateinit var adapter: FoodAdapter
    private val foodList = ArrayList<String>()
    private lateinit var speechRecognizer: SpeechRecognizer

    companion object {
        private const val RECORD_AUDIO_REQUEST_CODE = 1
        private const val PERMISSION_REQUEST_CODE = 2
        private const val FILE_NAME = "pantry_items.txt"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addFoodEditText = findViewById(R.id.AddFood)
        micButton = findViewById(R.id.micButton)

        val addButton = findViewById<Button>(R.id.button_add)
        val pantryButton = findViewById<Button>(R.id.button_pantry)
        val homeButton = findViewById<Button>(R.id.button_home)
        val shoppingListButton = findViewById<Button>(R.id.button_shopping_list)
        val nextButton = findViewById<Button>(R.id.button_next_foodPref)
        val listView = findViewById<ListView>(R.id.listView)

        // Set up the adapter
        adapter = FoodAdapter(this, R.layout.list_item, foodList) { item ->
            removeFood(item)
        }
        listView.adapter = adapter

        // Add button listener
        addButton.setOnClickListener { addFood() }

        // Pantry button listener
        pantryButton.setOnClickListener {
            startActivity(Intent(this, PantryActivity::class.java))
        }

        // Home button listener
        homeButton.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        // Shopping list button listener
        shoppingListButton.setOnClickListener {
            startActivity(Intent(this, ShoppingListActivity::class.java))
        }

        // Next button listener
        nextButton.setOnClickListener {
            saveItemsToFile(foodList)
            val intent = Intent(this, FoodPrefActivity::class.java).apply {
                putStringArrayListExtra("foods", foodList)
            }
            startActivity(intent)
        }

        // Initialize speech recognizer
        initializeSpeechRecognizer()

        // Microphone button listener
        micButton.setOnClickListener {
            startListening()
        }

        // Request permissions
        requestPermissionsIfNeeded()
    }

    private fun requestPermissionsIfNeeded() {
        val permissionsToRequest = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.RECORD_AUDIO)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), PERMISSION_REQUEST_CODE)
        }
    }

    private fun initializeSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {}

            override fun onError(error: Int) {
                val errorMessage = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                    SpeechRecognizer.ERROR_CLIENT -> "Client-side error"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
                    SpeechRecognizer.ERROR_NETWORK -> "Network error"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                    SpeechRecognizer.ERROR_NO_MATCH -> "No match found"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy"
                    SpeechRecognizer.ERROR_SERVER -> "Server error"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
                    else -> "Unknown error"
                }
                Toast.makeText(this@MainActivity, "Speech recognition error: $errorMessage", Toast.LENGTH_SHORT).show()
            }

            override fun onResults(results: Bundle?) {
                val data = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!data.isNullOrEmpty()) {
                    addFoodEditText.setText(data[0])
                    addFood()
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {}

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    private fun startListening() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
        }
        speechRecognizer.startListening(intent)
    }

    private fun addFood() {
        val food = addFoodEditText.text.toString().trim()
        if (food.isNotBlank() && !foodList.contains(food)) {
            foodList.add(food)
            adapter.notifyDataSetChanged()
            addFoodEditText.text.clear()
            Toast.makeText(this, "Food item added", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Item is empty or already exists", Toast.LENGTH_SHORT).show()
        }
    }

    private fun removeFood(item: String) {
        foodList.remove(item)
        adapter.notifyDataSetChanged()
        Toast.makeText(this, "Item removed", Toast.LENGTH_SHORT).show()
    }

    private fun saveItemsToFile(items: List<String>) {
        try {
            val file = File(filesDir, FILE_NAME)
            file.printWriter().use { writer ->
                items.forEach { item -> writer.println(item) }
            }
            Toast.makeText(this, "Items saved to pantry!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error saving items: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
