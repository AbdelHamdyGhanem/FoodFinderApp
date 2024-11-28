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

    private var AddFood: EditText? = null
    private var arrayList: ArrayList<String>? = null
    private lateinit var adapter: FoodAdapter
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var micButton: ImageButton
    private val RECORD_AUDIO_REQUEST_CODE = 1
    private val PERMISSION_REQUEST_CODE = 2
    private val FILE_NAME = "pantry_items.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AddFood = findViewById(R.id.AddFood)
        val buttonAdd = findViewById<Button>(R.id.button_add)
        val listView = findViewById<ListView>(R.id.listView)
        micButton = findViewById(R.id.micButton) // ImageButton for microphone

        // Initialize Pantry Button
        val buttonPantry = findViewById<Button>(R.id.button_pantry)
        buttonPantry.setOnClickListener {
            val intent = Intent(this, PantryActivity::class.java)
            startActivity(intent)
        }

        val buttonHome = findViewById<Button>(R.id.button_home)
        buttonHome.setOnClickListener {
            // Navigate back to the HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // Initialize arrayList
        arrayList = ArrayList()

        // Initialize the adapter and pass the lambda for onDeleteClick
        adapter = FoodAdapter(this, R.layout.list_item, arrayList!!) { item ->
            removeFood(item)
        }
        listView.adapter = adapter

        // Set up button click listener for adding food
        buttonAdd.setOnClickListener { addFood() }

        // Request storage permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }

        // Set up SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        // Check for microphone permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), RECORD_AUDIO_REQUEST_CODE)
        }

        micButton.setOnClickListener {
            startListening() // Start listening for voice input
        }

        // Button to navigate to ShoppingListActivity
        val buttonShoppingList = findViewById<Button>(R.id.button_shopping_list)
        buttonShoppingList.setOnClickListener {
            val intent = Intent(applicationContext, ShoppingListActivity::class.java)
            startActivity(intent)
        }

        // Handle "Next" button
        val buttonNextFoodPref = findViewById<Button>(R.id.button_next_foodPref)
        buttonNextFoodPref.setOnClickListener {
            // Save items to pantry file
            saveItemsToFile(arrayList!!)
            // Navigate to PantryActivity
            val intent = Intent(this, FoodPrefActivity::class.java)
            startActivity(intent)
        }


    }

    // This method is called when the ActionBar back button is clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish() // Close MainActivity and return to HomeActivity
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addFood() {
        val food = AddFood!!.text.toString().trim()

        if (!food.isBlank() && !arrayList!!.contains(food)) {
            arrayList!!.add(food)
            adapter.notifyDataSetChanged()
            AddFood!!.setText("") // Clear the EditText
            Toast.makeText(this, "Food item added", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Item is empty or already exists", Toast.LENGTH_SHORT).show()
        }
    }

    private fun removeFood(item: String) {
        arrayList!!.remove(item)
        adapter.notifyDataSetChanged() // Notify adapter to refresh the list
    }

    private fun saveItemsToFile(items: List<String>) {
        try {
            val file = File(filesDir, FILE_NAME)
            FileOutputStream(file, false).use { output ->
                items.forEach { item ->
                    output.write("$item\n".toByteArray())
                }
            }
            Toast.makeText(this, "Items saved to pantry!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error saving items: ${e.message}", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this@MainActivity, "Error recognizing speech", Toast.LENGTH_SHORT).show()
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

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy() // Release the recognizer
    }
}
