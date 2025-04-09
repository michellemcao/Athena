package com.example.cs_topics_project_test.function

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class DateCompleted (private val dateCompleted: Date, private val dueDate: DateAndTime) : Comparable<DateCompleted> {
    fun getDateCompleted() : Date {
        return dateCompleted
    }

    fun getDueDate() : DateAndTime {
        return dueDate
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getUnixTimeCompleted() : Long {
        val dateTime = LocalDateTime.of(dateCompleted.getYear(), dateCompleted.getMonth(), dateCompleted.getDate(), 0, 0)
        val userZoneId = ZoneId.systemDefault() // gets user's current time zone
        val unixTime = dateTime.atZone(userZoneId).toEpochSecond()
        return unixTime
    }
    override fun compareTo(other: DateCompleted): Int {
        val completeCompare = this.dateCompleted.compareTo(other.dateCompleted)
        val dueCompare = this.dueDate.compareTo(other.dueDate)
        if (completeCompare == 0) return dueCompare
        return completeCompare
    }
}