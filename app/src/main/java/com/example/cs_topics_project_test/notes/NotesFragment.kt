package com.example.cs_topics_project_test.notes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NotesFragment : Fragment() {

    private lateinit var notesAdapter: NotesAdapter
    private val notesList = mutableListOf<Note>()

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val EDIT_NOTE_REQUEST = 1
    private val NEW_NOTE_REQUEST = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_notes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notesAdapter = NotesAdapter(notesList) { note, position ->
            val intent = Intent(requireContext(), NoteEditorActivity::class.java)
            intent.putExtra("title", note.title)
            intent.putExtra("content", note.content)
            intent.putExtra("position", position)
            intent.putExtra("docId", note.docId)

            startActivityForResult(intent, EDIT_NOTE_REQUEST)
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewNoteView)
        recyclerView.adapter = notesAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val buttonNewNote: Button = view.findViewById(R.id.buttonNewNoteToggle)
        buttonNewNote.setOnClickListener {
            startActivityForResult(Intent(requireContext(), NoteEditorActivity::class.java), NEW_NOTE_REQUEST)
        }

        loadNotesFromFirestore()
    }

    private fun loadNotesFromFirestore() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("users")
            .document(userId)
            .collection("notes")
            .get()
            .addOnSuccessListener { snapshot ->
                notesList.clear()
                for (doc in snapshot.documents) {
                    val note = Note(
                        title = doc.getString("title") ?: "",
                        content = doc.getString("content") ?: "",
                        docId = doc.id
                    )
                    notesList.add(note)
                }
                notesAdapter.updateList(notesList)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load notes", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || data == null) return

        val title = data.getStringExtra("title") ?: return
        val content = data.getStringExtra("content") ?: return
        val userId = auth.currentUser?.uid ?: return
        val docId = data.getStringExtra("docId")

        when (requestCode) {
            NEW_NOTE_REQUEST -> {
                loadNotesFromFirestore() // Just reload the updated list — no second write needed
            }

            EDIT_NOTE_REQUEST -> {
                if (!docId.isNullOrEmpty()) {
                    db.collection("users")
                        .document(userId)
                        .collection("notes")
                        .document(docId)
                        .update(
                            mapOf(
                                "title" to title,
                                "content" to content,
                                "timestamp" to System.currentTimeMillis()
                            )
                        )
                        .addOnSuccessListener {
                            loadNotesFromFirestore()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Failed to update note", Toast.LENGTH_SHORT).show()
                        }
                }

            }
        }
    }
}
