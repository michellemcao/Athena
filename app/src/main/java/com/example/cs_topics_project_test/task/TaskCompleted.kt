package com.example.cs_topics_project_test.task

import com.example.cs_topics_project_test.function.Date
import com.example.cs_topics_project_test.function.DateAndTime
import com.example.cs_topics_project_test.function.Time

data class TaskCompleted (
    private val taskCompletedDate: Date, //TaskCompleted
    private val taskDue: DateAndTime,
    private val taskDetail: TaskDetail
) {
    // private val taskCompletedDate: Date = TaskManager.todayDate
    private var isCompleted = true

    // return whether task is completed and modify it
    fun taskCompleted() {
        this.isCompleted = true
    }
    fun taskNotCompleted() {
        this.isCompleted = false
    }
    fun isTaskCompleted(): Boolean {
        return this.isCompleted
    }

    // return task name/description and modify it
    fun getTaskName(): String {
        return this.taskDetail.getTaskName()
    }
    fun getTaskDescription(): String {
        return this.taskDetail.getTaskDescription()
    }

    // set due date/time and modify it
    fun getDueDate(): Date {
        return this.taskDue.getDate()
    }
    fun getDueTime(): Time {
        return this.taskDue.getTime()
    }

    fun getTaskCompletedDate(): Date {
        return this.taskCompletedDate
    }
}