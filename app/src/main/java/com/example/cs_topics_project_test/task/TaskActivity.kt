package com.example.cs_topics_project_test.task

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.function.Date
import com.example.cs_topics_project_test.function.Time

// to add new task
class TaskActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    private val localTasks = mutableListOf<Task>() // local tasks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        // val tasks = mutableListOf<Task>()
        taskAdapter = TaskAdapter(localTasks)

        val recyclerViewTasks: RecyclerView = findViewById(R.id.recyclerViewTasks)
        val editTextTaskName: EditText = findViewById(R.id.editTextTaskName)
        val buttonAddTask: Button = findViewById(R.id.buttonAddTask)
        val editTextTaskDescription: EditText = findViewById(R.id.editTextTaskDescription)
        val buttonBack: Button = findViewById(R.id.buttonBack)

        recyclerViewTasks.adapter = taskAdapter
        recyclerViewTasks.layoutManager = LinearLayoutManager(this)

        buttonAddTask.setOnClickListener {
            val taskName = editTextTaskName.text.toString()
            val taskDescription = editTextTaskDescription.text.toString()
            if (taskName.isNotBlank() && taskDescription.isNotBlank()) {
                val task = Task(
                    taskName,
                    Date(2025, 1, 19),
                    Time(6, 22, 30),
                    taskDescription
                )
                Toast.makeText(this, "Adding task: $taskName", Toast.LENGTH_SHORT).show()
                localTasks.add(task)
                TaskManager.tasks.add(task)
                taskAdapter.notifyDataSetChanged() // do we need it?
                editTextTaskName.text.clear()
                editTextTaskDescription.text.clear()
            } else {
                Toast.makeText(this, "Oopsie! Required field is blank.", Toast.LENGTH_SHORT).show()
            }
        }
        buttonBack.setOnClickListener {
            finish()
            //startActivity(Intent(this, TaskView::class.java)) // change to go to previous slide, not to Home
        }
    }
}