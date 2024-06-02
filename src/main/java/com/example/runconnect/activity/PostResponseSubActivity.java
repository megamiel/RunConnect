package com.example.runconnect.activity;

import static com.example.runconnect.R.id.responseSubContents;
import static com.example.runconnect.R.layout.post_token_layout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.runconnect.R;
import com.example.runconnect.activity.main.RunConnectMainActivity;
import com.example.runconnect.contents.AnotherUserInfoContents;
import com.example.runconnect.function.Functions;
import com.example.runconnect.info.SharedInfos;
import com.example.runconnect.info.UniqueInfos;
import com.example.runconnect.library.ThreadRunner;
import com.example.runconnect.post.Post;
import com.example.runconnect.post.PostTokenRecyclerAdapter;
import com.example.runconnect.user.User;

import java.util.stream.Collectors;

import static com.example.runconnect.R.id.*;

public class PostResponseSubActivity extends AppCompatActivity {
    public static Post post = null;

    @Override
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_response_layout);
        StackActivitysAdministrator.thisActivity = this;

        UniqueInfos.displayController.changeMainContents(post_token_layout, this, responseSubContents);
        UniqueInfos.displayController.setRecyclerView(responseList, new PostTokenRecyclerAdapter(post.getResponseIdList().stream().map(SharedInfos.postMap::get).collect(Collectors.toList()), post_token_layout, PostResponseSubActivity.this), this);

        Button responseBack = findViewById(responseBackButton);
        Button responseHome = findViewById(responseHomeButton);

        responseBack.setOnClickListener(v -> finish());

        // ホームボタンが押されると、スタックされたサブアクティビティを全て削除する
        responseHome.setOnClickListener(v -> {
            AppCompatActivity parent = StackActivitysAdministrator.parentActivityList.get(StackActivitysAdministrator.parentActivityList.size() - 1);
            while (parent instanceof PostResponseSubActivity) {
                parent.finish();
                parent = StackActivitysAdministrator.parentActivityList.remove(StackActivitysAdministrator.parentActivityList.size() - 1);
            }
            finish();
        });


        User user = SharedInfos.userMap.get(post.getUser().getId());
        String name = user.getName();
        String post = PostResponseSubActivity.post.getPostText();

        ImageView iconImageButton = findViewById(userIconButton);
        TextView nameText = findViewById(nameLabel);
        TextView postText = findViewById(postLabel);
        ImageButton responseImageButton = findViewById(responseButton);
        ImageButton goodImageButton = findViewById(goodButton);
//        ImageButton shareImageButton = findViewById(shareButton);
        TextView responseNumText = findViewById(responseNumLabel);
        TextView goodNumText = findViewById(goodNumLabel);
//        TextView shareNumText = findViewById(shareNumLabel);
        ImageView postImageView = findViewById(postImage);

        // iconImageButtonにuserのiconImageIdをもとにファイアベースから持ってきて貼り付ける

        UniqueInfos.db.downloadImage(user.getImageId(), Functions.setImageUri(this, iconImageButton));
        // どうにかしてこっちに移行したいけど、写真がおかしくなるため、どうしようかって感じ
//        Functions.setImageUri(RunConnectMainActivity.getInstance(),iconImageButton).exe(postToken.getPostImageUri());

        if (PostResponseSubActivity.post.getPostImageId().isEmpty()) {
            postImageView.setImageDrawable(null);
        } else {
            UniqueInfos.db.downloadImage(PostResponseSubActivity.post.getPostImageId(), Functions.setImageUri(this, postImageView));
        }


        iconImageButton.setOnClickListener(v -> {
            StackActivitysAdministrator.finishAllSubActivity();
            if (user == UniqueInfos.getUser()) {
                RunConnectMainActivity.getInstance().userInfoButtonEventHandling();
            } else {
                AnotherUserInfoContents.start(RunConnectMainActivity.getInstance(), user);
            }
        });


        responseImageButton.setOnClickListener(v -> {
            PostCreateSubActivity.id = null;
            ThreadRunner.start(() -> {
                Intent intent = new Intent(this, PostCreateSubActivity.class);
                startActivity(intent);
            });
            ThreadRunner.start(() -> {
                while (PostCreateSubActivity.id == null) {
                    Functions.sleep(500);
                }
                PostResponseSubActivity.post.addResponseList(PostCreateSubActivity.id);
            });
        });

        // ユーザがいいねしたことある投稿なら、いいねを解除、ないなら、いいねを登録
        goodImageButton.setOnClickListener(v -> {
            UniqueInfos.getUser().addGoodPostList(PostResponseSubActivity.post.getPostId());
            goodNumText.setText(String.valueOf(PostResponseSubActivity.post.getGoodCount()));
        });

        // 未実装
//        shareImageButton.setOnClickListener(v -> {
//
//        });


        nameText.setText(name);
        postText.setText(post);
        responseNumText.setText(String.valueOf(PostResponseSubActivity.post.getResponseIdList().size()));
        goodNumText.setText(String.valueOf(PostResponseSubActivity.post.getGoodCount()));
//        shareNumText.setText(String.valueOf(postToken.getShareCount()));

    }


}
