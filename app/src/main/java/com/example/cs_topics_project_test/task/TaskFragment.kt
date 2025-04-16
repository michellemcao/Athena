package com.example.cs_topics_project_test.task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.function.Date
import com.example.cs_topics_project_test.function.DateAndTime
import com.example.cs_topics_project_test.function.Time
import org.w3c.dom.Text
import java.util.Calendar

// to view tasks
class TaskFragment : Fragment(), TaskListener {

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
        TaskManager.init() // make local data storage to pull from main one
        return inflater.inflate(R.layout.fragment_task_upgrade, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Toast.makeText(activity, "Today's Date: $todayDate", Toast.LENGTH_SHORT).show()


        completedAdapter = TaskAdapterCompleted(TaskManager.tasksCompleted, this)
        // taskListAdapter = TaskListAdapter(TaskManager.tasks) // takes task from global TaskManager
        // tasks sorted by ascending name order
        // val ascTask = (TaskManager.tasksDueToday).sortedWith(Task.taskNameComparatorAscending)
        // dueTodayAdapter = TaskListAdapter(ascTask.toMutableList())
        dueTodayAdapter = TaskAdapterList(TaskManager.tasksDueToday, this) // completedAdapter)
        /*TaskDataStructure.rangeList(
                DateAndTime(todayDate, midnight), true,
                DateAndTime(todayDate, latenight), true
            )*/

        dueLaterAdapter = TaskAdapterList(TaskManager.tasksDueLater, this) // completedAdapter)
        /*TaskDataStructure.rangeListFrom(
                DateAndTime(todayDate, latenight), false)*/

        pastDueAdapter = TaskAdapterList(TaskManager.tasksPastDue, this) // completedAdapter)
        /*TaskDataStructure.rangeListTo(
                DateAndTime(todayDate, midnight), false)*/

        // variable declaration: opens up TaskActivity to add tasks
        val buttonNewTaskToggle: Button = view.findViewById(R.id.buttonNewTaskToggle)

        // recycler view variable declaration
        val recyclerViewDueToday: RecyclerView =
            view.findViewById(R.id.recyclerViewDueToday) // recyclerView to display tasks that are due today
        val recyclerViewDueLater: RecyclerView = view.findViewById(R.id.recyclerViewDueLater)
        val recyclerViewPastDue: RecyclerView = view.findViewById(R.id.recyclerViewPastDue)
        val recyclerViewCompleted: RecyclerView = view.findViewById(R.id.recyclerViewCompleted)

        // toggle buttons to show or hide recycler view
        val toggleDueToday: TextView = view.findViewById(R.id.toggleDueToday)
        val toggleDueLater: TextView = view.findViewById(R.id.toggleDueLater)
        val togglePastDue: TextView = view.findViewById(R.id.togglePastDue)
        val toggleCompleted: TextView = view.findViewById(R.id.toggleCompleted)

        // buttons to sort tasks and their listeners
        val sortDueToday: ImageButton = view.findViewById(R.id.buttonDueTodaySort)
        sortDueToday.setOnClickListener {
            showSortOptions(sortDueToday, dueTodayAdapter, TaskManager.tasksDueToday)
        }

        val sortDueLater: ImageButton = view.findViewById(R.id.buttonDueLaterSort)
        sortDueLater.setOnClickListener {
            showSortOptions(sortDueLater, dueLaterAdapter, TaskManager.tasksDueLater)
        }

        val sortPastDue: ImageButton = view.findViewById(R.id.buttonPastDueSort)
        sortPastDue.setOnClickListener {
            showSortOptions(sortPastDue, pastDueAdapter, TaskManager.tasksPastDue)
        }

        // connecting recyclerView to adapter
        recyclerViewDueToday.adapter = dueTodayAdapter
        recyclerViewDueToday.layoutManager = LinearLayoutManager(activity)

        recyclerViewDueLater.adapter = dueLaterAdapter
        recyclerViewDueLater.layoutManager = LinearLayoutManager(activity)

        recyclerViewPastDue.adapter = pastDueAdapter
        recyclerViewPastDue.layoutManager = LinearLayoutManager(activity)

        recyclerViewCompleted.adapter = completedAdapter
        recyclerViewCompleted.layoutManager = LinearLayoutManager(activity)

        // button functionality -> takes user to a new page to add tasks
        buttonNewTaskToggle.setOnClickListener {
            startActivity(Intent(activity, TaskActivity::class.java))
        }

        // toggle button code for showing or hiding tasks REDUNDANT CODE
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

    // Notify the adapter to refresh the RecyclerView, since tasks were most likely added in TaskActivity
    override fun onResume() {
        super.onResume()
        dueTodayAdapter.notifyDataSetChanged()
        dueLaterAdapter.notifyDataSetChanged()
        pastDueAdapter.notifyDataSetChanged()
    }

    // shows the sorting options when button is clicked
    private fun showSortOptions(
        view: ImageButton,
        adapter: TaskAdapterList,
        tasks: MutableList<Task>
    ) {
        val popup = PopupMenu(activity, view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.sort_menu, popup.menu) // Reference a menu XML file

        popup.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.sortByNameAscending -> {
                    // tasks sorted by ascending name order
                    val sortedTask =
                        (tasks/*TaskManager.tasksDueToday*/).sortedWith(Task.taskNameComparatorAscending)
                    adapter.updateList(sortedTask.toMutableList())
                    true
                }

                R.id.sortByNameDescending -> {
                    // tasks sorted by descending name order
                    val sortedTask = (tasks).sortedWith(Task.taskNameComparatorDescending)
                    adapter.updateList(sortedTask.toMutableList())
                    true
                }

                R.id.sortByDueDateAscending -> {
                    // tasks sorted in ascending due date and time order
                    val sortedTask = (tasks).sortedWith(Task.taskDueComparatorAscending)
                    adapter.updateList(sortedTask.toMutableList())
                    true
                }

                R.id.sortByDueDateDescending -> {
                    // tasks sorted in descending due date and time order
                    val sortedTask = (tasks).sortedWith(Task.taskDueComparatorDescending)
                    adapter.updateList(sortedTask.toMutableList())
                    true
                }

                else -> false
            }
        }
        popup.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onTaskPressed(task: Task, position : Int, taskList : MutableList<Task>, adapter : TaskAdapterList) {
        showTaskEditDeleteLayout(task, position, taskList, adapter)
    }

