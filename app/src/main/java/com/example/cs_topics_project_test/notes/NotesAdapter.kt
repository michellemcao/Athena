package com.example.cs_topics_project_test.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R

/*// Define an Adapter for Notes
class NotesAdapter(private var notesList: MutableList<Note>) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val noteTitle: TextView = view.findViewById(R.id.noteTitle)
        val noteContent: TextView = view.findViewById(R.id.noteContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notesList[position]
        holder.noteTitle.text = note.title
        holder.noteContent.text = note.content
    }

    override fun getItemCount(): Int = notesList.size

    fun updateList(newList: MutableList<Note>) {
        notesList = newList
        notifyDataSetChanged()
    }
}*/

class NotesAdapter(
    private var notesList: MutableList<Note>,
    private val onItemClick: (Note, Int) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val noteTitle: TextView = view.findViewById(R.id.noteTitle)
        val noteContent: TextView = view.findViewById(R.id.noteContent)

        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(notesList[position], position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notesList[position]
        holder.noteTitle.text = note.title
        holder.noteContent.text = note.content
    }

    override fun getItemCount(): Int = notesList.size

    fun updateList(newList: MutableList<Note>) {
        notesList = newList
        notifyDataSetChanged()
    }
}


