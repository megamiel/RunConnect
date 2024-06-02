package com.example.runconnect.load;

import static com.example.runconnect.R.id.loadingLabel;
import static com.example.runconnect.R.layout.loding_layout;

import android.annotation.SuppressLint;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.runconnect.info.UniqueInfos;
import com.example.runconnect.library.DisplayController;
import com.example.runconnect.library.Pointer;
import com.example.runconnect.library.ThreadRunner;
import com.example.runconnect.function.Functions;

public class Load {
    public static void loadingView(AppCompatActivity app, Pointer<Boolean> isLoading) {
        UniqueInfos.displayController.setContents(loding_layout);
        ThreadRunner.start(() -> loadInitAndLoopWhileLoading(app, isLoading));
    }

    private static void loadInitAndLoopWhileLoading(AppCompatActivity app, Pointer<Boolean> isLoading) {
        TextView loadingText = app.findViewById(loadingLabel);
        StringBuilder tailDot = new StringBuilder();
        while (isLoading.get()) {
            editLoadingText(loadingText, tailDot);
            Functions.sleep(700);
        }
    }

    private static void editLoadingText(TextView loadingText, StringBuilder tailDot) {
        loadingText.setText("LOADING" + tailDot);
        if (tailDot.length() == 3) {
            tailDot.setLength(0);
        } else {
            tailDot.append(".");
        }
    }
}
