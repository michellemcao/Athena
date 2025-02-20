package com.example.cs_topics_project_test.task

import android.content.Context
import android.widget.Toast
import com.example.cs_topics_project_test.function.DateAndTime
import com.example.cs_topics_project_test.function.Date
import com.example.cs_topics_project_test.function.DateCompleted
import com.example.cs_topics_project_test.function.Time
import java.util.NavigableMap
import java.util.TreeMap

object TaskDataStructure {
    /* Psychology behind data structure:
     * People generally tend to schedule multiple tasks around the same time in their task manager.
     * This helps them receive notifications etc at the same time.
     * As such, array list/linked list for the tasks (value field) is more beneficial than unique key for every new task added
     */

    // private val taskMap = TreeMap<DateAndTime, MutableList<Task>>()
    // private val taskMap = TreeMap<DateAndTime, TaskDetail>()
    private val taskMap = TreeMap<DateAndTime, TaskNode>()
    private val completedMap = TreeMap<DateCompleted, TaskNode>()

    private class TaskNode (
        val task: TaskDetail,
        var nextTask: TaskNode?
    ) {}

    // adding task to taskMap
    fun addTask(key: DateAndTime, value: TaskDetail) : Boolean {
        /*if (keyExists(key)) {
            if (!taskMap[key]?.contains(value)!!) taskMap[key]?.add(value)
            Log.d("addTask", "duplicate task identified. task not re-added")
        }*/
        /*taskMap[key] = mutableListOf(value) // creates new list and inserts task*/
        /*taskMap[key] = value // creates new list and inserts task*/
        if (keyExists(key)) {
            var current = taskMap[key]
            while (current != null) {
                if (current.task == value) return false // indicating to TaskActivity that adding failed cause duplicate exists
                current = current.nextTask
            }
            val place = taskMap[key]
            taskMap[key] = TaskNode(value, place)
        } else {
            taskMap[key] = TaskNode(value, null)
        }
        return true
    }

    // checking is key exists in taskMap
    private fun keyExists(key: DateAndTime) : Boolean {
        return taskMap.containsKey(key)
    }

    // taskMap helped functions
    private fun rangeMap(lowerBound : DateAndTime,
                         lowerInclusive : Boolean,
                         upperBound : DateAndTime,
                         upperInclusive : Boolean) : NavigableMap<DateAndTime, TaskNode> {
        return taskMap.subMap(lowerBound, lowerInclusive, upperBound, upperInclusive)
    }

    fun rangeListFrom(lowerBound: DateAndTime, lowerInclusive: Boolean) : MutableList<Task> {
        if (taskMap.isEmpty() || taskMap.lastKey() <= lowerBound) {
            return mutableListOf<Task>() /*rangeList(lowerBound, lowerInclusive,
                DateAndTime(Date(2026, 1, 1), Time(0, 0, false)),
                false)*/
        }
        return rangeList(lowerBound, lowerInclusive, taskMap.lastKey(), true)
    }

    fun rangeListTo(upperBound: DateAndTime, upperInclusive: Boolean) : MutableList<Task> {
        if (taskMap.isEmpty() || taskMap.firstKey() >= upperBound) {
            return mutableListOf<Task>() /*rangeList(
                DateAndTime(Date(2025, 1, 1), Time(0, 0, false)),
                true, upperBound, upperInclusive)*/
        }
        return rangeList(taskMap.firstKey(), true, upperBound, upperInclusive)
    }

    fun rangeList(lowerBound : DateAndTime,
                  lowerInclusive : Boolean,
                  upperBound : DateAndTime,
                  upperInclusive : Boolean) : MutableList<Task> {
        val rangeMap = rangeMap(lowerBound, lowerInclusive, upperBound, upperInclusive)
        val taskList = mutableListOf<Task>()
        for ((key, value) in rangeMap) {
            var current = value
            while (current != null) {
                val task = Task(
                    value.task.getTaskName(), value.task.getTaskDescription(), key.getDate(), key.getTime())
                taskList.add(task)
                current = current.nextTask
            }
            /*val task = Task(value.getTaskName(), value.getTaskDescription(), key.getDate(), key.getTime())
            taskList.add(task)*/
        }
        return taskList
    }

    fun rangeDateTasks(date : Date) : MutableList<Task> {
        return rangeList(
            DateAndTime(date, Time(0, 0, false)), true,
            DateAndTime(date, Time(11, 59, true)), true)
    }

    // edit task

    // complete task and process it
    fun processCompletedTask(key: DateAndTime, value: TaskDetail) {
        if (!keyExists(key)) return

        var current = taskMap[key] // TaskNode
        var prev : TaskNode? = null

        while (current != null) {
            if (current.task == value) {
                if (prev == null) { // if the first node is what we are looking for
                    if (current.nextTask == null) {
                        taskMap.remove(key) // no more entries for that DateAndTime so remove altogether
                    } else {
                        taskMap[key] = current.nextTask!! // shift the keys entry to the nextTask node
                    }
                } else {
                    prev.nextTask = current.nextTask
                }
                addCompletedTask(DateCompleted(TaskManager.todayDate, key), value)
            }
            prev = current
            current = current.nextTask
        }
    }

    private fun addCompletedTask(key: DateCompleted, value: TaskDetail) {
        if (completedMap.containsKey(key)) {
            var current = completedMap[key]
            while (current != null) {
                if (current.task == value) return
                current = current.nextTask
            }
            val place = completedMap[key]
            completedMap[key] = TaskNode(value, place)
        } else {
            completedMap[key] = TaskNode(value, null)
        }
    }

    fun getTasksCompleted() : MutableList<TaskCompleted> {
        val taskList = mutableListOf<TaskCompleted>()
        for ((key, value) in completedMap) { //key = DateComplete; value = TaskNode
            var current = value
            while (current != null) {
                val task = TaskCompleted(key.getDateCompleted(), key.getDueDate(), current.task)
                taskList.add(task)
                current = current.nextTask!!
            }
        }
        return taskList // TaskCompleted = Date, DateAndTime, TaskDetail
    }

    fun clearUpTasks() {

    }
}