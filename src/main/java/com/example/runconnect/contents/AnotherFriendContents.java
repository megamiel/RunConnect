package com.example.runconnect.contents;

import static com.example.runconnect.R.id.anotherFriendList;
import static com.example.runconnect.R.layout.another_friend_layout;
import static com.example.runconnect.R.layout.another_friend_token_layout;
import static com.example.runconnect.R.id.*;

import android.graphics.Color;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.runconnect.friend.AnotherFriendTokenRecyclerAdapter;
import com.example.runconnect.friend.FriendTokenRecyclerAdapter;
import com.example.runconnect.info.UniqueInfos;
import com.example.runconnect.user.User;

public class AnotherFriendContents {
    public static void start(AppCompatActivity app, User user) {
        if (user.getFriendIdList().isEmpty()) {
            Toast.makeText(app, "フレンドがいません", Toast.LENGTH_SHORT).show();
        } else {
            UniqueInfos.displayController.changeMainContents(another_friend_layout);
            UniqueInfos.displayController.setRecyclerView(anotherFriendList, new AnotherFriendTokenRecyclerAdapter(user.getFriendIdList(), another_friend_token_layout));
        }
    }
}
