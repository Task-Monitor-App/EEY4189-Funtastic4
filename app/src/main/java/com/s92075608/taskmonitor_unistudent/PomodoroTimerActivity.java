package com.s92075608.taskmonitor_unistudent;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class PomodoroTimerActivity extends AppCompatActivity {

    private TextView timerDisplay;
    private Button startButton, resetButton;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long timeLeftInMillis = 1 * 10 * 1000; // 25 minutes work session
    private long breakTimeInMillis = 1 * 5 * 1000; // 5 minutes break session
    private long timeLeftOnPause = 0;

    // Notification Channel ID
    private static final String CHANNEL_ID = "pomodoro_timer_channel";
    private int cycleCount = 0; // Counter for cycles
    private static final int MAX_CYCLES = 4; // 2 hours: 4 cycles of 25-min work + 5-min break

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro_timer);

        // Initialize UI elements
        timerDisplay = findViewById(R.id.timerDisplay);
        startButton = findViewById(R.id.startButton);
        resetButton = findViewById(R.id.resetButton);

        // Create the notification channel
        createNotificationChannel();

        startButton.setOnClickListener(view -> {
            if (isTimerRunning) {
                stopTimer();
            } else {
                startTimer();
            }
        });

        resetButton.setOnClickListener(view -> resetTimer());
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                // Send notification and start break or work session
                showNotification();
                if (cycleCount < MAX_CYCLES) {
                    cycleCount++;
                    if (cycleCount % 2 == 1) { // Odd cycle, it's work time
                        startBreak();
                    } else { // Even cycle, it's break time
                        startWork();
                    }
                } else {
                    // After 2 hours (4 cycles), stop the timer
                    timerDisplay.setText("02:00:00");
                    startButton.setText("Start");
                    isTimerRunning = false;
                }
            }
        }.start();

        startButton.setText("Stop");
        isTimerRunning = true;
    }

    private void startWork() {
        // Set work time to 25 minutes
        timeLeftInMillis = 25 * 60 * 1000;
        updateTimer();
    }

    private void startBreak() {
        // Set break time to 5 minutes
        timeLeftInMillis = 1 * 5 * 1000;
        updateTimer();
    }

    private void stopTimer() {
        countDownTimer.cancel();
        timeLeftOnPause = timeLeftInMillis;
        startButton.setText("Start");
        isTimerRunning = false;
    }

    private void resetTimer() {
        cycleCount = 0;
        timeLeftInMillis = 25 * 60 * 1000; // Reset to initial work session time
        if (isTimerRunning) {
            countDownTimer.cancel();
            startButton.setText("Start");
            isTimerRunning = false;
        }
        updateTimer();
    }

    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String time = String.format("%02d:%02d", minutes, seconds);
        timerDisplay.setText(time);
    }

    private void showNotification() {
        // Create a notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // You can change this to your app's icon
                .setContentTitle("Pomodoro Timer")
                .setContentText("Session complete! Time for your break.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true); // Dismiss notification on tap

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, notificationBuilder.build()); // Notification ID is 1
        }
    }

    private void createNotificationChannel() {
        // Create notification channel for Android 8.0 and higher
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "Pomodoro Timer Channel";
            String description = "Channel for Pomodoro Timer notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
