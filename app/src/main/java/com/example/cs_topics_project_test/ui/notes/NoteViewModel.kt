package com.example.cs_topics_project_test.ui.notes

import android.app.Application
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.cs_topics_project_test.ui.notes.NoteDao
import com.example.cs_topics_project_test.ui.notes.Note


class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val noteDao: NoteDao = NoteDatabase.getDatabase(application).noteDao()
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    // Method to insert a new note
    fun insert(note: Note) {
        viewModelScope.launch {
            noteDao.insert(note)
        }
    }

    // Method to delete a note
    fun delete(note: Note) {
        viewModelScope.launch {
            noteDao.delete(note)
        }
    }
}