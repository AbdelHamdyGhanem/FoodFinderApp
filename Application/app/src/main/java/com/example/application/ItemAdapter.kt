package com.example.application

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import java.io.File

class ItemAdapter(
    private val context: Context,
    private val items: MutableList<String>,
    private val fileName: String
) : ArrayAdapter<String>(context, R.layout.list_item, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = convertView ?: inflater.inflate(R.layout.list_item, parent, false)

        // Bind data
        val itemName = rowView.findViewById<TextView>(R.id.itemName)
        val deleteButton = rowView.findViewById<ImageButton>(R.id.deleteButton)

        itemName.text = items[position]

        // Set click listener on the delete button
        deleteButton.setOnClickListener {
            val itemToDelete = items[position]
            items.removeAt(position)
            deleteItemFromFile(itemToDelete)
            notifyDataSetChanged()
        }

        return rowView
    }

    private fun deleteItemFromFile(item: String) {
        try {
            // Read all lines from the file
            val file = File(context.filesDir, fileName)
            if (!file.exists()) {
                return
            }

            val lines = file.readLines().toMutableList()

            // Remove the item from the lines
            if (lines.remove(item)) {
                // Write the updated list back to the file
                file.writeText(lines.joinToString("\n"))
            } else {
            }
        } catch (e: Exception) {
        }
    }
}