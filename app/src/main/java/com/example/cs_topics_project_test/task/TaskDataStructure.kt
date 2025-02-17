package com.example.cs_topics_project_test.task

import com.example.cs_topics_project_test.function.DateAndTime
import com.example.cs_topics_project_test.function.Date
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

    private class TaskNode (
        val task: TaskDetail,
        val nextTask: TaskNode?
    ) {}

    // adding task
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

    private fun keyExists(key: DateAndTime) : Boolean {
        return taskMap.containsKey(key)
    }

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

    // remove task
}

// Red-Black BST -> Getting the data???