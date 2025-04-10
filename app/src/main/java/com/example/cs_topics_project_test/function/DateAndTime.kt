package com.example.cs_topics_project_test.function

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.cs_topics_project_test.task.TaskStore
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

data class DateAndTime(private var date : Date, private var time : Time) : Comparable<DateAndTime> {
    // Using 12 hour clock system

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(unixTime : Long) : this(
        getDateFromUnix(unixTime),
        getTimeFromUnix(unixTime)
    )

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun getDateFromUnix(unixTime: Long): Date {
            val dateTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTime), ZoneId.systemDefault())
            return Date(dateTime.year, dateTime.monthValue, dateTime.dayOfMonth)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getTimeFromUnix(unixTime: Long): Time {
            val dateTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTime), ZoneId.systemDefault())
            return Time(dateTime.hour, dateTime.minute)
        }
    }

    fun getDate(): Date {
        return date
    }

    fun getTime(): Time {
        return time
    }

    // fix this for any time zone
    @RequiresApi(Build.VERSION_CODES.O)
    fun getUnixTime() : Long {
        // var hour = time.getHour()
        // if (time.isPM()) hour += 12
        val dateTime = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDate(), time.getHour(), time.getMin())

        val userZoneId = ZoneId.systemDefault() // gets user's current time zone

        val unixTime = dateTime.atZone(userZoneId).toEpochSecond() // converted using user's zone

        // val dateTime = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDate(), hour, time.getMin())
        // val unixTime = dateTime.toEpochSecond(ZoneOffset.ofHours(-7)) // or use ZoneOffset.ofHours(x)
        return unixTime
    }

    // to sort into ascending order based on date and time
    override fun compareTo(other: DateAndTime): Int {
        val dCompare = this.date.compareTo(other.date)
        val tCompare = this.time.compareTo(other.time)
        if (dCompare == 0) return tCompare
        return dCompare
    }

    override fun toString(): String {
        return "$date $time"
    }
}