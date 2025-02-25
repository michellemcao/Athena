package com.example.cs_topics_project_test.ui.notes

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NoteDao {

    @Insert
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM notes")  // Assuming the table name is 'notes'
    fun getAllNotes(): LiveData<List<Note>>
}
