package com.s92075608.taskmonitor_unistudent;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private List<Task> taskList;
    private TaskDatabaseHelper dbHelper;

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
        dbHelper = new TaskDatabaseHelper(context);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskName.setText(task.getTaskName());
        holder.taskType.setText(task.getTaskType());
        holder.taskDate.setText(task.getDate());
        holder.taskTime.setText(task.getTime());

        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditTaskActivity.class);
            intent.putExtra("task_id", task.getId());
            context.startActivity(intent);
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.deleteButton.setOnClickListener(v -> deleteTask(task.getId()));
        }
        holder.completeButton.setOnClickListener(v -> markTaskCompleted(task.getId()));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void deleteTask(long taskId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TaskDatabaseHelper.TABLE_TASKS, TaskDatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(taskId)});
        taskList.removeIf(task -> task.getId() == taskId);
        notifyDataSetChanged();
        Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show();
    }

    private void markTaskCompleted(long taskId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("UPDATE " + TaskDatabaseHelper.TABLE_TASKS + " SET " + TaskDatabaseHelper.COLUMN_STATUS + " = ? WHERE " + TaskDatabaseHelper.COLUMN_ID + " = ?", new Object[]{"completed", taskId});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            taskList.removeIf(task -> task.getId() == taskId);
        }
        notifyDataSetChanged();
        Toast.makeText(context, "Task marked as completed", Toast.LENGTH_SHORT).show();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public TextView taskName, taskType, taskDate, taskTime;
        public Button editButton, deleteButton, completeButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.taskName);
            taskType = itemView.findViewById(R.id.taskType);
            taskDate = itemView.findViewById(R.id.taskDate);
            taskTime = itemView.findViewById(R.id.taskTime);
            editButton = itemView.findViewById(R.id.btn_edit_task);
            deleteButton = itemView.findViewById(R.id.btn_delete_task);
            completeButton = itemView.findViewById(R.id.btn_complete_task);
        }
    }
}
