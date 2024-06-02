package com.example.runconnect.message;

import static com.example.runconnect.R.layout.another_message_exchange_token_contents_layout;
import static com.example.runconnect.R.layout.message_exchange_token_layout;
import static com.example.runconnect.R.layout.you_message_exchange_token_contents_layout;

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
import com.example.runconnect.function.Functions;
import com.example.runconnect.info.UniqueInfos;

import java.util.List;

public class MessageListTokenViewHolder extends RecyclerView.ViewHolder {
    private final ImageView friendIconImageButton;
    private final TextView userNameText;
    private final TextView userMessageTextText;
    private final TextView userMessageTimeText;

    public MessageListTokenViewHolder(@NonNull View view) {
        super(view);
        friendIconImageButton = view.findViewById(R.id.friendIconImageButton);
        userNameText = view.findViewById(R.id.userNameLabel);
        userMessageTextText = view.findViewById(R.id.userMessageTextLabel);
        userMessageTimeText = view.findViewById(R.id.userMassageTimeLabel);
    }

    public ImageView getFriendIconImageButton() {
        return friendIconImageButton;
    }

    public TextView getUserNameText() {
        return userNameText;
    }

    public TextView getUserMessageTextText() {
        return userMessageTextText;
    }

    public TextView getUserMessageTimeText() {
        return userMessageTimeText;
    }
}