package com.example.runconnect.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runconnect.function.Functions;
import com.example.runconnect.info.UniqueInfos;

import static com.example.runconnect.R.id.*;
import static com.example.runconnect.R.layout.*;

import java.util.ArrayList;
import java.util.List;

public class MessageExchangeTokenRecyclerAdapter extends RecyclerView.Adapter<MessageExchangeTokenViewHolder> {
    List<Message> messageList;
    AppCompatActivity app;
    //    private boolean isYou;
    private List<Boolean> isYouList = new ArrayList<>();


    private int count = 0;

    public MessageExchangeTokenRecyclerAdapter(List<Message> messageList, AppCompatActivity app) {
        this.messageList = messageList;
        this.app = app;
    }

    public MessageExchangeTokenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        boolean isYou = messageList.get(count).getSendUser().getId().equals(UniqueInfos.getUser().getId());
        isYouList.add(isYou);
        View view;
        if (isYou) {
            view = LayoutInflater.from(parent.getContext()).inflate(you_message_exchange_token_contents_layout, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(another_message_exchange_token_contents_layout, parent, false);
        }
        MessageExchangeTokenViewHolder holder = new MessageExchangeTokenViewHolder(view);

        // メッセージが押された時の処理
        holder.itemView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
        });

        count++;
        return holder;
    }

    // 一行分のレイアウトの詳細設定
    @Override
    public void onBindViewHolder(@NonNull MessageExchangeTokenViewHolder holder, int position) {
        Message message = messageList.get(position);
        ImageButton messageIconImageButton = holder.getMessageIconImageButton();
        UniqueInfos.db.downloadImage(message.getSendUser().getImageId(), Functions.setImageUri(app, messageIconImageButton));
        TextView messageTextText = holder.getMessageTextText();
        TextView messageTimeText = holder.getMessageTimeText();
        char[] messageDateTokens = String.valueOf(message.getDate()).toCharArray();
        String hour = "" + messageDateTokens[8] + messageDateTokens[9];
        hour = hour.charAt(0) == '0' ? String.valueOf(hour.charAt(1)) : hour;
        String minute = "" + messageDateTokens[10] + messageDateTokens[11];
        messageTimeText.setText(hour + ":" + minute);
        messageTextText.setText(message.getText() + "\n\n");
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
