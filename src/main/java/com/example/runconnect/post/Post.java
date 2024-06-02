package com.example.runconnect.post;

import android.net.Uri;

import com.example.runconnect.function.Functions;
import com.example.runconnect.function.NullaryFunction;
import com.example.runconnect.info.UniqueInfos;
import com.example.runconnect.library.FirebaseDatabase;
import com.example.runconnect.library.Pointer;
import com.example.runconnect.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Post {
    private String postImageId;
    private Pointer<Uri> postImageUri;
    private String postId;
    private final User user;
    private String postText;
    private int viewCount;
    private int goodCount;
    private List<String> responseIdList;
    private long date;

    private boolean needNotified = true;

    private Post(User user, String postText, int viewCount, int goodCount, List<String> responseIdList, long date, String postImageId) {
        // idを設定(連番でいい)
        this.user = user;
        this.postText = postText;
        this.viewCount = viewCount;
        this.goodCount = goodCount;
        this.responseIdList = responseIdList;
        this.date = date;
        this.postImageId = postImageId;
    }


    public void editPostText(String postText) {
        this.postText = postText;
    }

    public void incrementGoodCount() {
        goodCount++;
    }

    public void incrementViewCount() {
        viewCount++;
    }

    public void addResponseList(String response) {
        responseIdList.add(response);
        save(UniqueInfos.db, () -> System.out.println("返信完了"));
    }


    public User getUser() {
        return user;
    }

    public String getPostImageId() {
        return postImageId;
    }

    public Uri getPostImageUri() {
        return postImageUri.get();
    }

    public String getPostId() {
        return postId;
    }

    public String getPostText() {
        return postText;
    }

    public int getGoodCount() {
        return goodCount;
    }


    public int getViewCount() {
        return viewCount;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setPostImageUri(Pointer<Uri> postImageUri) {
        this.postImageUri = postImageUri;
    }

    public void setNeedNotified(boolean needNotified) {
        this.needNotified = needNotified;
    }

    public List<String> getResponseIdList() {
        return responseIdList;
    }

    public long getDate() {
        return date;
    }

    public boolean getNeedNotified() {
        return needNotified;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("postId", postId);
        data.put("userId", user.getId());
        data.put("postText", postText);
        data.put("viewCount", viewCount);
        data.put("goodCount", goodCount);
        data.put("postImageId", postImageId);
        data.put("date", date);
        StringBuilder responseIdsString = new StringBuilder();
        responseIdList.stream().forEach(friendId -> responseIdsString.append(friendId).append(","));
        data.put("response", responseIdsString.toString());
        return data;
    }

    public void save(FirebaseDatabase db, NullaryFunction function) {
        db.write("posts", getPostId(), toMap(), function);
    }

    public static List<String> responseIdsToList(String responseIds) {
        List<String> responseIdsList = new ArrayList<>();
        if (!responseIds.isEmpty()) {
            responseIdsList.addAll(Arrays.asList(responseIds.split(",")));
        }
        return responseIdsList;
    }

    public static Post newInstance(User user, String postText, String postImageId) {
        Post post = new Post(user, postText, 0, 0, new ArrayList<>(), Functions.getCurrentTime(), postImageId);
        post.setPostId(Functions.getRandomId());
        return post;
    }


    public static Post newInstance(User user, String postText, int goodCount, int viewCount, List<String> responseList, long date, String postImageId) {
        return new Post(user, postText, goodCount, viewCount, responseList, date, postImageId);
    }


}
