package com.example.cs_topics_project_test.task

import com.example.cs_topics_project_test.function.Date
import com.example.cs_topics_project_test.function.DateAndTime
import com.example.cs_topics_project_test.function.DateCompleted
import com.example.cs_topics_project_test.function.Time
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

// @Parcelize
data class TaskStore (val taskName: String = "",
                      val taskDescription: String = "",
                      val dueDateAndTime: Long = 0L,
                      // val isCompleted: Boolean = false,
                      // val completedDate: Long?
) // : Parcelable


