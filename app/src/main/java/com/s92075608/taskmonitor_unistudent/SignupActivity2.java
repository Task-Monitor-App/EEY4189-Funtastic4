package com.s92075608.taskmonitor_unistudent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity2 extends AppCompatActivity {

    private EditText firstNameInput, lastNameInput, emailInput, nicInput, passwordInput, confirmPasswordInput;
    private Button signUpButton;
    private TextView loginRedirectText;
    private DatabaseHelper2 dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        // Initialize UI components
        firstNameInput = findViewById(R.id.firstNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        emailInput = findViewById(R.id.emailInput);
        nicInput = findViewById(R.id.nicInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        signUpButton = findViewById(R.id.signUpButton);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper2(this);

        // Sign Up button functionality
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameInput.getText().toString().trim();
                String lastName = lastNameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String nic = nicInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String confirmPassword = confirmPasswordInput.getText().toString().trim();

                // Validate inputs
                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || nic.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(SignupActivity2.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignupActivity2.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                } else {
                    // Add user to database
                    boolean isInserted = dbHelper.add_admin(firstName, lastName, email, nic, password);
                    if (isInserted) {
                        Toast.makeText(SignupActivity2.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                        // Redirect to login
                        Intent intent = new Intent(SignupActivity2.this, LoginActivity2.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignupActivity2.this, "NIC already exists. Please use a different NIC.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Redirect to login page
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity2.this, LoginActivity2.class);
                startActivity(intent);
                finish();
            }
        });
    }
    }
