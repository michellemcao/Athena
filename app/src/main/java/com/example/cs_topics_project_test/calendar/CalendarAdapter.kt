package com.example.cs_topics_project_test.calendar

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.CheckBox
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.function.DateAndTime
import com.example.cs_topics_project_test.function.Time
import com.example.cs_topics_project_test.task.Task
import com.example.cs_topics_project_test.task.TaskCompleted
import com.example.cs_topics_project_test.task.TaskDataStructure
import com.example.cs_topics_project_test.task.TaskDetail
import com.example.cs_topics_project_test.task.TaskListener
import com.example.cs_topics_project_test.task.TaskManager
import java.util.Calendar

class CalendarAdapter(private val tasks: MutableList<Task>, private val listener: TaskListener) : RecyclerView.Adapter<CalendarAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.textViewTaskName)
        val taskCheckBox: CheckBox = itemView.findViewById(R.id.checkBoxTask)
        val taskDueTime: TextView = itemView.findViewById(R.id.textViewTaskTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task_calendar, parent, false)
        return TaskViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskName.text = task.getTaskName()
        holder.taskCheckBox.isChecked = task.isTaskCompleted()
        holder.taskDueTime.text = task.getDueTime().toString()
        /*holder.taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) task.taskCompleted()
            else task.taskNotCompleted()
        }*/
        holder.taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                task.taskCompleted()

                val date = DateAndTime(task.getDueDate(), task.getDueTime())
                val taskD = TaskDetail(task.getTaskName(), task.getTaskDescription())

                // get time when task is completed
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY) // 24-hour format; 0 - 23
                val minute = calendar.get(Calendar.MINUTE)
                val completedDate = DateAndTime(TaskManager.todayDate, Time(hour, minute))

                val taskC = TaskCompleted(completedDate, date, taskD)

                // update data structure in TaskDataStructure (global tasks), TaskManager (to-do list tasks), and calendar (real-time updatin!)
                TaskDataStructure.processCompletedTask(completedDate, date, taskD)
                listener.onTaskCompleted(taskC)
                TaskManager.tasksCompleted.add(taskC)
                if (task.getDueDate() < TaskManager.todayDate) TaskManager.init(3)
                else if (task.getDueDate() > TaskManager.todayDate) TaskManager.init(2)
                else TaskManager.init(1) //TaskManager.tasksDueToday.removeAt(position)

                Toast.makeText(holder.itemView.context, "Completed task: ${task.getTaskName()}. Hooray!!", Toast.LENGTH_SHORT).show()
                tasks.removeAt(position) // adapter removal of position
                notifyDataSetChanged()
            }
            else task.taskNotCompleted()
        }
    }

    override fun getItemCount(): Int = tasks.size

    /*fun addTask(task: Task) {
        tasks.add(task)
        notifyItemInserted(tasks.size - 1)
        Toast.makeText(context, "Task ${task.getTaskName()} in TaskListAdapter; Number of tasks: ${tasks.size}", Toast.LENGTH_SHORT).show()
    }*/
}