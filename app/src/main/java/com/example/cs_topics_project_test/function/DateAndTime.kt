package com.example.cs_topics_project_test.function

data class DateAndTime(private val date : Date, private val time : Time) : Comparable<DateAndTime> {
    // Using 12 hour clock system

    fun getDate(): Date {
        return date
    }

    fun getTime(): Time {
        return time
    }

    // to sort into ascending order based on date and time
    override fun compareTo(other: DateAndTime): Int {
        val dCompare = this.date.compareTo(other.date)
        val tCompare = this.time.compareTo(other.time)
        if (dCompare == 0) {
            if (tCompare == 0) return 0
            else if (tCompare == 1) return 1
        }
        else if (dCompare == 1) return 1
        return -1
    }
}