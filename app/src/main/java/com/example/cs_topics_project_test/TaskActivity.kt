package com.example.cs_topics_project_test

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.function.Date
import com.example.cs_topics_project_test.function.Time
import com.example.cs_topics_project_test.function.tasks.Task
import com.example.cs_topics_project_test.ui.tasks.TaskAdapter

class TaskActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_tasks)

        val tasks = mutableListOf<Task>()
        taskAdapter = TaskAdapter(tasks, this)

        val recyclerViewTasks: RecyclerView = findViewById(R.id.recyclerViewTasks)
        val editTextTask: EditText = findViewById(R.id.editTextTask)
        val buttonAddTask: Button = findViewById(R.id.buttonAddTask)

        recyclerViewTasks.adapter = taskAdapter
        recyclerViewTasks.layoutManager = LinearLayoutManager(this)

        buttonAddTask.setOnClickListener {
            val taskName = editTextTask.text.toString()
            if (taskName.isNotBlank()) {
                val task = Task(
                    taskName,
                    Date(2025, 1, 19),
                    Time(6, 22, 30),
                    "testing... 1... 2... 3..."
                )
                Toast.makeText(this, "Adding task: $taskName", Toast.LENGTH_SHORT).show()
                taskAdapter.addTask(task)
                editTextTask.text.clear()
            } else {
                Toast.makeText(this, "Oopsie! Task name is blank.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}