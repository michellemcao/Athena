package com.example.cs_topics_project_test.function

data class DateAndTime(val year: Int, val month: Int, val dateNum: Int, val hour: Int, val min: Int, val isPM: Boolean) : Comparable<DateAndTime> {
    // Using 12 hour clock system
    private val date: Date = Date(year, month, dateNum)
    private val time: Time = Time(hour, min, isPM)

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