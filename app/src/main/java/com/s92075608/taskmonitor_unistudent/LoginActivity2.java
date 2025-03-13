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

public class LoginActivity2 extends AppCompatActivity {

    private EditText nicInput, passwordInput;
    private Button loginButton;
    private TextView signupRedirectText;
    private DatabaseHelper2 dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        // Initialize UI components
        nicInput = findViewById(R.id.nicInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        signupRedirectText = findViewById(R.id.signupRedirectText);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper2(this);

        // Login button functionality
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nic = nicInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                // Validate inputs
                if (nic.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity2.this, "NIC and Password are required!", Toast.LENGTH_SHORT).show();
                } else {
                    // Authenticate user
                    if (dbHelper.authenticateUser(nic, password)) {
                        Toast.makeText(LoginActivity2.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        // Redirect to the main page or dashboard
                        Intent intent = new Intent(LoginActivity2.this, AdminDashboardActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity2.this, "Invalid NIC or Password!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Redirect to signup page
        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity2.this, SignupActivity2.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
