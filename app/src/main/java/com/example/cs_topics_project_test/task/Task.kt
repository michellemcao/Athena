package com.example.cs_topics_project_test.task

import com.example.cs_topics_project_test.function.Date
import com.example.cs_topics_project_test.function.DateAndTime
import com.example.cs_topics_project_test.function.Time

data class Task (
    private var taskName: String,
    private var taskDescription: String,
    private var dueDate: Date,
    private var dueTime: Time
) : TaskOutline, Comparable<Task> {
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
    override fun getTaskDescription(): String {
        return this.taskDescription
    }
    override fun getTaskDetail(): TaskDetail {
        return TaskDetail(taskName, taskDescription)
    }

    override fun getDueDate(): Date {
        return this.dueDate
    }
    override fun getDueTime(): Time {
        return this.dueTime
    }
    override fun getDateAndTime(): DateAndTime {
        return DateAndTime(dueDate, dueTime)
    }

    fun setTaskName(name: String) {
        this.taskName = name
    }
    fun setTaskDescription(description: String) {
        this.taskDescription = description
    }

    // set due date/time and modify it

    fun setDueDate(date: Date) {
        this.dueDate = date
    }

    fun setDueTime(time: Time) {
        this.dueTime = time
    }

    // Date and Time ascending order
    override fun compareTo(other: Task): Int {
        val dCompare = this.dueDate.compareTo(other.dueDate)
        val tCompare = this.dueTime.compareTo(other.dueTime)
        if (dCompare == 0) return tCompare
        return dCompare
    }

    // comparators for sorting tasks
    companion object {
        val taskNameComparatorAscending = Comparator<Task> { t1, t2 -> t1.getTaskName().compareTo(t2.getTaskName()) }
        val taskNameComparatorDescending = Comparator<Task> { t1, t2 -> t2.getTaskName().compareTo(t1.getTaskName()) }
        val taskDueComparatorAscending = Comparator<Task> { t1, t2 -> t1.compareTo(t2) }
        val taskDueComparatorDescending = Comparator<Task> { t1, t2 -> t2.compareTo(t1) }
    }
}