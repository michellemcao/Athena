package com.example.cs_topics_project_test.notes

object NoteManager {
    val notes: MutableList<Note> = mutableListOf() // Active notes
    val archivedNotes: MutableList<Note> = mutableListOf() // Archived notes
}
