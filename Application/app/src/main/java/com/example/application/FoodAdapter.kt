package com.example.application

import android.content.Context
import android.system.Os
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView

class FoodAdapter(context: Context, resource: Int, objects: MutableList<String>) :
    ArrayAdapter<String>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item, parent, false)

        val itemName: TextView = view.findViewById(R.id.itemName)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)

        val item = getItem(position)
        itemName.text = item

        deleteButton.setOnClickListener {
            remove(item) // Remove the item from the adapter's data
            notifyDataSetChanged() // Notify the adapter that the data has changed
        }

        return view
    }
}