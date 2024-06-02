package com.example.runconnect.friend;

import static com.example.runconnect.R.id.anotherFriendRequestButton;
import static com.example.runconnect.R.id.friendIconImageButton;
import static com.example.runconnect.R.id.friendNameLabel;
import static com.example.runconnect.R.id.friendRankLabel;

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
import com.example.runconnect.info.SharedInfos;
import com.example.runconnect.info.UniqueInfos;
import com.example.runconnect.user.User;

import java.util.List;

// この二つを作って、FriendTokenRecyclerAdapterではなく、こっちを使い、こっちにボタンの登録もするように！
public class AnotherFriendTokenRecyclerAdapter extends RecyclerView.Adapter<AnotherFriendTokenViewHolder> {
    private List<String> friendIdList;
    private int id;

    public AnotherFriendTokenRecyclerAdapter(List<String> friendIdList, int id) {
        this.friendIdList = friendIdList;
        this.id = id;
    }

    @NonNull
    @Override
    public AnotherFriendTokenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(id, parent, false);
        AnotherFriendTokenViewHolder holder = new AnotherFriendTokenViewHolder(view, friendIconImageButton, friendNameLabel, friendRankLabel, anotherFriendRequestButton);

        holder.itemView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            String id = friendIdList.get(position);
            User friend = SharedInfos.userMap.get(id);
            AnotherUserInfoContents.start(RunConnectMainActivity.getInstance(), friend);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AnotherFriendTokenViewHolder holder, int position) {
        User user = SharedInfos.userMap.get(friendIdList.get(position));
        String friendName = user.getName();
        String friendRank = String.valueOf(user.getRank());
        ImageView friendIconImage = holder.getAnotherIconImage();
        TextView friendNameText = holder.getAnotherNameText();
        TextView friendRankText = holder.getAnotherRankText();
        Button friendRequestButton = holder.getAnotherFriendRequestButton();
        UniqueInfos.db.downloadImage(user.getImageId(), Functions.setImageUri(RunConnectMainActivity.getInstance(), friendIconImage));
        friendRequestButton.setOnClickListener(v->{
            user.addFriendRequest(UniqueInfos.getUser().getId());
        });
        if(UniqueInfos.getUser().getFriendIdList().contains(user.getId())){
            friendRequestButton.setText("フレンド");
        }
        friendNameText.setText(friendName);
        friendRankText.setText(friendRank);

    }

    @Override
    public int getItemCount() {
        return friendIdList.size();
    }
}