package com.example.runconnect.contents;

import static com.example.runconnect.R.id.distLabel;
import static com.example.runconnect.R.id.friendNumLabel;
import static com.example.runconnect.R.id.idLabel;
import static com.example.runconnect.R.id.myPostList;
import static com.example.runconnect.R.id.nameLabel;
import static com.example.runconnect.R.id.rankLabel;
import static com.example.runconnect.R.id.userInfoEditButton;
import static com.example.runconnect.R.id.userProfileIcon;
import static com.example.runconnect.R.layout.post_token_layout;
import static com.example.runconnect.R.layout.user_info_layout;

import android.content.Intent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.runconnect.R;
import com.example.runconnect.user.User;
import com.example.runconnect.activity.main.RunConnectMainActivity;
import com.example.runconnect.activity.UserInfoEditSubActivity;
import com.example.runconnect.function.Functions;
import com.example.runconnect.info.SharedInfos;
import com.example.runconnect.info.UniqueInfos;
import com.example.runconnect.library.ThreadRunner;
import com.example.runconnect.post.Post;
import com.example.runconnect.post.PostTokenRecyclerAdapter;

import java.util.List;
import java.util.stream.Collectors;
import static com.example.runconnect.R.id.*;

public class UserInfoContents {
    public static void start(AppCompatActivity app) {
        UniqueInfos.displayController.changeMainContents(user_info_layout);
        TextView idText = app.findViewById(idLabel);
        TextView nameText = app.findViewById(nameLabel);
        TextView rankText = app.findViewById(rankLabel);
        TextView friendNumText = app.findViewById(friendNumLabel);
        TextView distText = app.findViewById(distLabel);
        Button infoEditButton = app.findViewById(userInfoEditButton);
        ImageView iconImage = app.findViewById(userProfileIcon);
        ImageView infoImageButton = app.findViewById(R.id.userInfoButton);
        Button userFriendListButton=app.findViewById(friendListButton);

        User user = UniqueInfos.getUser();
        String imageId = user.getImageId();

        UniqueInfos.db.downloadImage(user.getImageId(), Functions.setImageUri(app, iconImage));

        idText.setText(user.getId());
        nameText.setText(user.getName());
        rankText.setText(String.valueOf(user.getRank()));
        friendNumText.setText(String.valueOf(user.getFriendIdList().size()));
        distText.setText(String.valueOf(user.getTotalMovingDistance()));
        infoEditButton.setOnClickListener(v -> {
            UserInfoEditSubActivity.isEditing = true;
            Intent intent = new Intent(RunConnectMainActivity.getInstance(), UserInfoEditSubActivity.class);
            app.startActivity(intent);
            ThreadRunner.start(() -> {
                while (UserInfoEditSubActivity.isEditing) {
                    Functions.sleep(500);
                }
                if (!imageId.equals(user.getImageId())) {
                    UniqueInfos.db.downloadImage(user.getImageId(), Functions.setImageUri(app, iconImage));
                    UniqueInfos.db.downloadImage(user.getImageId(), Functions.setImageUri(app, infoImageButton));
                }
                nameText.setText(UniqueInfos.getUser().getName());
            });
        });
        userFriendListButton.setOnClickListener(v->AnotherFriendContents.start(app,user));

        // 自身が投稿したポストを新しい順でリサイクラービューに貼り付ける
        List<Post> postList = SharedInfos.postMap.values().stream().filter(post -> post.getUser() == user).sorted((post0, post1) -> Long.compare(post1.getDate(), post0.getDate())).collect(Collectors.toList());
        UniqueInfos.displayController.setRecyclerView(myPostList, new PostTokenRecyclerAdapter(postList, post_token_layout));
    }
}
