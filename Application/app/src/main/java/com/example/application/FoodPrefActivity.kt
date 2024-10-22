package com.example.application

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class FoodPrefActivity : AppCompatActivity() {
    private var numberofRecipes: EditText? = null
    private var maximizeIngredients: Switch? = null
    private var ignorePantry: Switch? = null
    private var arrayList: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        val button_next_API = findViewById<Button>(R.id.button_next_API)
        val button_previous = findViewById<Button>(R.id.button_prev_foodPref)

        numberofRecipes = findViewById(R.id.number_of_recipes)
        maximizeIngredients = findViewById(R.id.maximizeIngredients)
        ignorePantry = findViewById(R.id.ignorePantry)
        arrayList = intent.getStringArrayListExtra("foods")

        // pass data
        button_next_API.setOnClickListener { passDataToApiTime() }

        button_previous.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun passDataToApiTime() {
        val numberOfRecipes = numberofRecipes!!.text.toString()
        val maximizeIngredients = maximizeIngredients!!.isChecked
        val ignorePantry = ignorePantry!!.isChecked

        // Use HashMap with specific types
        val preferences: HashMap<String, Any> = HashMap()
        preferences["numberOfRecipes"] = numberOfRecipes
        preferences["maximizeIngredients"] = maximizeIngredients
        preferences["ignorePantry"] = ignorePantry

        setContentView(R.layout.activity_timeout)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ApiTime::class.java)
            intent.putExtra("foods", arrayList)
            intent.putExtra("preferences", preferences)
            startActivity(intent)
        }, 3000)
    }

}
