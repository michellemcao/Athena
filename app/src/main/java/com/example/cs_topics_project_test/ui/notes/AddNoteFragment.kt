package com.example.cs_topics_project_test.ui.notes

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cs_topics_project_test.R

class AddNoteFragment : Fragment(R.layout.fragment_add_note) {

    private lateinit var noteViewModel: NoteViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteViewModel = ViewModelProvider(requireActivity()).get(NoteViewModel::class.java)

        val titleEditText = view.findViewById<EditText>(R.id.editTextTitle)
        val contentEditText = view.findViewById<EditText>(R.id.editTextContent)
        val saveButton = view.findViewById<Button>(R.id.saveButton)

        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val newNote = Note(title = title, content = content)
                noteViewModel.insert(newNote)
                parentFragmentManager.popBackStack() // Go back to the NotesFragment
            } else {
                Toast.makeText(context, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}