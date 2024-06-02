package com.example.runconnect.contents;

import static com.example.runconnect.R.id.friendList;
import static com.example.runconnect.R.id.messageButton;
import static com.example.runconnect.R.id.messageExchangeList;
import static com.example.runconnect.R.id.messageUserList;
import static com.example.runconnect.R.id.searchFriendButton;
import static com.example.runconnect.R.layout.friend_layout;
import static com.example.runconnect.R.layout.friend_token_layout;
import static com.example.runconnect.R.layout.message_exchange_layout;
import static com.example.runconnect.R.layout.message_list_layout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.runconnect.library.DisplayController;
import com.example.runconnect.message.MessageExchangeTokenRecyclerAdapter;
import com.example.runconnect.message.MessageListTokenRecyclerAdapter;
import com.example.runconnect.user.User;
import com.example.runconnect.friend.FriendTokenRecyclerAdapter;
import com.example.runconnect.info.UniqueInfos;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class FriendContents {
    public static void start(AppCompatActivity app) {
        UniqueInfos.displayController.changeMainContents(friend_layout);
        User user = UniqueInfos.getUser();
        UniqueInfos.displayController.setRecyclerView(friendList, new FriendTokenRecyclerAdapter(user.getFriendIdList(), friend_token_layout));
        app.findViewById(searchFriendButton).setOnClickListener(v -> FriendSearchContents.start(app));
        // 以下でメッセージコンテンツへ画面遷移する予定だから、次の僕頑張ってね
        app.findViewById(messageButton).setOnClickListener(v -> {
            UniqueInfos.displayController.changeMainContents(message_list_layout);
            UniqueInfos.displayController.setRecyclerView(messageUserList, new MessageListTokenRecyclerAdapter(new ArrayList<>(UniqueInfos.youMessageMap.keySet()), app));
        });
    }
}
