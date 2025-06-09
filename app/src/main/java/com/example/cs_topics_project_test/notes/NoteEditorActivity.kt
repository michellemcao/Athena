package com.example.cs_topics_project_test.notes

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.themes.ThemeManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class NoteEditorActivity : AppCompatActivity() {

    private lateinit var editTitle: EditText
    private lateinit var editContent: EditText
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var docId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_editor)

        editTitle = findViewById(R.id.editTitle)
        editContent = findViewById(R.id.editContent)
        val buttonSave: Button = findViewById(R.id.buttonSave)

        // val color = ContextCompat.getColor(this, ThemeManager.currentThemeColors!!.toolbar)
        val color = ThemeManager.currentThemeColors!!.toolbar
        editTitle.backgroundTintList = ColorStateList.valueOf(color)
        editContent.backgroundTintList = ColorStateList.valueOf(color)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()


        // Load existing note data if editing
        val intent = intent
        docId = intent.getStringExtra("docId") // get docId if editing
        editTitle.setText(intent.getStringExtra("title") ?: "")
        editContent.setText(intent.getStringExtra("content") ?: "")

        buttonSave.setOnClickListener {
            val title = editTitle.text.toString().trim()
            val content = editContent.text.toString().trim()

            if (title.isEmpty() && content.isEmpty()) {
                Toast.makeText(this, "Cannot save empty note", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = auth.currentUser?.uid
            if (userId == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val note = hashMapOf(
                "title" to title,
                "content" to content,
                "timestamp" to Date()
            )

            val notesRef = db.collection("users")
                .document(userId)
                .collection("notes")

            val resultIntent = Intent()
            resultIntent.putExtra("title", title)
            resultIntent.putExtra("content", content)

            if (docId == null) {
                // NEW note
                notesRef.add(note)
                    .addOnSuccessListener { docRef ->
                        resultIntent.putExtra("docId", docRef.id)
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to save note", Toast.LENGTH_SHORT).show()
                    }
            } else {
                // EDITING existing note
                notesRef.document(docId!!).set(note)
                    .addOnSuccessListener {
                        resultIntent.putExtra("docId", docId)
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to update note", Toast.LENGTH_SHORT).show()
                    }
            }
        }

    }
}
