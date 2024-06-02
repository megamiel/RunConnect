package com.example.runconnect.contents;

import static com.example.runconnect.R.id.idSearchButton;
import static com.example.runconnect.R.id.idSearchEdit;
import static com.example.runconnect.R.id.idSearchList;
import static com.example.runconnect.R.layout.friend_token_layout;
import static com.example.runconnect.R.layout.id_search_layout;

import android.graphics.Color;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.runconnect.friend.FriendTokenRecyclerAdapter;
import com.example.runconnect.info.SharedInfos;
import com.example.runconnect.info.UniqueInfos;
import com.example.runconnect.library.ButtonRegister;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FriendSearchContents {
    public static void start(AppCompatActivity app) {
        UniqueInfos.displayController.changeMainContents(id_search_layout);
        registerIdSearchButton(app);
    }

    // ID検索ボタンを登録
    private static void registerIdSearchButton(AppCompatActivity app) {
        ButtonRegister.regist(app,idSearchButton, () -> idSearchButtonEventHandling(app));
    }

    // id検索ボタンを押された時の処理
    private static void idSearchButtonEventHandling(AppCompatActivity app) {
        EditText idSearchEditText = app.findViewById(idSearchEdit);
        String searchId = idSearchEditText.getText().toString();
        idSearchEditText.setHintTextColor(searchId.isEmpty() ? Color.RED : Color.GRAY);
        if (searchId.isEmpty()) {
            return;
        }
        // 検索idが含まれているidを全部表示
        List<String> searchIdList = SharedInfos.userMap.keySet().stream().filter(key -> key.contains(searchId) && !key.equals(UniqueInfos.getUser().getId())).sorted(Comparator.comparingInt(String::length)).collect(Collectors.toList());
        UniqueInfos.displayController.setRecyclerView(idSearchList, new FriendTokenRecyclerAdapter(searchIdList, friend_token_layout));
    }
}
