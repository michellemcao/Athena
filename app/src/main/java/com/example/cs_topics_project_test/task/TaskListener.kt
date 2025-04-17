package com.example.cs_topics_project_test.task

interface TaskListener {
    fun onTaskCompleted(task: TaskCompleted)
    fun onTask(num: Int, task: Task)
    fun onTaskPressed(task: Task, position: Int, taskList : MutableList<Task>, adapter : TaskAdapterList)
}