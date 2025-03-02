package com.s92075608.taskmonitor_unistudent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView userName, userEmail;
    private EditText currentPassword, newPassword, confirmNewPassword;
    private Button updateUserDataButton, backButton;
    private DatabaseHelper dbHelper;
    private String userNIC; // For retrieving logged-in user's details

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize UI components
        profileImage = findViewById(R.id.profileImage);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        currentPassword = findViewById(R.id.currentPassword);
        newPassword = findViewById(R.id.newPassword);
        confirmNewPassword = findViewById(R.id.confirmNewPassword);
        updateUserDataButton = findViewById(R.id.updateUserDataButton);
        backButton = findViewById(R.id.backButton);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Get userNIC passed from LoginActivity (assuming it was passed via Intent)
        Intent intent = getIntent();
        userNIC = intent.getStringExtra("USER_NIC");

        // Load user details
        loadUserDetails();

        // Update password logic
        updateUserDataButton.setOnClickListener(v -> updatePassword());

        // Back button functionality
        backButton.setOnClickListener(v -> finish());
    }

    private void loadUserDetails() {
        Cursor cursor = dbHelper.getUserDetailsByNIC(userNIC);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String fullName = cursor.getString(cursor.getColumnIndex("first_name")) + " " +
                    cursor.getString(cursor.getColumnIndex("last_name"));
            @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("email"));

            // Set data to UI
            userName.setText(fullName);
            userEmail.setText(email);

            cursor.close();
        } else {
            Toast.makeText(this, "Failed to load user details.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updatePassword() {
        String currentPass = currentPassword.getText().toString().trim();
        String newPass = newPassword.getText().toString().trim();
        String confirmPass = confirmNewPassword.getText().toString().trim();

        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
        } else if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
        } else {
            boolean isUpdated = dbHelper.updatePassword(userNIC, currentPass, newPass);
            if (isUpdated) {
                Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Current password is incorrect!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
