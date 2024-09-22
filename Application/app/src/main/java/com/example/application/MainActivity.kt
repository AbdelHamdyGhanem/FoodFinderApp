package com.example.application

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    // Initialize variables
    private var AddFood: EditText? = null
    private var arrayList: ArrayList<String>? = null
    private var adapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AddFood = findViewById(R.id.AddFood)
        val buttonAdd = findViewById<Button>(R.id.button_add)
        val listView = findViewById<ListView>(R.id.listView)

        arrayList = ArrayList()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList!!)
        listView.adapter = adapter

        // Set up button click listener
        buttonAdd.setOnClickListener { v: View? -> addFood() }

        val button_next_foodPref = findViewById<Button>(R.id.button_next_foodPref)
        button_next_foodPref.setOnClickListener {
            val intent = Intent(applicationContext, FoodPrefActivity::class.java)
            // Pass the list of foods to FoodPrefActivity
            intent.putStringArrayListExtra("foods", arrayList)
            startActivity(intent)
        }
    }

    private fun addFood() {
        val food = AddFood!!.text.toString().trim { it <= ' ' }

        if (!food.isEmpty() && !arrayList!!.contains(food)) {
            arrayList!!.add(food)
            adapter!!.notifyDataSetChanged()
            AddFood!!.setText("")
            Toast.makeText(this, "Food item added", Toast.LENGTH_SHORT).show()
        } else if (food === " ") {
            Toast.makeText(this, "Item is empty or already exists", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }
}
