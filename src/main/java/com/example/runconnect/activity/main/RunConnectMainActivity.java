package com.example.runconnect.activity.main;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import static com.example.runconnect.R.id.*;
import static com.example.runconnect.R.layout.*;
import static com.google.firebase.firestore.DocumentChange.Type.*;

import com.example.runconnect.library.ActivityRunner;
import com.example.runconnect.message.Message;
import com.example.runconnect.notify.FriendPostedNotifyService;
import com.example.runconnect.user.User;
import com.example.runconnect.activity.RunningStartConfirmSubActivity;
import com.example.runconnect.contents.FriendContents;
import com.example.runconnect.contents.PostContents;
import com.example.runconnect.contents.RankingContents;
import com.example.runconnect.contents.UserInfoContents;
import com.example.runconnect.function.NullaryFunction;
import com.example.runconnect.function.Functions;
import com.example.runconnect.info.SharedInfos;
import com.example.runconnect.info.UniqueInfos;
import com.example.runconnect.library.ButtonRegister;
import com.example.runconnect.library.DataStorage;
import com.example.runconnect.library.DisplayController;
import com.example.runconnect.library.FirebaseDatabase;
import com.example.runconnect.load.Load;
import com.example.runconnect.post.Post;
import com.example.runconnect.contents.UserCreateContents;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

// 投稿機能の追加
public class RunConnectMainActivity extends AppCompatActivity {
    private static RunConnectMainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    // 初期化
    private void initialize() {
        setId();
        UniqueInfos.isBooting = true;
        UniqueInfos.displayController = new DisplayController(this);
        RunConnectMainActivity.instance = this;
        User.setRequireRankPoints();
        Load.loadingView(this, UniqueInfos.isLoading);
        readUsers(UniqueInfos.db, new ArrayList<>());
    }

    private void readUsers(FirebaseDatabase db, ArrayList<Map<String, Object>> userList) {
        db.read("users", userList, () -> {
            setUserMap(userList);
            registerUserInfoUpdateNotify(db);
            ArrayList<Map<String, Object>> postList = new ArrayList<>();
            readPosts(db, postList);
        });
    }

    private void readPosts(FirebaseDatabase db, ArrayList<Map<String, Object>> postList) {
        db.read("posts", postList, () -> {
            setPostMap(postList);
            registerPostInfoUpdateNotify(db);
            createUserAndStart();
        });
    }

    private void createUserAndStart() {
        NullaryFunction loginButtonClickFunction = () -> {
            DataStorage.write(this, "id", UniqueInfos.getUser().getId());
            start();
        };
        boolean isNew = User.isNew();
        if (isNew) {
            UserCreateContents.start(this, loginButtonClickFunction);
        } else {
            start();
        }

    }

    // ユーザ情報やファイアベースのデータをロードし終わったら開始してほしいメソッド
    private void start() {
        Intent intent = new Intent(this, FriendPostedNotifyService.class);
        stopService(intent);
        startService(intent);

        UniqueInfos.isLoading.set(false);
        UniqueInfos.displayController.setContents(run_connect_main_layout);
        registerMainButtons();
        ImageView infoButton = findViewById(userInfoButton);
        UniqueInfos.db.downloadImage(UniqueInfos.getUser().getImageId(), Functions.setImageUri(this, infoButton));
        PostContents.start(this);
    }


    // 端末に保存されたユーザのidを静的変数であるidに代入する
    private void setId() {
        UniqueInfos.id = DataStorage.read(this, "id");
    }

    private void setUserMap(ArrayList<Map<String, Object>> userList) {
        userList.forEach(data -> {
            User user = Functions.toUser(data);
            SharedInfos.userMap.put(user.getId(), user);
            setMessageMap(data, user);
        });
    }

    private void setPostMap(ArrayList<Map<String, Object>> postList) {
        postList.forEach(data -> {
            Post post = Functions.toPost(data);
            post.setNeedNotified(false);
            SharedInfos.postMap.put(post.getPostId(), post);
        });
    }

