package com.example.cs_topics_project_test.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.CheckBox
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.task.Task
import com.example.cs_topics_project_test.task.TaskCompleted

class CalendarAdapterCompleted(private val tasks: MutableList<TaskCompleted>) : RecyclerView.Adapter<CalendarAdapterCompleted.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.textViewTaskName)
        val taskCheckBox: CheckBox = itemView.findViewById(R.id.checkBoxTask)
        val taskCompletedDate: TextView = itemView.findViewById(R.id.textViewTaskCompletedDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task_calendar_completed, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskName.text = task.getTaskName()
        holder.taskCheckBox.isChecked = task.isTaskCompleted()
        holder.taskCompletedDate.text = task.getTaskCompletedDate().toString()
        holder.taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) task.taskCompleted()
            else task.taskNotCompleted()
        }
    }

    override fun getItemCount(): Int = tasks.size
}