package com.example.runconnect.contents;

import android.content.Intent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.runconnect.R.id.*;
import static com.example.runconnect.R.layout.*;

import com.example.runconnect.activity.MessageExchangeSubActivity;
import com.example.runconnect.library.ActivityRunner;
import com.example.runconnect.post.Post;
import com.example.runconnect.post.PostTokenRecyclerAdapter;
import com.example.runconnect.R;
import com.example.runconnect.user.User;
import com.example.runconnect.function.Functions;
import com.example.runconnect.info.SharedInfos;
import com.example.runconnect.info.UniqueInfos;

import java.util.List;
import java.util.stream.Collectors;

public class AnotherUserInfoContents {
    public static void start(AppCompatActivity app, User anotherUser) {
        UniqueInfos.displayController.changeMainContents(R.layout.another_user_info_layout);
        // ボタンの登録やアイコン、名前などのプロフィールの初期化
        TextView userIdText = app.findViewById(anotherIdLabel);
        ImageView userProfileIcon = app.findViewById(anotherUserProfileIcon);
        TextView userRankNumText = app.findViewById(anotherRankLabel);
        TextView userFriendNumText = app.findViewById(anotherFriendNumLabel);
        Button userFriendListButton = app.findViewById(anotherFriendListButton);
        Button userFriendRequestButton = app.findViewById(friendRequestButton);
        TextView userNameText = app.findViewById(anotherNameLabel);
        TextView userDistText = app.findViewById(anotherDistLabel);
//        TextView userMutualFriendText = app.findViewById(mutualFriendLabel);
        ImageButton messageButton=app.findViewById(messageSendButton);

        // クリックされた他ユーザのアイコンや名前を設定
        userIdText.setText(anotherUser.getId());
//        Functions.setImageUri(app, userProfileIcon).exe(anotherUser.getImageUri());
        UniqueInfos.db.downloadImage(anotherUser.getImageId(),Functions.setImageUri(app,userProfileIcon));
        userRankNumText.setText(String.valueOf(anotherUser.getRank()));
        userFriendNumText.setText(String.valueOf(anotherUser.getFriendIdList().size()));

        userFriendListButton.setOnClickListener(v -> {
            // フレンド一覧を表示し、ユーザをクリックするとその人のプロフィールへ飛べる
            AnotherFriendContents.start(app,anotherUser);
        });

        if(UniqueInfos.getUser().getFriendIdList().contains(anotherUser.getId())){
            userFriendRequestButton.setText("フレンド");
        }

        userFriendRequestButton.setOnClickListener(v -> {
            // このユーザにフレンド申請を送信
            // フレンド申請の承認はメッセージの一番上に表示されるフレンド申請一覧からできるようにする
            System.out.println(anotherUser.getName() + "にフレンド申請を送信しました");
            anotherUser.addFriendRequest(UniqueInfos.getUser().getId());
        });

        messageButton.setOnClickListener(v->{
            MessageExchangeSubActivity.selectUserId = anotherUser.getId();
            Intent intent = new Intent(app, MessageExchangeSubActivity.class);
            app.startActivity(intent);
        });

        userNameText.setText(anotherUser.getName());
        userDistText.setText(String.valueOf(anotherUser.getTotalMovingDistance()));


        // 共通のフレンドの表示必要か？ //
        // 一応共通のフレンドの名前リストはできてる
//        List<String> mutualFriendList = UniqueInfos.getUser().getFriendIdList().stream().filter(id -> anotherUser.getFriendIdList().contains(id)).map(id -> SharedInfos.userMap.get(id).getName()).collect(Collectors.toList());
//        userMutualFriendText.setText("");

        List<Post> userPostList = SharedInfos.postMap.values().stream().filter(post -> post.getUser() == anotherUser).sorted((post0, post1) -> Long.compare(post1.getDate(), post0.getDate())).collect(Collectors.toList());
        UniqueInfos.displayController.setRecyclerView(anotherPostList, new PostTokenRecyclerAdapter(userPostList, post_token_layout));
    }
}
