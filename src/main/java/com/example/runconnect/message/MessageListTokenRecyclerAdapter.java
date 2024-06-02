package com.example.runconnect.message;

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

import com.example.runconnect.R;
import com.example.runconnect.activity.MessageExchangeSubActivity;
import com.example.runconnect.function.Functions;
import com.example.runconnect.info.SharedInfos;
import com.example.runconnect.info.UniqueInfos;
import com.example.runconnect.library.Consts;
import com.example.runconnect.user.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.runconnect.R.id.*;
import static com.example.runconnect.R.layout.*;

public class MessageListTokenRecyclerAdapter extends RecyclerView.Adapter<MessageListTokenViewHolder> {
    private List<String> userIdList;
    private AppCompatActivity app;

    public MessageListTokenRecyclerAdapter(List<String> userIdList, AppCompatActivity app) {
        this.userIdList = userIdList;
        this.app = app;
    }

    public MessageListTokenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_token_layout, parent, false);
        MessageListTokenViewHolder holder = new MessageListTokenViewHolder(view);

        // メッセージが押された時の処理
        holder.itemView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            MessageExchangeSubActivity.selectUserId = userIdList.get(position);
            Intent intent = new Intent(app, MessageExchangeSubActivity.class);
            app.startActivity(intent);
        });
        return holder;
    }

    // 一行分のレイアウトの詳細設定
    @Override
    public void onBindViewHolder(@NonNull MessageListTokenViewHolder holder, int position) {
        String userId = userIdList.get(position);
        User user = SharedInfos.userMap.get(userId);
        ImageView friendIconImage = holder.getFriendIconImageButton();
        UniqueInfos.db.downloadImage(user.getImageId(), Functions.setImageUri(app, friendIconImage));
        TextView userNameText = holder.getUserNameText();
        userNameText.setText(user.getName());
        TextView userMessageTextText = holder.getUserMessageTextText();
        List<Message> messageList = UniqueInfos.youMessageMap.get(userId);
        Message lastMessage = messageList.stream().sorted((m0,m1)->m0.getDate()>m1.getDate()?-1:1).collect(Collectors.toList()).get(0);
        userMessageTextText.setText(lastMessage.getText().length() > 15 ? lastMessage.getText().substring(0, 15) + "..." : lastMessage.getText());
        TextView userMessageTimeText = holder.getUserMessageTimeText();

        Map<String, Integer> postDateMap = Functions.toMap(lastMessage.getDate());
        Map<String, Integer> currentDateMap = Functions.toMap(Functions.getCurrentTime());
        long elapsedTime = Functions.secondDiff(postDateMap, currentDateMap);
        int index = IntStream.range(0, Consts.SECOND_ARRAY.length).filter(i -> elapsedTime >= Consts.SECOND_ARRAY[i]).findFirst().orElse(-1);
        String elapsedStr = index == -1 ? "不明" : elapsedTime / Consts.SECOND_ARRAY[index] + Consts.TIME_UNIT_ARRAY[index] + "前";

        userMessageTimeText.setText(elapsedStr);
    }

    @Override
    public int getItemCount() {
        return userIdList.size();
    }
}


