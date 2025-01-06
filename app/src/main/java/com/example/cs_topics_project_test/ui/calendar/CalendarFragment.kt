package com.example.cs_topics_project_test.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.cs_topics_project_test.R

class CalendarFragment : Fragment() {

    //private lateinit var calendarView: CalendarView
    //private lateinit var dateViewVar: TextView

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

        // to update the date on calendar
        calendarView.setOnDateChangeListener {_, year, month, day ->
            val date = ("Tasks for " + "%02d".format(month+1) + "/"
                    + "%02d".format(day) + "/"
                    + "%02d".format(year) + ":") // date displayed in calendar_main in mm/dd/yyyy format
            dateViewVar.text = date
        }
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