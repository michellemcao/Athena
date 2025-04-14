package com.example.cs_topics_project_test.task

import android.icu.util.Calendar
import com.example.cs_topics_project_test.function.Date
import com.example.cs_topics_project_test.function.DateAndTime
import com.example.cs_topics_project_test.function.Time

object TaskManager {
    // generals
    var todayDate: Date = getTodaysDate()
    private val midnight: Time = Time(0, 0) // 12:00 AM
    private val latenight: Time = Time(23, 59) // 11:59 PM
    private var time: Time = getTodaysTime()

    // val tasks = mutableListOf<Task>() // past global list of tasks

    var tasksDueToday = TaskDataStructure.rangeList(
        DateAndTime(todayDate, midnight), true,
        DateAndTime(todayDate, latenight), true)

    var tasksDueLater = TaskDataStructure.rangeListFrom(
        DateAndTime(todayDate, latenight), false)

    var tasksPastDue = TaskDataStructure.rangeListTo(
        DateAndTime(todayDate, midnight), false)

    var tasksCompleted = TaskDataStructure.getTasksCompleted()

    // 1 = tasksDueToday
    // 2 = tasksDueLater
    // 3 = tasksPastDue
    fun init(num : Int) {
        when(num) {
            1 -> tasksDueToday = TaskDataStructure.rangeList(
                DateAndTime(todayDate, midnight), true,
                DateAndTime(todayDate, latenight), true)
            2 -> tasksDueLater = TaskDataStructure.rangeListFrom(
                DateAndTime(todayDate, latenight), false)
            3 -> tasksPastDue = TaskDataStructure.rangeListTo(
                DateAndTime(todayDate, midnight), false)
        }
    }

    fun init() {
        tasksDueToday = TaskDataStructure.rangeList(
            DateAndTime(todayDate, midnight), true,
            DateAndTime(todayDate, latenight), true)

        tasksDueLater = TaskDataStructure.rangeListFrom(
            DateAndTime(todayDate, latenight), false)

        tasksPastDue = TaskDataStructure.rangeListTo(
            DateAndTime(todayDate, midnight), false)

        tasksCompleted = TaskDataStructure.getTasksCompleted()
    }
    /*private fun getTodayDate() : Date {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val date = calendar.get(Calendar.DAY_OF_MONTH)
        return Date(year, month, date)
    }*/

    private fun getTodaysDate() : Date {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val date = calendar.get(Calendar.DAY_OF_MONTH)
        return Date(year, month, date)
    }

    private fun getTodaysTime() : Time {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val min = calendar.get(Calendar.MINUTE)
        return Time(hour, min)
    }
}