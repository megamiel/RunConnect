package com.example.runconnect.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.runconnect.R;
import com.example.runconnect.info.UniqueInfos;

public class RunningFinishConfirmSubActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_finish_confirm_layout);
        findViewById(R.id.runningFinishYesButton).setOnClickListener(v -> {
            UniqueInfos.isRunning.set(false);
            Intent intent = new Intent(RunningFinishConfirmSubActivity.this, ResultSubActivity.class);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.runningFinishNoButton).setOnClickListener(v -> finish());
    }
}
