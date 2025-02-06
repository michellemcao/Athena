package com.example.cs_topics_project_test.task

import android.util.Log
import android.widget.Toast
import com.example.cs_topics_project_test.function.DateAndTime
import java.util.TreeMap

object TaskDataStructure {
    /* Psychology behind data structure:
     * People generally tend to schedule multiple tasks around the same time in their task manager.
     * This helps them receive notifications etc at the same time.
     * As such, array list for the tasks (value) is more beneficial than unique key for every new task added
     */
    private val taskMap = TreeMap<DateAndTime, MutableList<Task>>()

    // adding task
    fun addTask(key: DateAndTime, value: Task) {
        if (keyExists(key)) {
            if (!taskMap[key]?.contains(value)!!) taskMap[key]?.add(value)
            Log.d("addTask", "duplicate task identified. task not re-added")
        }
        taskMap[key] = mutableListOf(value) // creates new list and inserts task
    }

    private fun keyExists(key: DateAndTime) : Boolean {
        return taskMap.containsKey(key)
    }

    // edit task

    // remove task
}

// Red-Black BST -> Getting the data???