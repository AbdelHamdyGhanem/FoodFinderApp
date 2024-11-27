package com.example.application

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlin.math.*

class ShoppingListActivity : AppCompatActivity() {

    private var AddFood: EditText? = null
    private var foodList: MutableList<String> = mutableListOf()
    private lateinit var adapter: FoodAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val RADIUS_OF_EARTH_KM = 6371
    private val DISTANCE_THRESHOLD_KM = 1

    private val groceryStores = listOf(
        GroceryStore("Sobeys Regent St", 45.961196, -66.639694),
        GroceryStore("Atlantic Superstore Smythe Street", 45.957962, -66.643322),
        GroceryStore("Walmart Supercentre", 45.936975, -66.634032),
        GroceryStore("Sobeys Uptown Centre", 45.944248, -66.656724)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        AddFood = findViewById(R.id.AddFood)
        val buttonAdd = findViewById<Button>(R.id.button_add)
        val listView = findViewById<ListView>(R.id.listView)
        val backButton = findViewById<Button>(R.id.backButton)

        adapter = FoodAdapter(this, R.layout.list_item, foodList) { item -> removeFood(item) }
        listView.adapter = adapter
        loadShoppingList()

        buttonAdd.setOnClickListener { addFood() }
        backButton.setOnClickListener { finish() }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermission()

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

        if (foodList.isNotEmpty()) {
            checkUserProximity()
        }
    }

    private fun checkUserProximity() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val userLat = location.latitude
                val userLng = location.longitude

                // Log the user's location
                println("User's Location: Lat=$userLat, Lng=$userLng")

                // Check each store for proximity
                groceryStores.forEach { store ->
                    val distance = calculateDistance(userLat, userLng, store.latitude, store.longitude)
                    if (distance <= DISTANCE_THRESHOLD_KM) {
                        sendNotification(store.name)
                    }
                }
            }
        }
    }

    private fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val latDistance = Math.toRadians(lat2 - lat1)
        val lngDistance = Math.toRadians(lng2 - lng1)

        val a = sin(latDistance / 2).pow(2.0) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(lngDistance / 2).pow(2.0)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return RADIUS_OF_EARTH_KM * c
    }

    private fun sendNotification(storeName: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "GroceryStoreReminder"
        val channelName = "Grocery Store Notifications"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, ShoppingListActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Nearby Grocery Store Alert")
            .setContentText("You're near $storeName. Don't forget your groceries!")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(storeName.hashCode(), notification)
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 2)
        }
    }

    private fun saveShoppingList() {
        val sharedPreferences = getSharedPreferences("ShoppingListPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val foodListString = foodList.joinToString(",")
        editor.putString("shopping_list", foodListString)
        editor.apply()
    }

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
        adapter.notifyDataSetChanged()
        saveShoppingList()
        Toast.makeText(this, "Food item removed", Toast.LENGTH_SHORT).show()
    }

    data class GroceryStore(val name: String, val latitude: Double, val longitude: Double)
}