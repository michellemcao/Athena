package com.example.cs_topics_project_test.ui.notes

import android.os.Parcel
import android.os.Parcelable

// Define the Note class with both regular and Parcelable constructor
data class Note(
    val id: Int = 0,         // Optional default value
    val title: String = "",  // Optional default value
    val content: String = "" // Optional default value
) : Parcelable {

    // Parcelable constructor
    constructor(parcel: Parcel) : this(
        parcel.readInt(),           // Read id from parcel
        parcel.readString() ?: "",  // Read title from parcel, default to "" if null
        parcel.readString() ?: ""   // Read content from parcel, default to "" if null
    )

    // Write the object's properties to a Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)         // Write id to parcel
        parcel.writeString(title)   // Write title to parcel
        parcel.writeString(content) // Write content to parcel
    }

    override fun describeContents(): Int = 0

    // Companion object to create and retrieve the Note from Parcel
    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Note> = object : Parcelable.Creator<Note> {
            override fun createFromParcel(parcel: Parcel): Note {
                return Note(parcel)  // Use the Parcelable constructor to recreate the object
            }

            override fun newArray(size: Int): Array<Note?> {
                return arrayOfNulls(size)
            }
        }
    }
}
