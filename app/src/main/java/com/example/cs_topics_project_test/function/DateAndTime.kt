package com.example.cs_topics_project_test.function

class DateAndTime(year: Int, month: Int, dateNum: Int, hour: Int, min: Int, sec: Int) {
    // Using 24 hour clock system
    private val date: Date = TODO()
    private val time: Time = TODO()

    init {
        val date = Date(year, month, dateNum)
        val time = Time(hour, min, sec)
    }

    fun getDate(): Date {
        return date
    }

    fun getTime(): Time {
        return time
    }
    
}