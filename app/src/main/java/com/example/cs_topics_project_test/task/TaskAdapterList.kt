package com.example.cs_topics_project_test.task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.CheckBox
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.function.DateAndTime

class TaskAdapterList(private val tasks: MutableList<Task>, private val listener: TaskListener) : RecyclerView.Adapter<TaskAdapterList.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.textViewTaskName)
        val taskCheckBox: CheckBox = itemView.findViewById(R.id.checkBoxTask)
        val taskDescription: TextView = itemView.findViewById(R.id.textViewTaskDescription)
        val taskDueDate: TextView = itemView.findViewById(R.id.textViewTaskDate)
        val taskDueTime: TextView = itemView.findViewById(R.id.textViewTaskTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskName.text = task.getTaskName()
        holder.taskDescription.text = task.getTaskDescription()
        holder.taskCheckBox.isChecked = task.isTaskCompleted()
        holder.taskDueDate.text = task.getDueDate().toString()
        holder.taskDueTime.text = task.getDueTime().toString()
        holder.taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                task.taskCompleted()

                val date = DateAndTime(task.getDueDate(), task.getDueTime())
                val taskD = TaskDetail(task.getTaskName(), task.getTaskDescription())
                // taskD.taskCompleted()

                val taskC = TaskCompleted(TaskManager.todayDate, date, taskD)
                TaskDataStructure.processCompletedTask(date, taskD)
                // TaskManager.tasksCompleted.add(taskC)
                listener.onTaskCompleted(taskC)

                Toast.makeText(holder.itemView.context, "Marked task ${task.getTaskName()} as completed; Number of tasks: ${tasks.size}; position: ${position}", Toast.LENGTH_SHORT).show()
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
    fun updateList(newTasks: MutableList<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged() // Refresh RecyclerView
    }
}