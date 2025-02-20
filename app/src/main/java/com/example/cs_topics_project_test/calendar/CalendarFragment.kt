package com.example.cs_topics_project_test.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.function.Date
import com.example.cs_topics_project_test.task.TaskDataStructure
import com.example.cs_topics_project_test.task.TaskManager
import java.text.SimpleDateFormat
import java.util.Locale

class CalendarFragment : Fragment() {

    // private lateinit var calendarView: CalendarView
    // private lateinit var dateViewVar: TextView
    private lateinit var calendarAdapter: CalendarAdapter
    private var targetDate : Date = TaskManager.todayDate // place holder date

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate layout for calendar view

        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        val dateViewVar = view.findViewById<TextView>(R.id.dateView)
        val recyclerViewCalendar : RecyclerView = view.findViewById(R.id.recyclerViewCalendar)

        val todayDateLong = calendarView.date

        // Convert to a readable format
        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val todayDate: String = sdf.format(java.util.Date(todayDateLong))
        //Toast.makeText(context, "Current date: " + currentDate, Toast.LENGTH_LONG).show();
        dateViewVar.text = buildString {
            append("Tasks for ")
            append(todayDate)
            append(":")
        }

        // to update the date on calendar
        calendarView.setOnDateChangeListener {_, year, month, day ->
            val date = (buildString {
                append("Tasks for ")
                append(month + 1)
                append("/")
                append(day)
                append("/")
                append(year)
                append(":")
            }) // date displayed in calendar_main in mm/dd/yyyy format
            dateViewVar.text = date

            targetDate = Date(year, month + 1, day)
            calendarAdapter = CalendarAdapter(TaskDataStructure.rangeDateTasks(targetDate))

            recyclerViewCalendar.adapter = calendarAdapter
            recyclerViewCalendar.layoutManager = LinearLayoutManager(activity)
            // Toast.makeText(activity, "Target Date: $targetDate", Toast.LENGTH_SHORT).show()
        }

        calendarAdapter = CalendarAdapter(TaskDataStructure.rangeDateTasks(targetDate))

        recyclerViewCalendar.adapter = calendarAdapter
        recyclerViewCalendar.layoutManager = LinearLayoutManager(activity)
    }
        //super.onCreate(savedInstanceState)
        // enableEdgeToEdge()

        //setContentView(R.layout.calendar_main)

        //calendarView stuff
        /*dateViewVar = findViewById(R.id.dateView)
        calendarView.setOnDateChangeListener {_, year, month, day ->
            val date = ("%02d".format(month+1) + "/"
                    + "%02d".format(day) + "/"
                    + "%02d".format(year)) // date displayed in calendar_main in mm/dd/yyyy format
            dateViewVar.text = date

        }*/
}