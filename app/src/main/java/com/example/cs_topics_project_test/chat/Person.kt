package com.example.cs_topics_project_test.chat

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Person(
    val name: String,
    val uid: String,
) : Parcelable

