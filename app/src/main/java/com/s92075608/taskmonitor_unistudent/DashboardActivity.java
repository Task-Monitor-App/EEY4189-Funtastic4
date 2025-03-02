package com.s92075608.taskmonitor_unistudent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard); // Link the layout file

        // Initialize buttons
        ImageButton reportButton = findViewById(R.id.reportButton);
        ImageButton addButton = findViewById(R.id.addButton);
        ImageButton viewButton = findViewById(R.id.viewButton);
        ImageButton profileButton = findViewById(R.id.profileButton);
        ImageButton CalenButton = findViewById(R.id.CalenButton);
        ImageButton NotifiButtonn = findViewById(R.id.NotifiButton);


        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the userNIC to the ProfileActivity
                Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
                intent.putExtra("USER_NIC", "123456789V"); // Replace with actual userNIC
                startActivity(intent);
            }
        });

        CalenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the userNIC to the ProfileActivity
                Intent intent = new Intent(DashboardActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        NotifiButtonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the userNIC to the ProfileActivity
                Intent intent = new Intent(DashboardActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });
        // Set click listeners
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, CompletedTasksActivity.class);
                startActivity(intent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a message or navigate to the Add Task Activity
                Toast.makeText(DashboardActivity.this, "Add Task Button Clicked", Toast.LENGTH_SHORT).show();

                // Navigate to AddTaskActivity
                Intent intent = new Intent(DashboardActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a message or navigate to the Add Task Activity
                Toast.makeText(DashboardActivity.this, "View Task Button Clicked", Toast.LENGTH_SHORT).show();

                // Navigate to AddTaskActivity
                Intent intent = new Intent(DashboardActivity.this, ViewAllTasksActivity.class);
                startActivity(intent);
            }
        });

        // Select Reminder Technique

        // Initialize buttons
        Button pomodoro_Button = findViewById(R.id.pomodoro_button);
        Button space_Button = findViewById(R.id.space_button);

        //Pomodoro Technique
        pomodoro_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PomodoroTimerActivity.class);
                startActivity(intent);
            }
        });
         //Spase Repetition Technique
        space_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DashboardActivity.this, SpaceRepetitionActivity.class);
                    startActivity(intent);
                }
        });
    }
}

