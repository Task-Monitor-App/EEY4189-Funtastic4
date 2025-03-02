package com.s92075608.taskmonitor_unistudent;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class EditTaskActivity extends AppCompatActivity {

    private EditText taskNameEditText;
    private Spinner taskTypeSpinner;
    private TextView selectedDateTextView, selectedTimeTextView;
    private Button saveTaskButton, selectDateButton, selectTimeButton;
    private long taskId;
    private TaskDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        dbHelper = new TaskDatabaseHelper(this);

        taskNameEditText = findViewById(R.id.edit_task_name);
        taskTypeSpinner = findViewById(R.id.edit_task_type);
        selectedDateTextView = findViewById(R.id.tvSelectedDate);
        selectedTimeTextView = findViewById(R.id.tvSelectedTime);
        saveTaskButton = findViewById(R.id.btn_save_task);
        selectDateButton = findViewById(R.id.edit_task_date);
        selectTimeButton = findViewById(R.id.edit_task_time);

        taskId = getIntent().getLongExtra("task_id", -1);
        Log.d("EditTaskActivity", "Task ID: " + taskId);
        loadTaskData(taskId);

        saveTaskButton.setOnClickListener(v -> saveTask());

        selectDateButton.setOnClickListener(v -> {
            // Date Picker Logic
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        // Format the selected date as needed
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        selectedDateTextView.setText(selectedDate);
                        Log.d("EditTaskActivity", "Selected Date: " + selectedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        selectTimeButton.setOnClickListener(v -> {
            // Time Picker Logic
            Calendar calendar = Calendar.getInstance();
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this,
                    (view, hourOfDay, minute) -> {
                        // Format the selected time as needed
                        String selectedTime = hourOfDay + ":" + minute;
                        selectedTimeTextView.setText(selectedTime);
                        Log.d("EditTaskActivity", "Selected Time: " + selectedTime);
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
            );
            timePickerDialog.show();
        });
    }

    private void loadTaskData(long taskId) {
        Cursor cursor = dbHelper.getTaskById(taskId);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String taskName = cursor.getString(cursor.getColumnIndex(TaskDatabaseHelper.COLUMN_TASK_NAME));
            @SuppressLint("Range") String taskType = cursor.getString(cursor.getColumnIndex(TaskDatabaseHelper.COLUMN_TASK_TYPE));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(TaskDatabaseHelper.COLUMN_DATE));
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(TaskDatabaseHelper.COLUMN_TIME));

            taskNameEditText.setText(taskName);
            // Set the task type spinner to the appropriate position based on taskType
            selectedDateTextView.setText(date);
            selectedTimeTextView.setText(time);
            cursor.close();

            Log.d("EditTaskActivity", "Loaded Task: " + taskName + ", " + taskType + ", " + date + ", " + time);
        }
    }

    private void saveTask() {
        String taskName = taskNameEditText.getText().toString();
        String taskType = taskTypeSpinner.getSelectedItem().toString();
        String date = selectedDateTextView.getText().toString();
        String time = selectedTimeTextView.getText().toString();

        // Ensure that the task name and other details are valid
        if (taskName.isEmpty() || date.equals("Not Set") || time.equals("Not Set")) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Log values before saving
        Log.d("EditTaskActivity", "Saving Task: " + taskName + ", " + taskType + ", " + date + ", " + time);

        ContentValues values = new ContentValues();
        values.put(TaskDatabaseHelper.COLUMN_TASK_NAME, taskName);
        values.put(TaskDatabaseHelper.COLUMN_TASK_TYPE, taskType);
        values.put(TaskDatabaseHelper.COLUMN_DATE, date);
        values.put(TaskDatabaseHelper.COLUMN_TIME, time);

        int rowsUpdated = dbHelper.getWritableDatabase().update(
                TaskDatabaseHelper.TABLE_TASKS,
                values,
                TaskDatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(taskId)}
        );

        if (rowsUpdated > 0) {
            Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close activity and return to the previous screen
        } else {
            Toast.makeText(this, "Failed to update task", Toast.LENGTH_SHORT).show();
        }
    }
}