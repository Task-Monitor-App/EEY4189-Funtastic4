package com.s92075608.taskmonitor_unistudent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TASK_NAME = "task_name";
    public static final String COLUMN_TASK_TYPE = "task_type";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_STATUS = "status";  // Added to constructor

    public TaskDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TASK_NAME + " TEXT, " +
                COLUMN_TASK_TYPE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_STATUS + " TEXT DEFAULT 'pending'" +
                ")";
        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_TASKS + " ADD COLUMN " + COLUMN_STATUS + " TEXT DEFAULT 'pending'");
        }
    }

    public long addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, task.getTaskName());
        values.put(COLUMN_TASK_TYPE, task.getTaskType());
        values.put(COLUMN_DESCRIPTION, task.getDescription());
        values.put(COLUMN_DATE, task.getDate());
        values.put(COLUMN_TIME, task.getTime());
        values.put(COLUMN_STATUS, "pending");  // Ensuring a default status

        long id = db.insert(TABLE_TASKS, null, values);
        db.close();  // Closing DB to prevent leaks
        return id;
    }

    public Cursor getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_TASKS, null, null, null, null, null, COLUMN_DATE + " ASC");
    }

    // Fetch completed tasks with an optional query for search
    public Cursor getCompletedTasks(String query) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Base query to fetch completed tasks
        String sqlQuery = "SELECT * FROM " + TABLE_TASKS + " WHERE " + COLUMN_STATUS + " = 'completed'";

        // Add search filtering if a query is provided
        if (!query.isEmpty()) {
            sqlQuery += " AND " + COLUMN_TASK_NAME + " LIKE ?";
            return db.rawQuery(sqlQuery, new String[]{"%" + query + "%"});
        }

        return db.rawQuery(sqlQuery, null);
    }

    public Cursor getTaskById(long taskId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                TaskDatabaseHelper.TABLE_TASKS,
                null,
                TaskDatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(taskId)},
                null,
                null,
                null
        );
    }
}