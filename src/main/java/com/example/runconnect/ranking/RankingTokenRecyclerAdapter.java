package com.example.runconnect.ranking;

import static com.example.runconnect.R.id.*;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runconnect.activity.main.RunConnectMainActivity;
import com.example.runconnect.contents.AnotherUserInfoContents;
import com.example.runconnect.contents.UserInfoContents;
import com.example.runconnect.function.Functions;
import com.example.runconnect.info.SharedInfos;
import com.example.runconnect.info.UniqueInfos;
import com.example.runconnect.user.User;

import java.util.List;

public class RankingTokenRecyclerAdapter extends RecyclerView.Adapter<RankingTokenViewHolder> {
    private List<User> rankingTokenList;

    // 複製したいレイアウトのxmlのid
    private int id;

    public RankingTokenRecyclerAdapter(List<User> rankingTokenList, int id) {
        this.rankingTokenList = rankingTokenList;
        this.id = id;
    }

    public RankingTokenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(id, parent, false);
        RankingTokenViewHolder holder = new RankingTokenViewHolder(view, rankNumLabel, rankNameLabel,rankingIconImage,rankPointLabel);

        // 投稿が押された時にその投稿をメインに表示し、投稿によるいいね数などの詳細も確認可能になるようにする
        holder.itemView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            User user = rankingTokenList.get(position);
            if (user == UniqueInfos.getUser()) {
                UserInfoContents.start(RunConnectMainActivity.getInstance());
            } else {
                AnotherUserInfoContents.start(RunConnectMainActivity.getInstance(),user);
            }
        });
        return holder;
    }

    // 一行分のレイアウトの詳細設定
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RankingTokenViewHolder holder, int position) {
        User rankingUser = rankingTokenList.get(position);
        TextView rankingNumText = holder.getRankingNumText();
        TextView rankingNameText = holder.getRankingNameText();
        ImageView rankingIconImage=holder.getIconImage();
        TextView rankPointText=holder.getRankPointText();
        UniqueInfos.db.downloadImage(rankingUser.getImageId(), Functions.setImageUri(RunConnectMainActivity.getInstance(),rankingIconImage));
        rankingNumText.setText(String.valueOf(position + 1));
        rankingNameText.setText(rankingUser.getName());
        rankPointText.setText(rankingUser.toPoint()+"  pt");
    }

    @Override
    public int getItemCount() {
        return rankingTokenList.size();
    }
}
