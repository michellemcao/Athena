package com.example.cs_topics_project_test.function_java;

public class Task implements TaskOutline {
    private String taskName;
    private Date dueDate;
    private Time dueTime;
    private boolean isCompleted;
    private String taskDescription;

    public Task(String name, Date date, Time time, String description) {
        this.taskName = name;
        this.dueDate = date;
        this.dueTime = time;
        this.taskDescription = description;
        this.isCompleted = false;
    }

    // return whether task is completed and modify it
    public void taskCompleted() {
        this.isCompleted = true;
    }
    public void taskNotCompleted() {
        this.isCompleted = false;
    }
    public boolean isTaskCompleted() {
        return this.isCompleted;
    }

    // return task name/description and modify it
    public String getTaskName() {
        return this.taskName;
    }
    public void setTaskName(String name) {
        this.taskName = name;
    }
    public String getTaskDescription() {
        return this.taskDescription;
    }
    public void setTaskDescription(String description) {
        this.taskDescription = description;
    }

    // set due date/time and modify it
    public Date getDueDate() {
        return this.dueDate;
    }
    public void setDueDate(Date date) {
        this.dueDate = date;
    }
    public Time getDueTime() {
        return this.dueTime;
    }
    public void setDueTime(Time time) {
        this.dueTime = time;
    }
}
