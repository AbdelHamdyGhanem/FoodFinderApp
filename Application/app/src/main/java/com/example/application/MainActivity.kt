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

class MainActivity : AppCompatActivity() {

    private var AddFood: EditText? = null
    private var arrayList: ArrayList<String>? = null
    private lateinit var adapter: FoodAdapter
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var micButton: ImageButton
    private val RECORD_AUDIO_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AddFood = findViewById(R.id.AddFood)
        val buttonAdd = findViewById<Button>(R.id.button_add)
        val listView = findViewById<ListView>(R.id.listView)
        micButton = findViewById(R.id.micButton) // ImageButton for microphone

        // Initialize arrayList
        arrayList = ArrayList()

        // Initialize the adapter and pass the lambda for onDeleteClick
        adapter = FoodAdapter(this, R.layout.list_item, arrayList!!) { item ->
            removeFood(item)
        }
        listView.adapter = adapter

        // Set up button click listener for adding food
        buttonAdd.setOnClickListener { addFood() }

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

        val buttonNextFoodPref = findViewById<Button>(R.id.button_next_foodPref)
        buttonNextFoodPref.setOnClickListener {
            val intent = Intent(applicationContext, FoodPrefActivity::class.java)
            intent.putStringArrayListExtra("foods", arrayList)
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
