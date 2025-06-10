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

class TaskAdapterCompleted(private val tasks: MutableList<TaskCompleted>, private val listener: TaskListener) : RecyclerView.Adapter<TaskAdapterCompleted.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.textViewTaskName)
        val taskCheckBox: CheckBox = itemView.findViewById(R.id.checkBoxTask)
        val taskDescription: TextView = itemView.findViewById(R.id.textViewTaskDescription)
        val taskCompletedOn: TextView = itemView.findViewById(R.id.taskCompletedOn)
        val taskCompletedDate: TextView = itemView.findViewById(R.id.textViewTaskCompletedDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task_completed, parent, false)
        return TaskViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskName.text = task.getTaskName()
        holder.taskDescription.text = task.getTaskDescription()
        if (ThemeManager.currentThemeName == "blackberry") {
            val color = "#FFFFFF".toColorInt()
            holder.taskName.setTextColor(color)
            holder.taskDescription.setTextColor(color)
        }
        holder.taskCheckBox.isChecked = task.isTaskCompleted()
        holder.taskCheckBox.buttonTintList = ColorStateList.valueOf(ThemeManager.currentThemeColors!!.completedText)
        holder.taskCompletedDate.text = task.getTaskCompletedDate().toString()
        holder.taskCompletedDate.setTextColor(ThemeManager.currentThemeColors!!.completedText)
        holder.taskCompletedOn.setTextColor(ThemeManager.currentThemeColors!!.completedText)
        holder.taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                task.taskNotCompleted()

                val dueDate = task.getDueDate()
                val date = DateAndTime(task.getDueDate(), task.getDueTime())
                val dateC = task.getTaskCompletedDate()
                val taskD = TaskDetail(task.getTaskName(), task.getTaskDescription())
                // taskD.taskCompleted()

                // val taskC = TaskCompleted(TaskManager.todayDate, date, taskD)
                // val taskC = TaskCompleted(dateC, date, taskD)
                val taskA = Task(task.getTaskName(), task.getTaskDescription(), task.getDueDate(), task.getDueTime())
                TaskDataStructure.processTask(dateC, date, taskD)
                // TaskManager.tasksCompleted.add(taskC)

                if (dueDate < TaskManager.todayDate) listener.onTask(3, taskA) // TaskManager.tasksPastDue.add(taskA)
                else if (dueDate > TaskManager.todayDate) listener.onTask(2, taskA) // TaskManager.tasksDueLater.add(taskA)
                else listener.onTask(1, taskA) // TaskManager.tasksDueToday.add(taskA)

                // listener.onTaskCompleted(taskC)

                Toast.makeText(holder.itemView.context, "UnCompleted task: ${task.getTaskName()}. Miss clicked?", Toast.LENGTH_SHORT).show()
                tasks.removeAt(position) // adapter removal of position
                notifyDataSetChanged()
            }
            else task.taskCompleted()
        }
    }

    override fun getItemCount(): Int = tasks.size

    fun addCompletedTask(task: TaskCompleted) {
        tasks.add(task)
        notifyItemInserted(tasks.size - 1)
    }
}