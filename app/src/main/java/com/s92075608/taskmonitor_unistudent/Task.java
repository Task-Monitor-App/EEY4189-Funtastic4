package com.s92075608.taskmonitor_unistudent;

public class Task {
    private long id;
    private String taskName;
    private String taskType;
    private String description;
    private String date;
    private String time;

    // Constructor for inserting a new task
    public Task(String taskName, String taskType, String description, String date, String time) {
        this.taskName = taskName;
        this.taskType = taskType;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    // Constructor for fetching a task from the database
    public Task(long id, String taskName, String taskType, String description, String date, String time) {
        this.id = id;
        this.taskName = taskName;
        this.taskType = taskType;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    public Task(int id, String name, String date) {
        // Constructor for tasks that don't have the full details (e.g., in a simple list view)
        this.id = id;
        this.taskName = name;
        this.date = date;
    }

    // Getter and Setter methods
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskType() {
        return taskType;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return taskName;
    }
}
