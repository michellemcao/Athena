package com.example.cs_topics_project_test.function

class Task (
    private var taskName: String,
    private var dueDate: Date,
    private var dueTime: Time,
    private var taskDescription: String
) :
    TaskOutline {
    private var isCompleted = false

    // return whether task is completed and modify it
    override fun taskCompleted() {
        this.isCompleted = true
    }
    override fun taskNotCompleted() {
        this.isCompleted = false
    }
    override fun isTaskCompleted(): Boolean {
        return this.isCompleted
    }

    // return task name/description and modify it
    override fun getTaskName(): String {
        return this.taskName
    }
    override fun setTaskName(name: String) {
        this.taskName = name
    }
    override fun getTaskDescription(): String {
        return this.taskDescription
    }
    override fun setTaskDescription(description: String) {
        this.taskDescription = description
    }

    // set due date/time and modify it
    override fun getDueDate(): Date {
        return this.dueDate
    }
    override fun setDueDate(date: Date) {
        this.dueDate = date
    }
    override fun getDueTime(): Time {
        return this.dueTime
    }
    override fun setDueTime(time: Time) {
        this.dueTime = time
    }
}