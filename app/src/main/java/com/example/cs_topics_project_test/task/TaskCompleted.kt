package com.example.cs_topics_project_test.task

import com.example.cs_topics_project_test.function.Date
import com.example.cs_topics_project_test.function.DateAndTime
import com.example.cs_topics_project_test.function.Time

data class TaskCompleted (
    private val taskCompletedDate: DateAndTime, //TaskCompleted
    private val taskDue: DateAndTime,
    private val taskDetail: TaskDetail) : TaskOutline {
    // private val taskCompletedDate: Date = TaskManager.todayDate
    private var isCompleted = true

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
        return this.taskDetail.getTaskName()
    }
    override fun getTaskDescription(): String {
        return this.taskDetail.getTaskDescription()
    }
    override fun getTaskDetail(): TaskDetail {
        return taskDetail
    }

    // set due date/time and modify it
    override fun getDueDate(): Date {
        return this.taskDue.getDate()
    }
    override fun getDueTime(): Time {
        return this.taskDue.getTime()
    }
    override fun getDateAndTime(): DateAndTime {
        return taskDue
    }
    fun getTaskCompletedDate(): DateAndTime {
        return this.taskCompletedDate
    }
}