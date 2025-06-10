package com.example.cs_topics_project_test.task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.themes.ThemeManager

class TaskAdapterActivity(private val tasks: MutableList<Task>) : RecyclerView.Adapter<TaskAdapterActivity.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.textViewTaskName)
        val taskDescription: TextView = itemView.findViewById(R.id.textViewTaskDescription)
        val taskDue: TextView = itemView.findViewById(R.id.textViewTaskDateAndTime)
        val due: TextView = itemView.findViewById(R.id.taskDue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task_add, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskName.text = task.getTaskName()
        holder.taskDescription.text = task.getTaskDescription()
        holder.taskDue.text = task.getDateAndTime().toString()
        if (ThemeManager.currentThemeName == "blackberry") {
            val color = "#FFFFFF".toColorInt()
            holder.taskName.setTextColor(color)
            holder.taskDescription.setTextColor(color)
            holder.taskDue.setTextColor(color)
            holder.due.setTextColor(color)
        }
    }

    override fun getItemCount(): Int = tasks.size
}