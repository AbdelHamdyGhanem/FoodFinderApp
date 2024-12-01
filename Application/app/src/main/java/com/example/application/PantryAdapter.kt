package com.example.application

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.BaseAdapter

class PantryAdapter(
    private val context: Context,
    private val items: MutableList<String>,
    private val onDeleteClick: (String) -> Unit
) : BaseAdapter() {

    // Return the number of items in the list
    override fun getCount(): Int = items.size

    // Return the item at a specific position
    override fun getItem(position: Int): String = items[position]

    // Return the item ID (can just be the position here)
    override fun getItemId(position: Int): Long = position.toLong()

    // Create or reuse a view for each item in the list
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // Inflate the list_item_pantry layout if no reusable view is available
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_pantry, parent, false)

        // Get references to the TextView and ImageButton
        val itemText = view.findViewById<TextView>(R.id.pantryItemText)
        val deleteButton = view.findViewById<ImageButton>(R.id.deleteButton)

        // Set the text for the pantry item
        val item = items[position]
        itemText.text = item

        // Set the click listener for the delete button
        deleteButton.setOnClickListener {
            onDeleteClick(item) // Call the callback function to handle deletion
        }

        return view
    }
}
