package com.example.runconnect.friend;

import static com.example.runconnect.R.id.*;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runconnect.activity.main.RunConnectMainActivity;
import com.example.runconnect.contents.AnotherUserInfoContents;
import com.example.runconnect.function.Functions;
import com.example.runconnect.info.UniqueInfos;
import com.example.runconnect.user.User;
import com.example.runconnect.info.SharedInfos;

import java.util.List;

public class FriendTokenRecyclerAdapter extends RecyclerView.Adapter<FriendTokenViewHolder> {
    List<String> friendIdList;

    // 複製したいレイアウトのxmlのid
    int id;

    public FriendTokenRecyclerAdapter(List<String> friendIdList, int id) {
        this.friendIdList = friendIdList;
        this.id = id;
    }

    public FriendTokenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(id, parent, false);
        FriendTokenViewHolder holder = new FriendTokenViewHolder(view, friendNameLabel, friendRankLabel,friendIconImageButton);

        holder.itemView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            String id = friendIdList.get(position);
            User friend = SharedInfos.userMap.get(id);
            AnotherUserInfoContents.start(RunConnectMainActivity.getInstance(),friend);
        });
        return holder;
    }

    // 一行分のレイアウトの詳細設定
    @Override
    public void onBindViewHolder(@NonNull FriendTokenViewHolder holder, int position) {
        // マップにid渡したら手に入るやつ
        String id = friendIdList.get(position);
        User friend = SharedInfos.userMap.get(id);
        String friendName = friend.getName();
        String friendRank = String.valueOf(friend.getRank());
        TextView friendNameText = holder.getFriendNameText();
        TextView friendRankText = holder.getFriendRankText();
        ImageView friendIconImage=holder.getIconImage();
        UniqueInfos.db.downloadImage(friend.getImageId(), Functions.setImageUri(RunConnectMainActivity.getInstance(),friendIconImage));

        friendNameText.setText(friendName);
        friendRankText.setText(friendRank);
    }


    @Override
    public int getItemCount() {
        return friendIdList.size();
    }
}
