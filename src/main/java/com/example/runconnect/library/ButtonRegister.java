package com.example.runconnect.library;

import androidx.appcompat.app.AppCompatActivity;

import com.example.runconnect.function.NullaryFunction;

public class ButtonRegister {
    public static <T extends android.view.View> T regist(AppCompatActivity app, int buttonId, NullaryFunction function) {
        T t = app.findViewById(buttonId);
        t.setOnClickListener(v -> function.exe());
        return t;
    }
}
