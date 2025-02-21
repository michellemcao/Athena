package com.example.cs_topics_project_test.task

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R
import org.w3c.dom.Text

// to view tasks
class TaskFragment : Fragment() {

    private lateinit var dueTodayAdapter: TaskAdapterList // tasks that are due today
    private lateinit var dueLaterAdapter: TaskAdapterList
    private lateinit var pastDueAdapter: TaskAdapterList
    private lateinit var completedAdapter: TaskAdapterCompleted

    private var isDueTodayVisible = true
    private var isDueLaterVisible = true
    private var isPastDueVisible = true
    private var isCompletedVisible = true

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


        completedAdapter = TaskAdapterCompleted(TaskManager.tasksCompleted)
        // taskListAdapter = TaskListAdapter(TaskManager.tasks) // takes task from global TaskManager
        // tasks sorted by ascending name order
        // val ascTask = (TaskManager.tasksDueToday).sortedWith(Task.taskNameComparatorAscending)
        // dueTodayAdapter = TaskListAdapter(ascTask.toMutableList())
        dueTodayAdapter = TaskAdapterList(TaskManager.tasksDueToday, completedAdapter)
        /*TaskDataStructure.rangeList(
                DateAndTime(todayDate, midnight), true,
                DateAndTime(todayDate, latenight), true
            )*/

        dueLaterAdapter = TaskAdapterList(TaskManager.tasksDueLater, completedAdapter
            /*TaskDataStructure.rangeListFrom(
                DateAndTime(todayDate, latenight), false)*/
        )

        pastDueAdapter = TaskAdapterList(TaskManager.tasksPastDue, completedAdapter
            /*TaskDataStructure.rangeListTo(
                DateAndTime(todayDate, midnight), false)*/
        )

        // variable declaration: opens up TaskActivity to add tasks
        val buttonNewTaskToggle: Button = view.findViewById(R.id.buttonNewTaskToggle)

        // recycler view variable declaration
        val recyclerViewDueToday : RecyclerView = view.findViewById(R.id.recyclerViewDueToday) // recyclerView to display tasks that are due today
        val recyclerViewDueLater : RecyclerView = view.findViewById(R.id.recyclerViewDueLater)
        val recyclerViewPastDue : RecyclerView = view.findViewById(R.id.recyclerViewPastDue)
        val recyclerViewCompleted : RecyclerView = view.findViewById(R.id.recyclerViewCompleted)

        // toggle buttons to show or hide recycler view
        val toggleDueToday : TextView = view.findViewById(R.id.toggleDueToday)
        val toggleDueLater : TextView = view.findViewById(R.id.toggleDueLater)
        val togglePastDue : TextView = view.findViewById(R.id.togglePastDue)
        val toggleCompleted : TextView = view.findViewById(R.id.toggleCompleted)

        // connecting recyclerView to adapter
        recyclerViewDueToday.adapter = dueTodayAdapter
        recyclerViewDueToday.layoutManager = LinearLayoutManager(activity)

        recyclerViewDueLater.adapter = dueLaterAdapter
        recyclerViewDueLater.layoutManager = LinearLayoutManager(activity)

        recyclerViewPastDue.adapter = pastDueAdapter
        recyclerViewPastDue.layoutManager = LinearLayoutManager(activity)

        recyclerViewCompleted.adapter = completedAdapter
        recyclerViewCompleted.layoutManager = LinearLayoutManager(activity)

        buttonNewTaskToggle.setOnClickListener {
        // button functionality -> takes user to a new page to add tasks
            startActivity(Intent(activity, TaskActivity::class.java))
        }

        // toggle button code for showing or hiding tasks
        toggleDueToday.setOnClickListener {
            if (isDueTodayVisible) {
                recyclerViewDueToday.visibility = View.GONE // Hide RecyclerView
                toggleDueToday.text = "∨"
            } else {
                recyclerViewDueToday.visibility = View.VISIBLE // Show RecyclerView
                toggleDueToday.text = "∧"
            }
            isDueTodayVisible = !isDueTodayVisible // Toggle state
        }

        toggleDueLater.setOnClickListener {
            if (isDueLaterVisible) {
                recyclerViewDueLater.visibility = View.GONE // Hide RecyclerView
                toggleDueLater.text = "∨"
            } else {
                recyclerViewDueLater.visibility = View.VISIBLE // Show RecyclerView
                toggleDueLater.text = "∧"
            }
            isDueLaterVisible = !isDueLaterVisible // Toggle state
        }

        togglePastDue.setOnClickListener {
            if (isPastDueVisible) {
                recyclerViewPastDue.visibility = View.GONE // Hide RecyclerView
                togglePastDue.text = "∨"
            } else {
                recyclerViewPastDue.visibility = View.VISIBLE // Show RecyclerView
                togglePastDue.text = "∧"
            }
            isPastDueVisible = !isPastDueVisible // Toggle state
        }

        toggleCompleted.setOnClickListener {
            if (isCompletedVisible) {
                recyclerViewCompleted.visibility = View.GONE // Hide RecyclerView
                toggleCompleted.text = "∨"
            } else {
                recyclerViewCompleted.visibility = View.VISIBLE // Show RecyclerView
                toggleCompleted.text = "∧"
            }
            isCompletedVisible = !isCompletedVisible // Toggle state
        }
    }

    override fun onResume() {
        super.onResume()
        dueTodayAdapter.notifyDataSetChanged() // Notify the adapter to refresh the RecyclerView
        dueLaterAdapter.notifyDataSetChanged()
        pastDueAdapter.notifyDataSetChanged()
    }
}