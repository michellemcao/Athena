package com.example.cs_topics_project_test.notes

import android.content.Intent
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


/*class NotesFragment : Fragment() {

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
}*/
class NotesFragment : Fragment() {

    private lateinit var notesAdapter: NotesAdapter
    private val notesList = mutableListOf<Note>() // You can later use NoteManager.notes for shared data
    private val EDIT_NOTE_REQUEST = 1
    private val NEW_NOTE_REQUEST = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_notes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Add some dummy notes
        notesList.add(Note("First Note", "This is a test"))
        notesList.add(Note("Second Note", "Another test note"))

        // Pass click lambda to adapter
        notesAdapter = NotesAdapter(notesList) { note, position ->
            // Open editor to edit note
            val intent = Intent(requireContext(), NoteEditorActivity::class.java)
            intent.putExtra("title", note.title)
            intent.putExtra("content", note.content)
            intent.putExtra("position", position)
            startActivityForResult(intent, EDIT_NOTE_REQUEST)
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewNoteView)
        recyclerView.adapter = notesAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val buttonNewNote: Button = view.findViewById(R.id.buttonNewNoteToggle)
        buttonNewNote.setOnClickListener {
            // Open editor for a new note (empty fields)
            val intent = Intent(requireContext(), NoteEditorActivity::class.java)
            startActivityForResult(intent, NEW_NOTE_REQUEST)
        }
    }

    // Handle returned result from NoteEditorActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != android.app.Activity.RESULT_OK || data == null) return

        val title = data.getStringExtra("title") ?: return
        val content = data.getStringExtra("content") ?: return

        when (requestCode) {
            EDIT_NOTE_REQUEST -> {
                val position = data.getIntExtra("position", -1)
                if (position != -1) {
                    notesList[position] = Note(title, content)
                    notesAdapter.notifyItemChanged(position)
                }
            }
            NEW_NOTE_REQUEST -> {
                notesList.add(Note(title, content))
                notesAdapter.notifyItemInserted(notesList.size - 1)
            }
        }
    }
}

