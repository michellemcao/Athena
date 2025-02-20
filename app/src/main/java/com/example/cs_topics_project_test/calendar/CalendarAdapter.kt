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

class CalendarAdapter(private val tasks: MutableList<Task>) : RecyclerView.Adapter<CalendarAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.textViewTaskName)
        val taskCheckBox: CheckBox = itemView.findViewById(R.id.checkBoxTask)
        val taskDueTime: TextView = itemView.findViewById(R.id.textViewTaskTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task_calendar, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskName.text = task.getTaskName()
        holder.taskCheckBox.isChecked = task.isTaskCompleted()
        holder.taskDueTime.text = task.getDueTime().toString()
        holder.taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) task.taskCompleted()
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