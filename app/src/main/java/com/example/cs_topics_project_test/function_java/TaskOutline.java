package com.example.cs_topics_project_test.function_java;

public interface TaskOutline {
    String getTaskName(); // the name of the task
    Date getDueDate(); // the due date
    Time getDueTime(); // the due time
    boolean isTaskCompleted(); // returns whether the task is complete
    String getTaskDescription(); // returns task description

    // Modifiers
    void setTaskName(String name); // resets the task name
    void setDueDate(Date date); // changes due date
    void setDueTime(Time time); // changes the time that tasks was due
    void taskCompleted(); // sets task to completed
    void taskNotCompleted(); // sets task to incomplete
    void setTaskDescription(String description); // resets the task description
}
