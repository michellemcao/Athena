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
import com.example.cs_topics_project_test.function.Date
import com.example.cs_topics_project_test.function.DateAndTime
import com.example.cs_topics_project_test.function.Time

// to view tasks
class TaskFragment : Fragment() {

    private lateinit var dueTodayAdapter: TaskListAdapter // tasks that are due today
    private lateinit var dueLaterAdapter: TaskListAdapter

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

        // taskListAdapter = TaskListAdapter(TaskManager.tasks) // takes task from global TaskManager
        dueTodayAdapter = TaskListAdapter(
            TaskDataStructure.rangeList(
                DateAndTime(
                    Date(2025, 2, 13),
                    Time(0, 0, false)
                ),
                true,
                DateAndTime(
                    Date(2025, 2, 14),
                    Time(0, 0, false)
                ),
                false
            )
        )

        dueLaterAdapter = TaskListAdapter(TaskDataStructure.rangeListFrom(
            DateAndTime(Date(2025, 2, 13),
                Time(11, 59, true)
            ),
            false
        ))

        // variable declaration
        val buttonNewTaskToggle: Button = view.findViewById(R.id.buttonNewTaskToggle) // opens up TaskActivity to add tasks
        val recyclerViewDueToday : RecyclerView = view.findViewById(R.id.recyclerViewDueToday) // recyclerView to display tasks that are due today
        val recyclerViewDueLater : RecyclerView = view.findViewById(R.id.recyclerViewDueLater)

        recyclerViewDueToday.adapter = dueTodayAdapter
        recyclerViewDueToday.layoutManager = LinearLayoutManager(activity)

        recyclerViewDueLater.adapter = dueLaterAdapter
        recyclerViewDueLater.layoutManager = LinearLayoutManager(activity)

        buttonNewTaskToggle.setOnClickListener {
        // button functionality
            startActivity(Intent(activity, TaskActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        dueTodayAdapter.notifyDataSetChanged() // Notify the adapter to refresh the RecyclerView
        dueLaterAdapter.notifyDataSetChanged()
    }
}