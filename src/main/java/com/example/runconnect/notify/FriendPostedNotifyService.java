package com.example.runconnect.notify;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.runconnect.info.SharedInfos;
import com.example.runconnect.info.UniqueInfos;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class FriendPostedNotifyService extends Service {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        firestore.collection("posts").addSnapshotListener((snapshot, exception) -> {
            if (exception != null || UniqueInfos.isBooting || snapshot == null || snapshot.isEmpty()) {
                return;
            }

            snapshot.getDocumentChanges().forEach(dc -> {
                if (dc.getType() != DocumentChange.Type.ADDED) {
                    return;
                }
                Map<String, Object> postMap = dc.getDocument().getData();
                if ((UniqueInfos.getUser().getFriendIdList().contains(postMap.get("userId").toString())) && SharedInfos.postMap.get(postMap.get("postId").toString()).getNeedNotified()) {
                    String postedFriend = SharedInfos.userMap.get(postMap.get("userId")).getName();
                    String postedContent = postMap.get("postText").toString();
                    FriendPostedNotify.getInstance().notification(postedFriend, postedContent);
                }
            });
        });
        return super.onStartCommand(intent, flags, startId);
    }
}