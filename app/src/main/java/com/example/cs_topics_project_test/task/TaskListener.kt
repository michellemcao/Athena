package com.example.cs_topics_project_test.task

interface TaskListener {
    fun onTaskCompleted(task: TaskCompleted)
    fun onTaskPressed(task: Task, position: Int, taskList : MutableList<Task>, adapter : TaskAdapterList)
}