package com.example.cs_topics_project_test.function

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class DateCompleted (private val dateCompleted: DateAndTime, private val dueDate: DateAndTime) : Comparable<DateCompleted> {

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(unixTimeC : Long, unixTimeD : Long) : this(
        //getDateFromUnix(unixTimeC),
        DateAndTime(unixTimeC),
        DateAndTime(unixTimeD)
    )

    /*companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun getDateFromUnix(unixTime: Long): Date {
            val dateTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTime), ZoneId.systemDefault())
            return Date(dateTime.year, dateTime.monthValue, dateTime.dayOfMonth)
        }
    }*/

    fun getDateCompleted() : DateAndTime {
        return dateCompleted
    }

    fun getDueDate() : DateAndTime {
        return dueDate
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getUnixTimeCompleted() : Long {
        /*val dateTime = LocalDateTime.of(dateCompleted.getYear(), dateCompleted.getMonth(), dateCompleted.getDate(), 0, 0)
        val userZoneId = ZoneId.systemDefault() // gets user's current time zone
        val unixTime = dateTime.atZone(userZoneId).toEpochSecond()
        return unixTime*/
        return dateCompleted.getUnixTime()
    }

    override fun compareTo(other: DateCompleted): Int {
        val completeCompare = this.dateCompleted.compareTo(other.dateCompleted)
        val dueCompare = this.dueDate.compareTo(other.dueDate)
        if (completeCompare == 0) return dueCompare
        return completeCompare
    }
}