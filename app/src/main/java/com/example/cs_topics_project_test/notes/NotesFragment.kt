package com.example.cs_topics_project_test.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.notes.Note
import com.example.cs_topics_project_test.notes.NotesAdapter


class NotesFragment : Fragment() {

    private lateinit var notesAdapter: NotesAdapter
    private val notesList = mutableListOf<Note>() // No NoteManager, just local list

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_notes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Temporary dummy notes
        notesList.add(Note("First Note", "This is a test"))
        notesList.add(Note("Second Note", "Another test note"))

        notesAdapter = NotesAdapter(notesList)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewNoteView)
        recyclerView.adapter = notesAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val buttonNewNote: Button = view.findViewById(R.id.buttonNewNoteToggle)
        buttonNewNote.setOnClickListener {
            // For now just add a new note (later open a new screen)
            val newNote = Note("New Note", "Content goes here")
            notesList.add(newNote)
            notesAdapter.notifyItemInserted(notesList.size - 1)
        }
    }
}
