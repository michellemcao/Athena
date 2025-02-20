package com.example.cs_topics_project_test.function

class DateCompleted (private val dateCompleted: Date, private val dueDate: DateAndTime) : Comparable<DateCompleted> {
    fun getDateCompleted() : Date {
        return dateCompleted
    }

    fun getDueDate() : DateAndTime {
        return dueDate
    }

    override fun compareTo(other: DateCompleted): Int {
        val completeCompare = this.dateCompleted.compareTo(other.dateCompleted)
        val dueCompare = this.dueDate.compareTo(other.dueDate)
        if (completeCompare == 0) return dueCompare
        return completeCompare
    }
}