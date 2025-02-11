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

    private lateinit var taskListAdapter: TaskListAdapter // tasks that are due today

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate layout for task view
        return inflater.inflate(R.layout.fragment_task_upgrade, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskListAdapter = TaskListAdapter(TaskManager.tasks) // takes task from global TaskManager

        // variable declaration
        val buttonNewTaskToggle: Button = view.findViewById(R.id.buttonNewTaskToggle) // opens up TaskActivity to add tasks
        val recyclerViewDueToday : RecyclerView = view.findViewById(R.id.recyclerViewDueToday) // recyclerView to display tasks that are due today

        recyclerViewDueToday.adapter = taskListAdapter
        recyclerViewDueToday.layoutManager = LinearLayoutManager(activity)

        buttonNewTaskToggle.setOnClickListener {
        // button functionality
            startActivity(Intent(activity, TaskActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        taskListAdapter.notifyDataSetChanged() // Notify the adapter to refresh the RecyclerView
    }

}