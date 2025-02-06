package com.example.cs_topics_project_test.task

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R

// to view added tasks
class TaskView : AppCompatActivity() {

    private lateinit var taskListAdapter: TaskListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_task) // opening up the appropriate layout page

        taskListAdapter = TaskListAdapter(TaskManager.tasks)

        // variable declaration
        val buttonNewTaskToggle: Button = findViewById(R.id.buttonNewTaskToggle)
        // val buttonBack: Button = findViewById(R.id.buttonBack)
        val recyclerViewTaskView : RecyclerView = findViewById(R.id.recyclerViewTaskView) // recyclerView to display tasks

        recyclerViewTaskView.adapter = taskListAdapter
        recyclerViewTaskView.layoutManager = LinearLayoutManager(this)

        // button functionality
        buttonNewTaskToggle.setOnClickListener {
            startActivity(Intent(this, TaskActivity::class.java))
        }
        /*buttonBack.setOnClickListener {
            finish()
            //startActivity(Intent(this, HomeActivity::class.java)) // change to go to previous slide, not to Home
        }*/
    }

    override fun onResume() {
        super.onResume()
        taskListAdapter.notifyDataSetChanged() // Notify the adapter to refresh the RecyclerView
    }
}