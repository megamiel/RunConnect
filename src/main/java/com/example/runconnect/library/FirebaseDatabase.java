package com.example.runconnect.library;


import android.net.Uri;

import com.example.runconnect.function.NullaryFunction;
import com.example.runconnect.function.UnaryFunction;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Map;

public class FirebaseDatabase {
    private FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    public void write(String tag, String id, Map<String, Object> map, NullaryFunction function) {
        fireStore.collection(tag).document(id).set(map).addOnSuccessListener(d -> function.exe()).addOnFailureListener(e -> function.exe());
    }

    public void read(String tag, ArrayList<Map<String, Object>> dataList, NullaryFunction function) {
        fireStore.collection(tag).get().addOnCompleteListener(task -> {
            setList(task, dataList);
            function.exe();
        });
    }

    private void setList(Task<QuerySnapshot> task, ArrayList<Map<String, Object>> dataList) {
        if (task.isSuccessful()) {
            task.getResult().forEach(document -> dataList.add(document.getData()));
        }
    }

    public void registerUpdateNotify(String tag, UnaryFunction<DocumentChange> function) {
        fireStore.collection(tag).addSnapshotListener((snapshot, exception) -> updateNotify(snapshot, exception, function));
    }

    private void updateNotify(QuerySnapshot snapshot, Exception exception, UnaryFunction<DocumentChange> function) {
        if (exception == null && snapshot != null && !snapshot.isEmpty()) {
            documentUpdate(snapshot, function);
        }
    }

    private void documentUpdate(QuerySnapshot snapshot, UnaryFunction<DocumentChange> function) {
        snapshot.getDocumentChanges().forEach(function::exe);
    }


    // db.downloadImage(imageId,Functions.setImageUri(this,iconImage));
    // 上記のように呼び出せばiconImage部分のコンテキストにimageIdに対応した画像が貼り付けられる
    // 初期のロード設定以外では呼び出さないようにする
    public void downloadImage(String id, UnaryFunction<Uri> function) {
        if (id == null || id.isEmpty()) {
            return;
        }
        if (function == null) {
            function = uri -> {
            };
        }
        storage.getReference().child(id).getDownloadUrl().addOnSuccessListener(function::exe);
    }

    public void deleteImage(String id) {
        storage.getReference().child(id).delete();
    }

    public FirebaseFirestore getFireStoreInstance() {
        return fireStore;
    }
}
