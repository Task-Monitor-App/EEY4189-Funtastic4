package com.s92075608.taskmonitor_unistudent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "users";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_NIC = "nic";
    private static final String COLUMN_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FIRST_NAME + " TEXT, " +
                COLUMN_LAST_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_NIC + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            // Example of schema update
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN profile_image_url TEXT");
        }
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean addUser(String firstName, String lastName, String email, String nic, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_NIC, nic);
        values.put(COLUMN_PASSWORD, hashPassword(password));

        try {
            long result = db.insert(TABLE_NAME, null, values);
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    public boolean authenticateUser(String nic, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String hashedPassword = hashPassword(password);

        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{COLUMN_ID},
                COLUMN_NIC + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{nic, hashedPassword},
                null, null, null
        );

        boolean isAuthenticated = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isAuthenticated;
    }

    public boolean updatePassword(String nic, String currentPassword, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        String hashedCurrentPassword = hashPassword(currentPassword);
        String hashedNewPassword = hashPassword(newPassword);

        // Check if current password matches the one stored in the database
        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{COLUMN_PASSWORD},
                COLUMN_NIC + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{nic, hashedCurrentPassword},
                null, null, null
        );

        if (cursor.getCount() > 0) {
            // Update the password with the new hashed password
            ContentValues values = new ContentValues();
            values.put(COLUMN_PASSWORD, hashedNewPassword);

            int rowsUpdated = db.update(TABLE_NAME, values, COLUMN_NIC + "=?", new String[]{nic});
            cursor.close();
            db.close();
            return rowsUpdated > 0; // Return true if the password was successfully updated
        }

        cursor.close();
        db.close();
        return false; // Return false if current password does not match
    }

    public Cursor getUserDetailsByNIC(String nic) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                TABLE_NAME,
                new String[]{COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_EMAIL},
                COLUMN_NIC + "=?",
                new String[]{nic},
                null, null, null
        );
    }
}
