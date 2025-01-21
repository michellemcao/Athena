package com.example.cs_topics_project_test.task

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R

// to view tasks
class TaskFragment : Fragment() {

    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate layout for task view
        return inflater.inflate(R.layout.fragment_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskAdapter = TaskAdapter(TaskManager.tasks)

        // variable declaration
        val buttonNewTaskToggle: Button = view.findViewById(R.id.buttonNewTaskToggle)
        val recyclerViewTaskView : RecyclerView = view.findViewById(R.id.recyclerViewTaskView) // recyclerView to display tasks

        recyclerViewTaskView.adapter = taskAdapter
        recyclerViewTaskView.layoutManager = LinearLayoutManager(activity)

        // button functionality
        buttonNewTaskToggle.setOnClickListener {
            startActivity(Intent(activity, TaskActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        taskAdapter.notifyDataSetChanged() // Notify the adapter to refresh the RecyclerView
    }

}