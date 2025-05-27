package com.example.cs_topics_project_test.calendar

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.function.Date
import com.example.cs_topics_project_test.task.*
import com.kizitonwose.calendar.core.*
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.view.ViewContainer
import nl.dionsegijn.konfetti.core.*
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.O)
class CalendarFragment : Fragment(), TaskListener {

    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var calendarAdapterCompleted: CalendarAdapterCompleted
    private lateinit var konfettiView: KonfettiView
    private lateinit var dateViewVar: TextView
    private lateinit var monthViewVar: TextView
    private lateinit var calendarView: CalendarView
    private lateinit var recyclerViewCalendar: RecyclerView
    private lateinit var recyclerViewCalendarCompleted: RecyclerView

    private var selectedDate: LocalDate = LocalDate.now()
    private val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.getDefault())

    private var targetDate: Date = TaskManager.todayDate

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

        calendarView = view.findViewById(R.id.calendarView)
        dateViewVar = view.findViewById(R.id.dateView)
        monthViewVar = view.findViewById(R.id.monthView)
        konfettiView = view.findViewById(R.id.konfettiView)

        recyclerViewCalendar = view.findViewById(R.id.recyclerViewCalendar)
        recyclerViewCalendarCompleted = view.findViewById(R.id.recyclerViewCalendarCompleted)

        // Init Calendar range
        var currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(12)
        val lastMonth = currentMonth.plusMonths(12)
        val daysOfWeek = daysOfWeek()

        calendarView.setup(firstMonth, lastMonth, daysOfWeek.first())
        calendarView.scrollToMonth(currentMonth)

        monthViewVar.text = buildString {
            append(currentMonth.month.toString())
            append(" ")
            append(currentMonth.year.toString())
        }

        // Initial UI
        updateSelectedDate(LocalDate.now())

        val weekTitlesContainer = view.findViewById<ViewGroup>(R.id.titlesContainer) // editing the title of the weeks
        weekTitlesContainer.children
            .map { it as TextView }
            .forEachIndexed { index, textView ->
                val dayOfWeek = daysOfWeek[index]
                val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                textView.text = title
            }

        // Bind calendar cells
        calendarView.dayBinder = object : com.kizitonwose.calendar.view.MonthDayBinder<DayViewContainer> {
            override fun create(view: View): DayViewContainer {
                return DayViewContainer(view)
            }

            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.textView.text = day.date.dayOfMonth.toString()

                when {
                    day.date == LocalDate.now() && day.date == selectedDate -> {
                        container.textView.setBackgroundResource(R.drawable.background_circle_today)
                        container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    }
                    day.date == LocalDate.now() -> {
                        container.textView.setBackgroundResource(R.drawable.background_circle_outline_today)
                        container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.wintergreen))
                    }
                    day.date == selectedDate -> {
                        container.textView.setBackgroundResource(R.drawable.background_circle_selected)
                        container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

                    }
                    day.position == DayPosition.MonthDate -> {
                        container.textView.setBackgroundResource(R.drawable.background_circle_outline)
                        container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.bubblegum))
                    }
                    else -> {
                        container.textView.setBackgroundResource(R.drawable.background_circle_outline_light)
                        container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.cottoncandy))
                    }
                }

                container.view.setOnClickListener {
                    // updateSelectedDate(day.date)

                    // if (selectedDate != day.date) {
                    val oldDate = selectedDate
                    updateSelectedDate(day.date)

                    // Redraw both the old and new selected date
                    calendarView.notifyDateChanged(oldDate)
                    calendarView.notifyDateChanged(day.date)
                    // }
                }
            }
        }

        calendarView.monthScrollListener = { month ->
            currentMonth = month.yearMonth
            monthViewVar.text = buildString {
                append(currentMonth.month.toString())
                append(" ")
                append(currentMonth.year.toString())
            }
            if (selectedDate.month != month.yearMonth.month) {
                updateSelectedDate(month.yearMonth.atDay(1)) // Optional: update selected month
                calendarView.notifyDateChanged(selectedDate)
            }
        }

        // RecyclerView Adapters
        calendarAdapterCompleted = CalendarAdapterCompleted(TaskDataStructure.getTasksCompletedRange(targetDate), this)
        calendarAdapter = CalendarAdapter(TaskDataStructure.rangeDateTasks(targetDate), this)

        recyclerViewCalendarCompleted.adapter = calendarAdapterCompleted
        recyclerViewCalendarCompleted.layoutManager = LinearLayoutManager(activity)

        recyclerViewCalendar.adapter = calendarAdapter
        recyclerViewCalendar.layoutManager = LinearLayoutManager(activity)
    }

    private fun updateSelectedDate(newDate: LocalDate) {
        selectedDate = newDate
        targetDate = Date(selectedDate.year, selectedDate.monthValue, selectedDate.dayOfMonth)

        dateViewVar.text = "Tasks for ${selectedDate.format(formatter)}:"

        calendarAdapterCompleted = CalendarAdapterCompleted(TaskDataStructure.getTasksCompletedRange(targetDate), this)
        calendarAdapter = CalendarAdapter(TaskDataStructure.rangeDateTasks(targetDate), this)

        recyclerViewCalendarCompleted.adapter = calendarAdapterCompleted
        recyclerViewCalendarCompleted.layoutManager = LinearLayoutManager(activity)

        recyclerViewCalendar.adapter = calendarAdapter
        recyclerViewCalendar.layoutManager = LinearLayoutManager(activity)
    }

    override fun onTaskCompleted(task: TaskCompleted) {
        // creating confetti, explosion style
        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
            position = Position.Relative(0.5, 0.3)
        )
        konfettiView.start(party)
        calendarAdapterCompleted.addCompletedTask(task)
    }

    override fun onTask(num: Int, task: Task) {
        calendarAdapter.addTask(task)
    }

    override fun onTaskPressed(task: Task, position: Int, taskList: MutableList<Task>, adapter: TaskAdapterList) = Unit

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


    inner class DayViewContainer(view: View) : ViewContainer(view) {
        val textView: TextView = view.findViewById<TextView>(R.id.calendarDayText)
        /*val textView: TextView = TextView(view.context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }


        init {
            (view as ViewGroup).addView(textView)
        }*/
    }
}