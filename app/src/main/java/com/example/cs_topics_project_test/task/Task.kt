package com.example.cs_topics_project_test.task

import com.example.cs_topics_project_test.function.Date
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

    // Date and Time ascending order
    override fun compareTo(other: Task): Int {
        val dCompare = this.dueDate.compareTo(other.dueDate)
        val tCompare = this.dueTime.compareTo(other.dueTime)
        if (dCompare == 0) {
            if (tCompare == 0) return 0
            else if (tCompare == 1) return 1
        } else if (dCompare == 1) return 1
        return -1
    }

    // comparators for sorting tasks
    companion object {
        val taskNameComparatorAscending = Comparator<Task> { t1, t2 -> t1.getTaskName().compareTo(t2.getTaskName()) }
        val taskNameComparatorDescending = Comparator<Task> { t1, t2 -> t2.getTaskName().compareTo(t1.getTaskName()) }
        val taskDueComparatorAscending = Comparator<Task> { t1, t2 -> t1.compareTo(t2) }
        val taskDueComparatorDescending = Comparator<Task> { t1, t2 -> t2.compareTo(t1) }
    }
}