package com.example.cs_topics_project_test.task

import android.content.res.ColorStateList
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.CheckBox
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.cs_topics_project_test.R
import com.example.cs_topics_project_test.function.DateAndTime
import com.example.cs_topics_project_test.function.Time
import com.example.cs_topics_project_test.themes.ThemeManager
import java.util.Calendar

class TaskAdapterList(private val tasks: MutableList<Task>, private val listener: TaskListener) : RecyclerView.Adapter<TaskAdapterList.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.textViewTaskName)
        val taskCheckBox: CheckBox = itemView.findViewById(R.id.checkBoxTask)
        val taskDescription: TextView = itemView.findViewById(R.id.textViewTaskDescription)
        val taskDueDate: TextView = itemView.findViewById(R.id.textViewTaskDate)
        val taskDueTime: TextView = itemView.findViewById(R.id.textViewTaskTime)
        val due: TextView = itemView.findViewById(R.id.taskDue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskName.text = task.getTaskName()
        holder.taskDescription.text = task.getTaskDescription()
        holder.taskCheckBox.isChecked = task.isTaskCompleted()
        holder.taskCheckBox.buttonTintList = ColorStateList.valueOf(ThemeManager.currentThemeColors!!.completedText)
        holder.taskDueDate.text = task.getDueDate().toString()
        holder.taskDueTime.text = task.getDueTime().toString()
        if (ThemeManager.currentThemeName == "blackberry") {
            val color = "#FFFFFF".toColorInt()
            holder.taskName.setTextColor(color)
            holder.taskDescription.setTextColor(color)
            holder.taskDueDate.setTextColor(color)
            holder.taskDueTime.setTextColor(color)
            holder.due.setTextColor(color)
        }
        holder.taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                task.taskCompleted()

                val date = DateAndTime(task.getDueDate(), task.getDueTime())
                val taskD = TaskDetail(task.getTaskName(), task.getTaskDescription())
                // taskD.taskCompleted()

                // get time when task is completed
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY) // 24-hour format; 0 - 23
                val minute = calendar.get(Calendar.MINUTE)
                val completedDate = DateAndTime(TaskManager.todayDate, Time(hour, minute))

                // val taskC = TaskCompleted(TaskManager.todayDate, date, taskD)
                val taskC = TaskCompleted(completedDate, date, taskD)
                TaskDataStructure.processCompletedTask(completedDate, date, taskD)
                // TaskManager.tasksCompleted.add(taskC)
                listener.onTaskCompleted(taskC)

                Toast.makeText(holder.itemView.context, "Completed task: ${task.getTaskName()}. Hooray!!", Toast.LENGTH_SHORT).show()
                tasks.removeAt(position) // adapter removal of position
                notifyDataSetChanged()
            }
            else task.taskNotCompleted()
        }
        holder.itemView.setOnLongClickListener {
            listener.onTaskPressed(task, position, tasks, this)
            true
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
    fun addTask(task: Task) {
        tasks.add(task)
        // notifyItemInserted(tasks.size - 1)
        notifyDataSetChanged()
    }
}