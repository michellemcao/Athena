package com.example.cs_topics_project_test.task

data class TaskDetail (
    private var taskName: String = "",
    private var taskDescription: String = "",
) {
    private var isCompleted = false

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
        return this.taskName
    }
    fun setTaskName(name: String) {
        this.taskName = name
    }
    fun getTaskDescription(): String {
        return this.taskDescription
    }
    fun setTaskDescription(description: String) {
        this.taskDescription = description
    }

    override fun toString() : String {
        return this.taskName
    }
}