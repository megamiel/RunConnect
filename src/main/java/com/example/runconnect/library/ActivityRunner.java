package com.example.runconnect.library;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

// 画面遷移(Intent)
public class ActivityRunner {
    // 新しくアクティビティをスタックする
    public static void start(AppCompatActivity app, Class<?> clazz) {
        app.startActivity(new Intent(app, clazz));
    }

    // 現在のアクティビティを終了し、新しいアクティビティをスタックする
    public static void switchOver(AppCompatActivity app, Class<?> clazz){
        start(app,clazz);
        app.finish();
    }
}
