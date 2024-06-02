package com.example.runconnect.activity;

import static com.example.runconnect.R.id.messageExchangeList;
import static com.example.runconnect.R.id.messageSendButton;

import android.graphics.Shader;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.runconnect.R;

import static com.example.runconnect.R.id.*;

import com.example.runconnect.info.SharedInfos;
import com.example.runconnect.info.UniqueInfos;
import com.example.runconnect.message.Message;
import com.example.runconnect.message.MessageExchangeTokenRecyclerAdapter;
import com.example.runconnect.user.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class MessageExchangeSubActivity extends AppCompatActivity {
    public static String selectUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_exchange_layout);
        MessageExchangeTokenRecyclerAdapter adapter = new MessageExchangeTokenRecyclerAdapter(UniqueInfos.youMessageMap.getOrDefault(selectUserId,new ArrayList<>()).stream().sorted(Comparator.comparingLong(Message::getDate)).collect(Collectors.toList()), this);
        UniqueInfos.displayController.setRecyclerView(messageExchangeList, adapter, this);
        findViewById(messageSendButton).setOnClickListener(v -> {
            EditText sendText = findViewById(messageText);
            String sendMessage = sendText.getText().toString();
            Message message = Message.newInstance(UniqueInfos.getUser(), SharedInfos.userMap.get(selectUserId), "", sendMessage);
            message.send();
            sendText.setText("");
            MessageExchangeTokenRecyclerAdapter adap = new MessageExchangeTokenRecyclerAdapter(UniqueInfos.youMessageMap.getOrDefault(selectUserId,new ArrayList<>()).stream().sorted(Comparator.comparingLong(Message::getDate)).collect(Collectors.toList()), this);
            UniqueInfos.displayController.setRecyclerView(messageExchangeList, adap, this);
        });

        TextView userNameText = findViewById(otherUserName);
        userNameText.setText(SharedInfos.userMap.get(selectUserId).getName());
    }
}
