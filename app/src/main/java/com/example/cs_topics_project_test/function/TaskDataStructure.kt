package com.example.cs_topics_project_test.function

import com.example.cs_topics_project_test.task.Task

class TaskDataStructure {
    private val tasks: MutableList<Task> = mutableListOf()

    fun getTask() : List<Task> = tasks

    fun addTask(task: Task) {
        tasks.add(task)
    }
}

// Red-Black BST -> Getting the data???