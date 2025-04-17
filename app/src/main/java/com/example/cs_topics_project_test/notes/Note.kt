package com.example.cs_topics_project_test.notes

/*import com.example.cs_topics_project_test.function.Date
import com.example.cs_topics_project_test.function.Time
import com.example.cs_topics_project_test.function.DateAndTime

// Note class represents a note in the system.
data class Note (
    // Properties to store note details
    private var noteTitle: String,
    private var noteContent: String,
    private var creationDate: Date,
    private var creationTime: Time,
    private var lastModifiedDate: Date = creationDate,  // Default to creation date initially
    private var lastModifiedTime: Time = creationTime   // Default to creation time initially
) : Comparable<Note> {

    // Getter methods for note details:
    fun getNoteTitle(): String {
        return this.noteTitle  // Returns note title
    }

    fun getNoteContent(): String {
        return this.noteContent  // Returns note content
    }

    /*fun getNoteDetail(): NoteDetail {
        return NoteDetail(noteTitle, noteContent)  // Returns NoteDetail object with title and content
    }*/
    fun getNoteDetail(): Pair<String, String> {
        return Pair(noteTitle, noteContent)  // Returns title and content as a Pair
    }


    fun getCreationDate(): Date {
        return this.creationDate  // Returns creation date
    }

    fun getCreationTime(): Time {
        return this.creationTime  // Returns creation time
    }

    fun getLastModifiedDate(): Date {
        return this.lastModifiedDate  // Returns last modified date
    }

    fun getLastModifiedTime(): Time {
        return this.lastModifiedTime  // Returns last modified time
    }

    // Method to update the content of the note and modify last modified date/time:
    fun updateContent(newContent: String, newDate: Date, newTime: Time) {
        this.noteContent = newContent  // Updates note content
        this.lastModifiedDate = newDate  // Updates last modified date
        this.lastModifiedTime = newTime  // Updates last modified time
    }

    // Comparable method to compare notes based on creation date/time
    override fun compareTo(other: Note): Int {
        val dCompare = this.creationDate.compareTo(other.creationDate)  // Compare creation dates
        val tCompare = this.creationTime.compareTo(other.creationTime)  // Compare creation times
        if (dCompare == 0) {
            if (tCompare == 0) return 0  // If both creation date and time are the same
            else if (tCompare == 1) return 1  // If this note was created later
        } else if (dCompare == 1) return 1  // If this note was created later
        return -1  // Otherwise, the other note was created earlier
    }

    // Companion object with comparators for sorting notes:
    companion object {
        // Sort by note title (ascending order)
        val noteTitleComparatorAscending = Comparator<Note> { n1, n2 -> n1.getNoteTitle().compareTo(n2.getNoteTitle()) }

        // Sort by note title (descending order)
        val noteTitleComparatorDescending = Comparator<Note> { n1, n2 -> n2.getNoteTitle().compareTo(n1.getNoteTitle()) }

        // Sort by creation date/time (ascending order)
        val noteCreationComparatorAscending = Comparator<Note> { n1, n2 -> n1.compareTo(n2) }

        // Sort by creation date/time (descending order)
        val noteCreationComparatorDescending = Comparator<Note> { n1, n2 -> n2.compareTo(n1) }
    }
}*/

data class Note(
    val title: String,
    val content: String
)

