package com.s92075608.taskmonitor_unistudent;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CompletedTasksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CompletedTaskAdapter completedTaskAdapter;
    private List<Task> completedTaskList;
    private TaskDatabaseHelper dbHelper;
    private EditText searchTask;
    private Button backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_tasks);

        // Initialize UI components
        recyclerView = findViewById(R.id.completed_task_list);
        searchTask = findViewById(R.id.search_completed_tasks);
        backButton = findViewById(R.id.back_button);
        dbHelper = new TaskDatabaseHelper(this);

        // Load completed tasks
        completedTaskList = new ArrayList<>();
        fetchCompletedTasks();

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        completedTaskAdapter = new CompletedTaskAdapter(completedTaskList);
        recyclerView.setAdapter(completedTaskAdapter);

        // Search functionality
        searchTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTasks(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Back button click listener
        backButton.setOnClickListener(v -> finish());
    }

    private void fetchCompletedTasks() {
        Cursor cursor = dbHelper.getCompletedTasks("");  // Pass an empty string for no filtering
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String date = cursor.getString(2);
                completedTaskList.add(new Task(id, name, date));
            }
            cursor.close();
        }
        if (completedTaskAdapter != null) {
            completedTaskAdapter.notifyDataSetChanged();
        }
    }

    private void filterTasks(String query) {
        List<Task> filteredList = new ArrayList<>();
        for (Task task : completedTaskList) {
            if (task.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(task);
            }
        }
        completedTaskAdapter.updateList(filteredList);
    }
}
