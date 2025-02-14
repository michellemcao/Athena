package com.example.cs_topics_project_test.task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.function.Date
import com.example.cs_topics_project_test.function.Time
import java.util.Calendar

// to add new task
class TaskActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    private val localTasks = mutableListOf<Task>() // local tasks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        // val tasks = mutableListOf<Task>()
        taskAdapter = TaskAdapter(localTasks)

        val recyclerViewTasks: RecyclerView = findViewById(R.id.recyclerViewTasks) // to display recently added tasks
        val buttonAddTask: Button = findViewById(R.id.buttonAddTask) // to add the task into TaskManager
        val editTextTaskName: EditText = findViewById(R.id.editTextTaskName) // the task name
        val editTextTaskDescription: EditText = findViewById(R.id.editTextTaskDescription) // the task description
        val textViewDate: TextView = findViewById(R.id.textViewDate) // the task due date
        val textViewTime: TextView = findViewById(R.id.textViewTime) // the task due time
        val buttonBack: Button = findViewById(R.id.buttonBack) // to go back to TaskFragment page
        var dueDate: Date = Date(2025, 1, 1)
        var dueTime: Time = Time(12, 0, true)

        // setting up recyclerView
        recyclerViewTasks.adapter = taskAdapter
        recyclerViewTasks.layoutManager = LinearLayoutManager(this)

        // adding tasks
        buttonAddTask.setOnClickListener {
            val taskName = editTextTaskName.text.toString()
            val taskDescription = editTextTaskDescription.text.toString()
            if (taskName.isNotBlank() && taskDescription.isNotBlank()) {
                val task = Task(
                    taskName,
                    dueDate,
                    dueTime,
                    taskDescription
                )
                Toast.makeText(this, "Adding task: $taskName", Toast.LENGTH_SHORT).show()
                localTasks.add(task)
                TaskManager.tasks.add(task)
                taskAdapter.notifyDataSetChanged() // do we need it?

                // resetting all the fields
                editTextTaskName.text.clear()
                editTextTaskDescription.text.clear()
                textViewDate.text = "Select due date"
                textViewTime.text = "Select due time"
            } else {
                Toast.makeText(this, "Oopsie! Required field is blank.", Toast.LENGTH_SHORT).show()
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
            finish()
            //startActivity(Intent(this, TaskView::class.java)) // change to go to previous slide, not to Home
        }
    }

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

    private fun showTimePickerDialog(onTimeSelected: (Time) -> Unit) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val isPM = selectedHour >= 12
                val adjustedHour = if (selectedHour % 12 == 0) 12 else selectedHour % 12
                val selectedTime = Time(adjustedHour, selectedMinute, isPM)
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