    override fun onTaskCompleted(task : TaskCompleted) {
        completedAdapter.addCompletedTask(task)
    }

    override fun onTask(num: Int, task: Task) {
        when(num) {
            1 -> dueTodayAdapter.addTask(task)
            2 -> dueLaterAdapter.addTask(task)
            3 -> pastDueAdapter.addTask(task)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showTaskEditDeleteLayout(task: Task, position: Int, taskList: MutableList<Task>, adapter: TaskAdapterList) {
        val layout = view?.findViewById<View>(R.id.taskEditDeleteLayout) ?: return

        layout.visibility = View.VISIBLE

        val editTextTaskName: EditText = layout.findViewById<EditText>(R.id.editTextTaskName) // the task name
        val editTextTaskDescription: EditText = layout.findViewById<EditText>(R.id.editTextTaskDescription) // the task description
        val textViewDate: TextView = layout.findViewById<TextView>(R.id.textViewDate) // the task due date
        val textViewTime: TextView = layout.findViewById<TextView>(R.id.textViewTime) // the task due time

        // setting all options to default value
        editTextTaskName.setText(task.getTaskName())
        editTextTaskDescription.setText(task.getTaskDescription())
        textViewDate.text = task.getDueDate().toString()
        textViewTime.text = task.getDueTime().toString()

        // storing values for backend functionality
        var taskName = task.getTaskName()
        var taskDescription = task.getTaskDescription()
        var dueDate = task.getDueDate()
        var dueTime = task.getDueTime()

        layout.findViewById<Button>(R.id.buttonEditTask).setOnClickListener {
            // when edit task button is clicked
            // taskName = editTextTaskName.text.toString()
            // taskDescription = editTextTaskDescription.text.toString()
            if (editTextTaskName.text.toString().isNotBlank()) {
                // removing the original task
                if (TaskDataStructure.removeTask( // removes task from TreeMap & Firestore
                    DateAndTime(task.getDueDate(), task.getDueTime()),
                    TaskDetail(taskName, taskDescription))) taskList.remove(task) // removes task from list

                // TaskDataStructure.deleteTask(taskName, taskDescription, task.getDateAndTime().getUnixTime()) // remove task from firestore

                // notify adapter that tasks have been deleted
                adapter.notifyItemRemoved(position)

                taskName = editTextTaskName.text.toString()
                taskDescription = editTextTaskDescription.text.toString()
                if (taskDescription.isEmpty()) taskDescription = ""
                val taskNew = Task(taskName, taskDescription, dueDate, dueTime)
                Toast.makeText(activity, "Edited task: $taskName", Toast.LENGTH_SHORT).show()

                // taskList.add(taskNew)
                // taskList[position] = taskNew

                // TaskManager.tasks.add(task) // previous TaskManager
                val added: Boolean = TaskDataStructure.addTask(
                    DateAndTime(dueDate, dueTime),
                    TaskDetail(taskName, taskDescription)) // for storage w/ TreeMap

                // for viewing in app real-time
                if (added) {
                    TaskDataStructure.storeTask(taskNew) // for storage w/ FireStore
                    // notify adapter that tasks have been added
                    if (dueDate < TaskManager.todayDate) {
                        TaskManager.tasksPastDue.add(taskNew)
                        pastDueAdapter.notifyDataSetChanged()
                    }
                    else if (dueDate > TaskManager.todayDate) {
                        TaskManager.tasksDueLater.add(taskNew)
                        dueLaterAdapter.notifyDataSetChanged()
                    }
                    else {
                        TaskManager.tasksDueToday.add(taskNew)
                        dueTodayAdapter.notifyDataSetChanged()
                    }
                }

                layout.visibility = View.GONE
                // exiting edit view
            } else {
                Toast.makeText(activity, "Oops! Required field is blank. Cannot edit task", Toast.LENGTH_SHORT).show()
            }
        }

        // select date
        textViewDate.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                dueDate = selectedDate
                textViewDate.text = dueDate.toString()
                // Toast.makeText(this, "Date Selected: $dueDate", Toast.LENGTH_SHORT).show()
            }
        }

        // select time
        textViewTime.setOnClickListener {
            showTimePickerDialog { selectedTime ->
                dueTime = selectedTime
                textViewTime.text = selectedTime.toString()
                // Toast.makeText(this, "Time Selected: $selectedTime", Toast.LENGTH_SHORT).show()
            }
        }

        layout.findViewById<Button>(R.id.buttonDeleteTask).setOnClickListener {
            if (TaskDataStructure.removeTask( // removes task from TreeMap & Firestore
                DateAndTime(dueDate, dueTime),
                TaskDetail(taskName, taskDescription))
                ) taskList.remove(task) // removes task from adapter
            // TaskDataStructure.deleteTask(taskName, taskDescription, task.getDateAndTime().getUnixTime())// remove task from firestore
            adapter.notifyDataSetChanged()
            layout.visibility = View.GONE
        }
    }

    private fun showDatePickerDialog(onDateSelected: (Date) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = activity?.let {
            DatePickerDialog(
                it,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = Date(selectedYear, selectedMonth + 1, selectedDay)
                    onDateSelected(selectedDate)
                }, year, month, day)
        }
        datePickerDialog!!.show()
    }

    private fun showTimePickerDialog(onTimeSelected: (Time) -> Unit) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            activity,
            { _, selectedHour, selectedMinute ->
                val selectedTime = Time(selectedHour, selectedMinute)
                onTimeSelected(selectedTime)
            },
            hour,
            minute,
            false // Set to false for 12-hour format with AM/PM
        )
        timePickerDialog.show()
    }
}