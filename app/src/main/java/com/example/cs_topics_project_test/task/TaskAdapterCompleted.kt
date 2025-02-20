package com.example.cs_topics_project_test.task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R

class TaskAdapterCompleted(private val tasks: MutableList<TaskCompleted>) : RecyclerView.Adapter<TaskAdapterCompleted.TaskViewHolder>(), TaskListener {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.textViewTaskName)
        val taskCheckBox: CheckBox = itemView.findViewById(R.id.checkBoxTask)
        val taskDescription: TextView = itemView.findViewById(R.id.textViewTaskDescription)
        val taskCompletedDate: TextView = itemView.findViewById(R.id.textViewTaskCompletedDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task_completed, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskName.text = task.getTaskName()
        holder.taskDescription.text = task.getTaskDescription()
        holder.taskCheckBox.isChecked = task.isTaskCompleted()
        holder.taskCompletedDate.text = task.getTaskCompletedDate().toString() // ??
    }

    override fun getItemCount(): Int = tasks.size

    override fun onTaskCompleted(task: TaskCompleted) {
        tasks.add(task)
        notifyItemInserted(tasks.size - 1)
    }
}