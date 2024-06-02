package com.example.runconnect.post;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.example.runconnect.activity.PostCreateSubActivity;
import com.example.runconnect.activity.PostResponseSubActivity;
import com.example.runconnect.activity.StackActivitysAdministrator;
import com.example.runconnect.activity.main.RunConnectMainActivity;
import com.example.runconnect.library.Consts;
import com.example.runconnect.library.ThreadRunner;
import com.example.runconnect.user.User;
import com.example.runconnect.contents.AnotherUserInfoContents;
import com.example.runconnect.function.Functions;
import com.example.runconnect.info.SharedInfos;
import com.example.runconnect.info.UniqueInfos;

public class PostTokenRecyclerAdapter extends RecyclerView.Adapter<PostTokenViewHolder> {
    List<Post> postList;

    // 複製したいレイアウトのxmlのid
    private int id;
    private AppCompatActivity app;

    public PostTokenRecyclerAdapter(List<Post> postList, int id) {
        this.postList = postList;
        this.id = id;
        app = RunConnectMainActivity.getInstance();
    }

    public PostTokenRecyclerAdapter(List<Post> postList, int id, AppCompatActivity app) {
        this.postList = postList;
        this.id = id;
        this.app = app;
    }


    public PostTokenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(id, parent, false);
        PostTokenViewHolder holder = new PostTokenViewHolder(view);

        // 投稿が押された時にその投稿をメインに表示し、投稿によるいいね数などの詳細も確認可能になるようにする
        holder.itemView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            // インテントで返信欄の表示するのいかがでしょうか
            Post post = postList.get(position);
            PostResponseSubActivity.post = post;
            StackActivitysAdministrator.parentActivityList.add(app);
            Intent intent = new Intent(app, PostResponseSubActivity.class);
            app.startActivity(intent);
        });
        return holder;
    }

    // 一行分のレイアウトの詳細設定
    @Override
    public void onBindViewHolder(@NonNull PostTokenViewHolder holder, int position) {
        Post postToken = SharedInfos.postMap.get(postList.get(position).getPostId());
        User user = postToken.getUser();
        String name = user.getName();
        String post = postToken.getPostText();

        ImageView iconImageButton = holder.getIconImageButton();
        TextView nameText = holder.getNameText();
        TextView postText = holder.getPostText();
        ImageButton responseButton = holder.getResponseButton();
        ImageButton goodButton = holder.getGoodButton();
        TextView responseNumText = holder.getResponseNumText();
        TextView goodNumText = holder.getGoodNumText();
        ImageView postImage = holder.getPostImage();
        TextView dateText = holder.getDateText();

        UniqueInfos.db.downloadImage(user.getImageId(), Functions.setImageUri(RunConnectMainActivity.getInstance(), iconImageButton));

        if (postToken.getPostImageId().isEmpty()) {
            postImage.setImageDrawable(null);
        } else {
            UniqueInfos.db.downloadImage(postToken.getPostImageId(), Functions.setImageUri(RunConnectMainActivity.getInstance(), postImage));
        }

        iconImageButton.setOnClickListener(v -> {
            StackActivitysAdministrator.finishAllSubActivity();
            if (user == UniqueInfos.getUser()) {
                RunConnectMainActivity.getInstance().userInfoButtonEventHandling();
            } else {
                AnotherUserInfoContents.start(RunConnectMainActivity.getInstance(), user);
            }
        });


        responseButton.setOnClickListener(v -> {
            PostCreateSubActivity.id = null;
            ThreadRunner.start(() -> {
                Intent intent = new Intent(RunConnectMainActivity.getInstance(), PostCreateSubActivity.class);
                RunConnectMainActivity.getInstance().startActivity(intent);
            });
            ThreadRunner.start(() -> {
                while (PostCreateSubActivity.id == null) {
                    Functions.sleep(500);
                }
                postToken.addResponseList(PostCreateSubActivity.id);
            });
        });

        // ユーザがいいねしたことある投稿なら、いいねを解除、ないなら、いいねを登録
        goodButton.setOnClickListener(v -> {
            UniqueInfos.getUser().addGoodPostList(postToken.getPostId());
            goodNumText.setText(String.valueOf(postToken.getGoodCount()));
        });

        // 未実装
//        shareButton.setOnClickListener(v -> {
//
//        });


        nameText.setText(name);
        postText.setText(post);
        responseNumText.setText(" " + postToken.getResponseIdList().size());
        goodNumText.setText(" " + postToken.getGoodCount());
//        shareNumText.setText(String.valueOf(postToken.getShareCount()));


        Map<String, Integer> postDateMap = Functions.toMap(postToken.getDate());
        Map<String, Integer> currentDateMap = Functions.toMap(Functions.getCurrentTime());
        long elapsedTime = Functions.secondDiff(postDateMap, currentDateMap);
        int index = IntStream.range(0, Consts.SECOND_ARRAY.length).filter(i -> elapsedTime >= Consts.SECOND_ARRAY[i]).findFirst().orElse(-1);
        String elapsedStr = index == -1 ? "不明" : elapsedTime / Consts.SECOND_ARRAY[index] + Consts.TIME_UNIT_ARRAY[index] + "前";


        dateText.setText(elapsedStr);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static PostTokenRecyclerAdapter newInstance(List<Post> postList, int id) {
        return new PostTokenRecyclerAdapter(postList, id);
    }
}
