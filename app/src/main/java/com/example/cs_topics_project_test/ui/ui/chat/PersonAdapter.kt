package com.example.cs_topics_project_test.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R


// Adapter for RecyclerView
class PersonAdapter(
    private val personList: List<Person>,
    private val onClick: (Person) -> Unit
) : RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    // ViewHolder class for each item in the list
    inner class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.personNameTextView)

        // Set up the click listener for each item
        fun bind(person: Person) {
            nameTextView.text = person.name
            itemView.setOnClickListener {
                onClick(person)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_person, parent, false) // Inflate the item layout
        return PersonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(personList[position]) // Bind the data to the view
    }

    override fun getItemCount(): Int {
        return personList.size // Return the number of items in the list
    }
}