    private void setMessageMap(Map<String, Object> data, User user) {
        if (UniqueInfos.getUser() != null && user.getId().equals(UniqueInfos.getUser().getId())) {
            List<String> messageIdList = Functions.idsToList(data.get("message").toString());
            System.out.println(messageIdList.size());
            CollectionReference reference = UniqueInfos.db.getFireStoreInstance().collection("messages");
            messageIdList.forEach(id -> reference.document(id).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Message message = Functions.toMessage(task.getResult().getData());
                    UniqueInfos.addYouMessage(message);
                }
            }));
            registerMessageUpdateNotify(UniqueInfos.db);
        }
    }

    private void registerUserInfoUpdateNotify(FirebaseDatabase db) {
        db.registerUpdateNotify("users", this::userInfoUpdateNotify);
    }

    private void userInfoUpdateNotify(DocumentChange dc) {
        HashMap<DocumentChange.Type, NullaryFunction> userInfoUpdateNotifyMap = new HashMap<>();
        Map<String, Object> data = dc.getDocument().getData();
        userInfoUpdateNotifyMap.put(ADDED, () -> {
            User user = Functions.toUser(data);
            if (!SharedInfos.userMap.containsKey(user.getId())) {
                SharedInfos.userMap.put(user.getId(), user);
            }
        });
        userInfoUpdateNotifyMap.put(MODIFIED, () -> {
            User user = Functions.toUser(data);
            SharedInfos.userMap.put(user.getId(), user);
        });
        userInfoUpdateNotifyMap.put(REMOVED, () -> {
            String id = data.get("id").toString();
            SharedInfos.userMap.remove(id);
        });
        userInfoUpdateNotifyMap.get(dc.getType()).exe();
    }


    private void registerPostInfoUpdateNotify(FirebaseDatabase db) {
        db.registerUpdateNotify("posts", this::postInfoUpdateNotify);
    }

    private void messageUpdateNotify(DocumentChange dc) {
        Map<String, Object> messageData = dc.getDocument().getData();
        User user = UniqueInfos.getUser();
        if (messageData.get("sendUser").toString().equals(user.getId()) || messageData.get("receiveUser").toString().equals(user.getId())) {
            UniqueInfos.addYouMessage(Functions.toMessage(messageData));
        }
    }

    private void registerMessageUpdateNotify(FirebaseDatabase db) {
        db.registerUpdateNotify("messages", this::messageUpdateNotify);
    }

    private void postInfoUpdateNotify(DocumentChange dc) {
        HashMap<DocumentChange.Type, NullaryFunction> postInfoUpdateNotifyMap = new HashMap<>();
        Map<String, Object> data = dc.getDocument().getData();
        postInfoUpdateNotifyMap.put(ADDED, () -> {
            Post post = Functions.toPost(data);
            if (!SharedInfos.postMap.containsKey(post.getPostId())) {
                System.out.println(post.getPostText());
                SharedInfos.postMap.put(post.getPostId(), post);
            }
        });
        postInfoUpdateNotifyMap.put(MODIFIED, () -> {
            Post post = Functions.toPost(data);
            SharedInfos.postMap.put(post.getPostId(), post);
        });
        postInfoUpdateNotifyMap.put(REMOVED, () -> {
            String postId = data.get("postId").toString();
            SharedInfos.postMap.remove(postId);
        });
        postInfoUpdateNotifyMap.get(dc.getType()).exe();
    }

    // ユーザ情報ボタンを押された時の処理
    @SuppressLint("SetTextI18n")
    public void userInfoButtonEventHandling() {
        UserInfoContents.start(this);
    }

    // 投稿ボタンを押された時の処理
    private void postButtonEventHandling() {
        PostContents.start(this);
    }

    // フレンドボタンを押された時の処理
    private void friendButtonEventHandling() {
        FriendContents.start(this);
    }

    // ランキングボタンを押された時の処理
    private void rankingButtonEventHandling() {
        RankingContents.start();
    }

    // ランニングボタンを押された時の処理
    private void runningButtonEventHandling() {
        // インテントで画面遷移し、ランニングしますか？と聞き、はいを押すとランニング画面にIntentで遷移
        // RUNボタンをもう一度押すと、終了しますか？と表示し、はいを押すと、
        // 走った距離や時間などのリザルトを表示する画面へIntentで遷移し、その後何かボタンを押すと、リザルトインテントとランニングインテントを終了する
        ActivityRunner.start(this, RunningStartConfirmSubActivity.class);
    }

    // ユーザ情報ボタンを登録
    private void registerUserInfoButton() {
        ButtonRegister.regist(this, userInfoButton, this::userInfoButtonEventHandling);
    }

    // ポストボタンを登録
    private void registerPostButton() {
        ButtonRegister.regist(this, postButton, this::postButtonEventHandling);
    }

    // フレンドボタンを登録
    private void registerFriendButton() {
        ButtonRegister.regist(this, friendButton, this::friendButtonEventHandling);
    }

    // ランキングボタンを登録
    private void registerRankingButton() {
        ButtonRegister.regist(this, rankingButton, this::rankingButtonEventHandling);
    }

    // ランニングボタンを登録
    private void registerRunningButton() {
        ButtonRegister.regist(this, runningButton, this::runningButtonEventHandling);
    }

    // ボタンを登録(サブボタンの登録もしておくべき)
    private void registerMainButtons() {
        registerUserInfoButton();
        registerPostButton();
        registerFriendButton();
        registerRankingButton();
        registerRunningButton();
    }

    public static RunConnectMainActivity getInstance() {
        return instance;
    }

    // 戻るボタン
    @Override
    public void onBackPressed() {
        UniqueInfos.isBooting = false;
    }

    // ホームボタン
    @Override
    public void onUserLeaveHint() {
        UniqueInfos.isBooting = false;
        super.onUserLeaveHint();
    }
}