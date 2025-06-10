package com.example.cs_topics_project_test.task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.function.Date
import com.example.cs_topics_project_test.function.DateAndTime
import com.example.cs_topics_project_test.function.Time
import com.example.cs_topics_project_test.themes.ThemeManager
import java.util.Calendar

// to add new task
class TaskActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapterActivity
    private val localTasks = mutableListOf<Task>() // local tasks
    private var tCount = 0 // number of tasks in local task list

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val theme = ThemeManager.currentThemeColors!!
        window.statusBarColor = theme.header

        val rootView = findViewById<ConstraintLayout>(R.id.background)
        rootView.setBackgroundColor(theme.backgroundTasks)


        // val tasks = mutableListOf<Task>()
        taskAdapter = TaskAdapterActivity(localTasks)

        // retrieve data from ui feature to add into task manager
        val recyclerViewTasks: RecyclerView = findViewById(R.id.recyclerViewTasks) // to display recently added tasks
        val buttonAddTask: Button = findViewById(R.id.buttonAddTask) // to add the task into TaskManager
        val editTextTaskName: EditText = findViewById(R.id.editTextTaskName) // the task name
        val editTextTaskDescription: EditText = findViewById(R.id.editTextTaskDescription) // the task description
        val textViewDate: TextView = findViewById(R.id.textViewDate) // the task due date
        val textViewTime: TextView = findViewById(R.id.textViewTime) // the task due time
        val buttonBack: Button = findViewById(R.id.buttonBack) // to go back to TaskFragment page

        // background of the date and time pickers
        val bg = ContextCompat.getDrawable(this, R.drawable.date_selector_background)?.mutate()
        bg!!.setTint(theme.editTask)
        textViewDate.background = bg
        textViewTime.background = bg

        // highlight of the date and time pickers
        // val color = ContextCompat.getColor(rootView.context, theme.toolbar)
        editTextTaskName.backgroundTintList = ColorStateList.valueOf(theme.toolbar)
        editTextTaskDescription.backgroundTintList = ColorStateList.valueOf(theme.toolbar)

        // default date and time values to make sure the change actually happens
        var dueDate = TaskManager.todayDate

        var calendar = Calendar.getInstance()
        var hour = calendar.get(Calendar.HOUR_OF_DAY) // 24-hour format; 0 - 23
        var min = calendar.get(Calendar.MINUTE)
        var dueTime = Time(hour, min)
        // var dueTime = Time(12, 0) // 12:00 PM or noon

        // setting date and time that will be added
        textViewDate.text = dueDate.toString()
        textViewTime.text = dueTime.toString()

        // setting up recyclerView
        recyclerViewTasks.adapter = taskAdapter
        recyclerViewTasks.layoutManager = LinearLayoutManager(this)

        // adding tasks
        buttonAddTask.setOnClickListener {
            val taskName = editTextTaskName.text.toString()
            var taskDescription = editTextTaskDescription.text.toString()
            if (taskName.isNotBlank()) { // && taskDescription.isNotBlank()) {
                /*if (dueDate == null || dueTime == null) {
                    Toast.makeText(this, "Set a due date & time for better user experience!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }*/
                if (taskDescription.isEmpty()) taskDescription = ""
                val task = Task(taskName, taskDescription, dueDate, dueTime)
                Toast.makeText(this, "Adding task: $taskName to local", Toast.LENGTH_SHORT).show()
                localTasks.add(task)

                // TaskManager.tasks.add(task) // previous TaskManager
                val added: Boolean = TaskDataStructure.addTask(
                    DateAndTime(dueDate, dueTime),
                    TaskDetail(taskName, taskDescription)) // for storage w/ Firebase

                // for viewing in app real-time
                if (added) {
                    TaskDataStructure.storeTask(task)
                    if (dueDate < TaskManager.todayDate) TaskManager.tasksPastDue.add(task)
                    else if (dueDate > TaskManager.todayDate) TaskManager.tasksDueLater.add(task)
                    else TaskManager.tasksDueToday.add(task)
                }

                // notify adapter that tasks has been update
                // taskListAdapter.notifyDataSetChanged() // do we need it?
                taskAdapter.notifyItemInserted(tCount++)
                // Toast.makeText(this, "Number of tasks in local: $tCount", Toast.LENGTH_SHORT).show()

                // resetting all the fields
                editTextTaskName.text.clear()
                editTextTaskDescription.text.clear()
                dueDate = TaskManager.todayDate

                calendar = Calendar.getInstance()
                hour = calendar.get(Calendar.HOUR_OF_DAY) // 24-hour format; 0 - 23
                min = calendar.get(Calendar.MINUTE)
                dueTime = Time(hour, min)
                // dueTime = Time(12, 0) // 12:00 PM

                textViewDate.text = dueDate.toString()
                textViewTime.text = dueTime.toString()
            } else {
                Toast.makeText(this, "Oops! Required field is blank.", Toast.LENGTH_SHORT).show()
            }
        }

        textViewDate.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                dueDate = selectedDate
                textViewDate.text = dueDate.toString()
                // Toast.makeText(this, "Date Selected: $dueDate", Toast.LENGTH_SHORT).show()
            }
        }

        textViewTime.setOnClickListener {
            showTimePickerDialog { selectedTime ->
                dueTime = selectedTime
                textViewTime.text = selectedTime.toString()
                // Toast.makeText(this, "Time Selected: $selectedTime", Toast.LENGTH_SHORT).show()
            }
        }

        buttonBack.setOnClickListener {
            // TaskDataStructure.storeAllTasks()
            finish()
            //startActivity(Intent(this, TaskView::class.java)) // change to go to previous slide, not to Home
        }
    }

    // method to pick the due date using ui + kotlin backend
    private fun showDatePickerDialog(onDateSelected: (Date) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Date(selectedYear, selectedMonth + 1, selectedDay)
            // val formattedDate = "${selectedDay}/${selectedMonth + 1}/$selectedYear"
            onDateSelected(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    // method to pick the due time using ui feature + kotlin backend
    private fun showTimePickerDialog(onTimeSelected: (Time) -> Unit) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                /*val isPM = selectedHour >= 12
                val adjustedHour = if (selectedHour % 12 == 0) 12 else selectedHour % 12
                val selectedTime = Time(adjustedHour, selectedMinute, isPM)*/
                val selectedTime = Time(selectedHour, selectedMinute)
                /*val formattedTime = String.format("%02d:%02d %s",
                    formattedHour, selectedMinute,
                    if (isPM) "PM" else "AM"
                )*/
                onTimeSelected(selectedTime)
            },
            hour,
            minute,
            false // Set to false for 12-hour format with AM/PM
        )

        timePickerDialog.show()
    }
}