package com.example.cs_topics_project_test.ui.tasks

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.cs_topics_project_test.HomeActivity
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.TaskActivity
import com.example.cs_topics_project_test.function.tasks.Task

class TaskView : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_tasks)

        val tasks = mutableListOf<Task>()
        val buttonNewTaskToggle: Button = findViewById(R.id.buttonNewTaskToggle)
        val buttonBack: Button = findViewById(R.id.buttonBack)

        buttonNewTaskToggle.setOnClickListener {
            startActivity(Intent(this, TaskActivity::class.java))
        }
        buttonBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java)) // change to go to previous slide, not to Home
        }
    }
}