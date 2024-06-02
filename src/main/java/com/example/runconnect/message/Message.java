package com.example.runconnect.message;

import com.example.runconnect.function.Functions;
import com.example.runconnect.info.UniqueInfos;
import com.example.runconnect.user.User;

import java.util.HashMap;
import java.util.Map;

public class Message {
    private String id;
    private User sendUser;
    private User receiveUser;
    private long date;
    private String imageId;
    private String text;

    // 既存メッセージを作成時に使用
    public Message(String id, User sendUser, User receiveUser, long date, String imageId, String text) {
        this.id = id;
        this.sendUser = sendUser;
        this.receiveUser = receiveUser;
        this.date = date;
        this.imageId = imageId;
        this.text = text;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("sendUser", sendUser.getId());
        data.put("receiveUser", receiveUser.getId());
        data.put("date", date);
        data.put("image", imageId);
        data.put("text", text);
        return data;
    }

    // 送信機能
    public void send() {
        UniqueInfos.db.write("messages", id, toMap(), () -> System.out.println("送信しました"));
        updateUserMessageData(sendUser);
        updateUserMessageData(receiveUser);
    }

    private void updateUserMessageData(User user) {
        StringBuilder userNewMessageData = new StringBuilder();
        UniqueInfos.db.getFireStoreInstance().collection("users").document(user.getId()).get().addOnCompleteListener(task -> {
            userNewMessageData.append(task.getResult().getData().get("message").toString()).append(id).append(',');
            UniqueInfos.db.getFireStoreInstance().collection("users").document(user.getId()).update("message", userNewMessageData.toString());
        });
    }

    public String getId() {
        return id;
    }

    public User getSendUser() {
        return sendUser;
    }

    public User getReceiveUser() {
        return receiveUser;
    }

    public long getDate() {
        return date;
    }

    public String getImageId() {
        return imageId;
    }

    public String getText() {
        return text;
    }

    // 新規メッセージを作成時に使用
    public static Message newInstance(User sendUser, User receiveUser, String imageId, String text) {
        return new Message(Functions.getRandomId(), sendUser, receiveUser, Functions.getCurrentTime(), imageId, text);
    }
}
