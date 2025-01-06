package com.example.cs_topics_project_test.function

interface TaskOutline {
    fun getTaskName(): String // the name of the task
    fun getDueDate(): Date // the due date
    fun getDueTime(): Time // the due time
    fun isTaskCompleted(): Boolean // returns whether the task is complete
    fun getTaskDescription(): String // returns task description

    // Modifiers
    fun setTaskName(name: String) // resets the task name
    fun setDueDate(date: Date) // changes due date
    fun setDueTime(time: Time) // changes the time that tasks was due
    fun taskCompleted() // sets task to completed
    fun taskNotCompleted() // sets task to incomplete
    fun setTaskDescription(description: String) // resets the task description
}