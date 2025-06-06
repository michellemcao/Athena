package com.example.cs_topics_project_test.notes


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.cs_topics_project_test.R

class NoteEditorActivity : AppCompatActivity() {

    private lateinit var editTitle: EditText
    private lateinit var editContent: EditText
    private var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_editor)

        editTitle = findViewById(R.id.editTitle)
        editContent = findViewById(R.id.editContent)
        val buttonSave: Button = findViewById(R.id.buttonSave)

        // Load existing note data if editing
        val intent = intent
        editTitle.setText(intent.getStringExtra("title") ?: "")
        editContent.setText(intent.getStringExtra("content") ?: "")
        position = intent.getIntExtra("position", -1)

        buttonSave.setOnClickListener {
            val title = editTitle.text.toString()
            val content = editContent.text.toString()

            val resultIntent = Intent()
            resultIntent.putExtra("title", title)
            resultIntent.putExtra("content", content)
            resultIntent.putExtra("position", position)
            setResult(Activity.RESULT_OK, resultIntent)
            finish() // Close this activity and return to NotesFragment
        }
    }
}
