package com.example.cs_topics_project_test.ui.notes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotesFragment : Fragment(R.layout.fragment_notes) {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)

        // Initialize RecyclerView and Adapter
        noteAdapter = NoteAdapter { note ->
            // Navigate to the edit fragment and pass the selected note for editing
            val editNoteFragment = EditNoteFragment.newInstance(note)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_edit_note, editNoteFragment)
                .addToBackStack(null)
                .commit()
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = noteAdapter

        // Initialize ViewModel
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        // Observe LiveData from the ViewModel
        noteViewModel.allNotes.observe(viewLifecycleOwner, Observer { notes ->
            noteAdapter.submitList(notes)
        })

        // Handle FAB click for adding a new note
        fab.setOnClickListener {
            val addNoteFragment = AddNoteFragment() // Fragment for adding a new note
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_add_note, addNoteFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}