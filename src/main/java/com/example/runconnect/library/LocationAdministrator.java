package com.example.runconnect.library;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.runconnect.function.UnaryFunction;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class LocationAdministrator {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private AppCompatActivity app;

    public LocationAdministrator(AppCompatActivity app) {
        this.app = app;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(app);
    }

    // 更新をリクエストし、更新が終わったらコールバックを実行
    public void requestGetLocation(UnaryFunction<Location> function) {
        checkPermission();
        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, new DefaultCancellationToken()).addOnSuccessListener(app, function::exe);
    }


    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(app, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(app, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // パーミッションの許可を取得する
            ActivityCompat.requestPermissions(app, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        }
    }
}
