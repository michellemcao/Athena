package com.example.cs_topics_project_test.task

interface TaskListener {
    fun onTaskCompleted(task: TaskCompleted) {}
}