package com.example.cs_topics_project_test.task

import android.icu.util.Calendar
import com.example.cs_topics_project_test.function.Date
import com.example.cs_topics_project_test.function.DateAndTime
import com.example.cs_topics_project_test.function.Time

object TaskManager {
    // generals
    val todayDate: Date
        get() {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val date = calendar.get(Calendar.DAY_OF_MONTH)
            return Date(year, month, date)
        }
    private val midnight: Time = Time(12, 0, false) // 12:00 AM
    private val latenight: Time = Time(11, 59, true) // 11:59 PM

    // val tasks = mutableListOf<Task>() // past global list of tasks

    var tasksDueToday = TaskDataStructure.rangeList(
        DateAndTime(todayDate, midnight), true,
        DateAndTime(todayDate, latenight), true)

    var tasksDueLater = TaskDataStructure.rangeListFrom(
        DateAndTime(todayDate, latenight), false)

    var tasksPastDue = TaskDataStructure.rangeListTo(
        DateAndTime(todayDate, midnight), false)

    val tasksCompleted = TaskDataStructure.getTasksCompleted()

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
    /*private fun getTodayDate() : Date {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val date = calendar.get(Calendar.DAY_OF_MONTH)
        return Date(year, month, date)
    }*/
}