package com.example.cs_topics_project_test.task

import com.example.cs_topics_project_test.function.DateAndTime
import java.util.NavigableMap
import java.util.TreeMap

object TaskDataStructure {
    /* Psychology behind data structure:
     * People generally tend to schedule multiple tasks around the same time in their task manager.
     * This helps them receive notifications etc at the same time.
     * As such, array list/linked list for the tasks (value field) is more beneficial than unique key for every new task added
     */

    // private val taskMap = TreeMap<DateAndTime, MutableList<Task>>()
    private val taskMap = TreeMap<DateAndTime, TaskDetail>()
    // private val taskMap = TreeMap<DateAndTime, TaskNode>()

    /*private class TaskNode {
        lateinit var task: TaskDetail
        lateinit var nextTask: TaskNode
    }*/

    // adding task
    fun addTask(key: DateAndTime, value: TaskDetail) {
        /*if (keyExists(key)) {
            if (!taskMap[key]?.contains(value)!!) taskMap[key]?.add(value)
            Log.d("addTask", "duplicate task identified. task not re-added")
        }*/
        /*taskMap[key] = mutableListOf(value) // creates new list and inserts task*/
        taskMap[key] = value // creates new list and inserts task
    }

    private fun keyExists(key: DateAndTime) : Boolean {
        return taskMap.containsKey(key)
    }

    fun rangeMap(lowerBound : DateAndTime,
              lowerInclusive : Boolean,
              upperBound : DateAndTime,
              upperInclusive : Boolean) : NavigableMap<DateAndTime, TaskDetail> {
        return taskMap.subMap(lowerBound, lowerInclusive, upperBound, upperInclusive)
    }

    fun rangeList(taskMap : NavigableMap<DateAndTime, TaskDetail>) : MutableList<Task> {
        val taskList = mutableListOf<Task>()
        for ((key, value) in taskMap) {
            val task = Task(value.getTaskName(), value.getTaskDescription(), key.getDate(), key.getTime())
            taskList.add(task)
        }
        return taskList
    }

    // edit task

    // remove task
}

// Red-Black BST -> Getting the data???