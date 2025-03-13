package com.s92075608.taskmonitor_unistudent;



import android.app.DatePickerDialog;

import android.app.TimePickerDialog;

import android.content.Intent;

import android.os.Bundle;

import android.widget.ArrayAdapter;

import android.widget.Button;

import android.widget.EditText;

import android.widget.Spinner;

import android.widget.TextView;

import android.widget.Toast;



import androidx.appcompat.app.AppCompatActivity;



import java.util.Calendar;



public class AddTaskActivity extends AppCompatActivity {



    private EditText etTaskName, etDescription;

    private Spinner spTaskType;

    private TextView tvSelectedDate, tvSelectedTime;

    private Button btnSelectDate, btnSelectTime, btnAddTask;



    private Calendar calendar;

    private TaskDatabaseHelper dbHelper;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_task);



        etTaskName = findViewById(R.id.etTaskName);

        etDescription = findViewById(R.id.etDescription);

        spTaskType = findViewById(R.id.spTaskType);

        tvSelectedDate = findViewById(R.id.tvSelectedDate);

        tvSelectedTime = findViewById(R.id.tvSelectedTime);

        btnSelectDate = findViewById(R.id.btnSelectDate);

        btnSelectTime = findViewById(R.id.btnSelectTime);

        btnAddTask = findViewById(R.id.btnAddTask);



        calendar = Calendar.getInstance();

        dbHelper = new TaskDatabaseHelper(this);



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(

                this,

                R.array.spTaskType,

                android.R.layout.simple_spinner_item

        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spTaskType.setAdapter(adapter);



        btnSelectDate.setOnClickListener(view -> showDatePickerDialog());

        btnSelectTime.setOnClickListener(view -> showTimePickerDialog());



        btnAddTask.setOnClickListener(view -> addTask());

    }



    private void showDatePickerDialog() {

        int year = calendar.get(Calendar.YEAR);

        int month = calendar.get(Calendar.MONTH);

        int day = calendar.get(Calendar.DAY_OF_MONTH);



        DatePickerDialog datePickerDialog = new DatePickerDialog(

                this,

                (view, selectedYear, selectedMonth, selectedDay) -> {

                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;

                    tvSelectedDate.setText("Selected Date: " + selectedDate);

                },

                year, month, day

        );



        datePickerDialog.show();

    }



    private void showTimePickerDialog() {

        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        int minute = calendar.get(Calendar.MINUTE);



        TimePickerDialog timePickerDialog = new TimePickerDialog(

                this,

                (view, selectedHour, selectedMinute) -> {

                    String selectedTime = selectedHour + ":" + (selectedMinute < 10 ? "0" + selectedMinute : selectedMinute);

                    tvSelectedTime.setText("Selected Time: " + selectedTime);

                },

                hour, minute, true

        );



        timePickerDialog.show();

    }



    private void addTask() {

        String taskName = etTaskName.getText().toString().trim();

        String taskType = spTaskType.getSelectedItem().toString();

        String description = etDescription.getText().toString().trim();

        String selectedDate = tvSelectedDate.getText().toString().replace("Selected Date: ", "").trim();

        String selectedTime = tvSelectedTime.getText().toString().replace("Selected Time: ", "").trim();



        if (taskName.isEmpty()) {

            Toast.makeText(this, "Please enter task name", Toast.LENGTH_SHORT).show();

            return;

        }



        if (selectedDate.equals("Not Set") || selectedTime.equals("Not Set")) {

            Toast.makeText(this, "Please select date and time", Toast.LENGTH_SHORT).show();

            return;

        }



        // Save task to database

        long taskId = dbHelper.addTask(new Task(taskName, taskType, description, selectedDate, selectedTime));



        if (taskId > 0) {

            Toast.makeText(this, "Task Added Successfully!", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(AddTaskActivity.this, ViewAllTasksActivity.class));

            finish();

        } else {

            Toast.makeText(this, "Error adding task", Toast.LENGTH_SHORT).show();

        }

    }

}