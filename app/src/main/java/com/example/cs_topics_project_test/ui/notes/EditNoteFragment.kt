package com.example.cs_topics_project_test.ui.notes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.cs_topics_project_test.R

class EditNoteFragment : Fragment(R.layout.fragment_edit_note) {

    companion object {
        private const val ARG_NOTE = "note"

        fun newInstance(note: Note): EditNoteFragment {
            val fragment = EditNoteFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARG_NOTE, note)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var note: Note

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the note passed from the argument
        note = arguments?.getParcelable(ARG_NOTE) ?: return

        // Use the note object to populate your UI, such as EditText for title/content
    }
}
