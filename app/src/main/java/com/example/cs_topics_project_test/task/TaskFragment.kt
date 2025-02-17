package com.example.cs_topics_project_test.task

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
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
    private lateinit var pastDueAdapter: TaskListAdapter

    /*private val todayDate: Date = Date(
        Calendar.getInstance().get(Calendar.YEAR),
        Calendar.getInstance().get(Calendar.MONTH) + 1,
        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    )
    private val midnight: Time = Time(0, 0, false) // 00:00 AM
    private val latenight: Time = Time(11, 59, true) // 11:59 PM*/

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

        // Toast.makeText(activity, "Today's Date: $todayDate", Toast.LENGTH_SHORT).show()


        // taskListAdapter = TaskListAdapter(TaskManager.tasks) // takes task from global TaskManager
        // tasks sorted by ascending name order
        // val ascTask = (TaskManager.tasksDueToday).sortedWith(Task.taskNameComparatorAscending)
        // dueTodayAdapter = TaskListAdapter(ascTask.toMutableList())
        dueTodayAdapter = TaskListAdapter(TaskManager.tasksDueToday)
        /*TaskDataStructure.rangeList(
                DateAndTime(todayDate, midnight), true,
                DateAndTime(todayDate, latenight), true
            )*/

        dueLaterAdapter = TaskListAdapter(TaskManager.tasksDueLater
            /*TaskDataStructure.rangeListFrom(
                DateAndTime(todayDate, latenight), false)*/
        )

        pastDueAdapter = TaskListAdapter(TaskManager.tasksPastDue
            /*TaskDataStructure.rangeListTo(
                DateAndTime(todayDate, midnight), false)*/
        )

        // variable declaration
        val buttonNewTaskToggle: Button = view.findViewById(R.id.buttonNewTaskToggle) // opens up TaskActivity to add tasks
        val recyclerViewDueToday : RecyclerView = view.findViewById(R.id.recyclerViewDueToday) // recyclerView to display tasks that are due today
        val recyclerViewDueLater : RecyclerView = view.findViewById(R.id.recyclerViewDueLater)
        val recyclerViewPastDue : RecyclerView = view.findViewById(R.id.recyclerViewPastDue)

        recyclerViewDueToday.adapter = dueTodayAdapter
        recyclerViewDueToday.layoutManager = LinearLayoutManager(activity)

        recyclerViewDueLater.adapter = dueLaterAdapter
        recyclerViewDueLater.layoutManager = LinearLayoutManager(activity)

        recyclerViewPastDue.adapter = pastDueAdapter
        recyclerViewPastDue.layoutManager = LinearLayoutManager(activity)

        buttonNewTaskToggle.setOnClickListener {
        // button functionality
            startActivity(Intent(activity, TaskActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        dueTodayAdapter.notifyDataSetChanged() // Notify the adapter to refresh the RecyclerView
        dueLaterAdapter.notifyDataSetChanged()
        pastDueAdapter.notifyDataSetChanged()
    }
}