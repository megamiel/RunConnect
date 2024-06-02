package com.example.runconnect.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.runconnect.R;
import com.example.runconnect.library.ActivityRunner;

public class RunningStartConfirmSubActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_start_confirm_layout);
        findViewById(R.id.runningStartYesButton).setOnClickListener(v -> {
            ActivityRunner.switchOver(this,RunningSubActivity.class);
        });

        findViewById(R.id.runningStartNoButton).setOnClickListener(v -> finish());
    }
}
