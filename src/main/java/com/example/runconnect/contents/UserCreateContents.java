package com.example.runconnect.contents;

import static com.example.runconnect.R.id.idInputEdit;
import static com.example.runconnect.R.id.loginButton;
import static com.example.runconnect.R.id.nameInputEdit;
import static com.example.runconnect.R.layout.user_create_layout;

import android.graphics.Color;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.runconnect.library.ButtonRegister;
import com.example.runconnect.user.User;
import com.example.runconnect.function.NullaryFunction;
import com.example.runconnect.info.SharedInfos;
import com.example.runconnect.info.UniqueInfos;
import com.example.runconnect.library.FirebaseDatabase;

public class UserCreateContents {
    public static void start(AppCompatActivity app, NullaryFunction loginButtonClickFunction) {
        UniqueInfos.displayController.setContents(user_create_layout);
        registerLoginButton(app, loginButtonClickFunction);
    }

    private static void registerLoginButton(AppCompatActivity app, NullaryFunction function) {
        ButtonRegister.regist(app, loginButton, () -> loginButtonEventHandling(app, function));
    }

    // ログインボタンを押された時の処理
    private static void loginButtonEventHandling(AppCompatActivity app, NullaryFunction function) {
        String id = ((EditText) app.findViewById(idInputEdit)).getText().toString();
        String name = ((EditText) app.findViewById(nameInputEdit)).getText().toString();
        boolean isUserCreateSuccess = isUserCreateSuccess(app);
        if (isUserCreateSuccess) {
            FirebaseDatabase db = new FirebaseDatabase();
            User user = User.getNewInstance(id, name);
            user.firstSave(db, function);
            SharedInfos.userMap.put(user.getId(), user);
            UniqueInfos.id = id;
        }
    }

    private static boolean isUserCreateSuccess(AppCompatActivity app) {
        boolean isUserCreateSuccess = true;
        EditText idInputEditText = app.findViewById(idInputEdit);
        EditText nameInputEditText = app.findViewById(nameInputEdit);
        String id = idInputEditText.getText().toString();
        String name = nameInputEditText.getText().toString();
        if (SharedInfos.userMap.containsKey(id)) {
            idInputEditText.setText("");
            idInputEditText.setHint("既に存在するIDです");
            isUserCreateSuccess = false;
        }
        if (id.length() > 30) {
            idInputEditText.setText("");
            idInputEditText.setHint(" 30文字以下にしてください");
        } else {
            idInputEditText.setHint(" 1文字以上入力してください");
        }
        if (id.isEmpty() || id.length() > 30) {
            isUserCreateSuccess = false;
        }
        idInputEditText.setHintTextColor(isUserCreateSuccess ? Color.GRAY : Color.RED);
        if (name.isEmpty()) {
            nameInputEditText.setHintTextColor(Color.RED);
            isUserCreateSuccess = false;
        } else {
            nameInputEditText.setHintTextColor(Color.GRAY);
        }
        return isUserCreateSuccess;
    }
}
