package com.example.cs_topics_project_test.function

class DateAndTime(year: Int, month: Int, dateNum: Int, hour: Int, min: Int, sec: Int) : Comparable<DateAndTime> {
    // Using 24 hour clock system
    private val date: Date = Date(year, month, dateNum)
    private val time: Time = Time(hour, min, sec)

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