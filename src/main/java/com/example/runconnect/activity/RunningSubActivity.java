package com.example.runconnect.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.runconnect.R.id.*;
import static com.example.runconnect.R.layout.*;

import com.example.runconnect.library.LocationAdministrator;
import com.example.runconnect.library.Pointer;
import com.example.runconnect.library.ThreadRunner;
import com.example.runconnect.info.UniqueInfos;
import com.example.runconnect.function.Functions;
import com.example.runconnect.function.UnaryFunction;

public class RunningSubActivity extends AppCompatActivity {
    private Location lastLocation = null;
    private LocationAdministrator locationAdministrator;
    private static Pointer<Double> movingDistance = new Pointer<>(0.0);
    private static int time = 0;
    private int minusTime = 0;

    private UnaryFunction<Location> measureFunction = location -> {
        double tokenMovingDistance = Functions.getDistance(lastLocation, location);

        // 移動距離が早すぎる、遅すぎる、歩数が極端に少ない場合、12秒間をカウントしなかったことにする
        // ズルしていなければtrue,していればfalse
        if (tokenMovingDistance >= 5 && tokenMovingDistance <= 120) {
            movingDistance.set(movingDistance.get() + tokenMovingDistance);
        } else {
            minusTime += 12;
        }

        if (UniqueInfos.isRunning.get()) {
            updateRunningDataThreadStart(location);
        } else {
            double movingDistance = RunningSubActivity.movingDistance.get();
            time -= minusTime;
            double averageMovingDistance = movingDistance / time;
            int lastTime = time % 12;
            movingDistance += averageMovingDistance * lastTime;
            averageMovingDistance = movingDistance / time;
            System.out.println("移動距離:" + movingDistance + "m");
            System.out.println("平均移動距離(秒):" + averageMovingDistance + "m");
            System.out.println("運動時間:" + time + "s");
            finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(running_layout);
        movingDistance.set(0.0);
        time = 0;

        findViewById(runFinishButton).setOnClickListener(v -> {
            Intent intent = new Intent(RunningSubActivity.this, RunningFinishConfirmSubActivity.class);
            startActivity(intent);
        });
        runningStart();
    }

    public void runningStart() {
        movingDistance.set(0.0);
        UniqueInfos.isRunning.set(true);
        locationAdministrator = new LocationAdministrator(this);
        ThreadRunner.start(this::startTimer);
        measureStart();

        // なぜかリザルト画面からこっちに移ってしまうのを防ぐための一応の策を記述した
        ThreadRunner.start(() -> {
            while (UniqueInfos.isRunning.get()) {
                Functions.sleep(500);
            }
            finish();
        });
    }

    private void measureStart() {
        locationAdministrator.requestGetLocation(measureFunction);
    }

    private void startTimer() {
        while (UniqueInfos.isRunning.get()) {
            Functions.sleep(1000);
            time++;
            System.out.println(time);
        }
    }


    private void updateRunningDataThreadStart(Location location) {
        ThreadRunner.start(() -> updateRunningData(location));
    }

    private void updateRunningData(Location location) {
        lastLocation = location;
        int sleepTime = 0;
        while (UniqueInfos.isRunning.get() && sleepTime < 12000) {
            Functions.sleep(500);
            sleepTime += 500;
        }
        measureStart();
    }

    // 戻るボタンを無効にする
    @Override
    public void onBackPressed() {
    }

    public static double getMovingDistance() {
        return movingDistance.get();
    }

    public static int getElapsedTime() {
        return time;
    }

}
