package com.s92075608.taskmonitor_unistudent;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewAllTasksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private TaskDatabaseHelper dbHelper;
    private ImageButton addTaskButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        recyclerView = findViewById(R.id.task_list);
        addTaskButton = findViewById(R.id.add_task_button);
        dbHelper = new TaskDatabaseHelper(this);

        taskList = new ArrayList<>();
        loadTasks();

        taskAdapter = new TaskAdapter(this, taskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskAdapter);

        addTaskButton.setOnClickListener(v -> {
            startActivity(new Intent(ViewAllTasksActivity.this, AddTaskActivity.class));
        });
    }

    private void loadTasks() {
        Cursor cursor = dbHelper.getAllTasks();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(TaskDatabaseHelper.COLUMN_ID));
                @SuppressLint("Range") String taskName = cursor.getString(cursor.getColumnIndex(TaskDatabaseHelper.COLUMN_TASK_NAME));
                @SuppressLint("Range") String taskType = cursor.getString(cursor.getColumnIndex(TaskDatabaseHelper.COLUMN_TASK_TYPE));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(TaskDatabaseHelper.COLUMN_DESCRIPTION));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(TaskDatabaseHelper.COLUMN_DATE));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(TaskDatabaseHelper.COLUMN_TIME));
                @SuppressLint("Range") String status = cursor.getString(cursor.getColumnIndex(TaskDatabaseHelper.COLUMN_STATUS));

                if (!status.equals("completed")) {
                    Task task = new Task(id, taskName, taskType, description, date, time);
                    taskList.add(task);
                }
            }
            cursor.close();
        }
    }



}