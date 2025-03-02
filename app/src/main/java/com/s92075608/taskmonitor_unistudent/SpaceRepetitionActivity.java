package com.s92075608.taskmonitor_unistudent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

public class SpaceRepetitionActivity extends AppCompatActivity {

    private TextView timerDisplay;
    private Button startButton, resetButton;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long timeLeftInMillis;
    private long breakTimeInMillis = 3 * 60 * 1000; // 3 minutes break session
    private long timeLeftOnPause = 0;

    // Notification Channel ID
    private static final String CHANNEL_ID = "space_repetition_timer_channel";
    private int studySessionCount = 0; // Counter for study sessions
    private static final int MAX_SESSIONS = 6; // Maximum number of study sessions

    // Time intervals for study sessions
    private long[] studyIntervals = {5, 10, 15, 20, 30, 40}; // study time in minutes
    private long currentStudyTimeInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_repetition); // Use the same layout for simplicity

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
        if (studySessionCount < MAX_SESSIONS) {
            currentStudyTimeInMillis = studyIntervals[studySessionCount] * 60 * 1000; // Convert minutes to milliseconds
            countDownTimer = new CountDownTimer(currentStudyTimeInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    updateTimer();
                }

                @Override
                public void onFinish() {
                    // Send notification and start break or next session
                    showNotification();
                    studySessionCount++;
                    if (studySessionCount < MAX_SESSIONS) {
                        startBreak();
                    } else {
                        // End of space repetition cycle
                        timerDisplay.setText("Study Sessions Complete!");
                        startButton.setText("Start");
                        isTimerRunning = false;
                    }
                }
            }.start();

            startButton.setText("Stop");
            isTimerRunning = true;
        }
    }

    private void startBreak() {
        // Start break time (3 minutes) before next study session
        timeLeftInMillis = breakTimeInMillis;
        updateTimer();
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                // Break is over, now start the next study session
                startTimer();
            }
        }.start();
    }

    private void stopTimer() {
        countDownTimer.cancel();
        timeLeftOnPause = timeLeftInMillis;
        startButton.setText("Start");
        isTimerRunning = false;
    }

    private void resetTimer() {
        studySessionCount = 0;
        timeLeftInMillis = 5 * 60 * 1000; // Start with a 5-minute study session
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
                .setContentTitle("Space Repetition Timer")
                .setContentText("Session complete! Time for a break.")
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
            CharSequence name = "Space Repetition Timer Channel";
            String description = "Channel for Space Repetition Timer notifications";
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