package com.example.runconnect.contents;

import static com.example.runconnect.R.id.postList;
import static com.example.runconnect.R.layout.post_layout;
import static com.example.runconnect.R.layout.post_token_layout;

import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.runconnect.activity.PostCreateSubActivity;
import com.example.runconnect.function.Functions;
import com.example.runconnect.info.SharedInfos;
import com.example.runconnect.info.UniqueInfos;
import com.example.runconnect.library.ActivityRunner;
import com.example.runconnect.library.ThreadRunner;
import com.example.runconnect.post.Post;
import com.example.runconnect.post.PostTokenRecyclerAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.runconnect.R.id.*;

public class PostContents {
    public static void start(AppCompatActivity app) {
        UniqueInfos.displayController.changeMainContents(post_layout);
        UniqueInfos.displayController.setRecyclerView(postList, PostTokenRecyclerAdapter.newInstance(getPostTokenList(), post_token_layout));

        Button keywordSearchButton = app.findViewById(searchPostButton);
        Button postButton = app.findViewById(postingButton);

        keywordSearchButton.setOnClickListener(v -> {
            // メインコンテンツを変更し、キーワードを入力するとキーワードを空白区切りにし、全てのキーワードが入力されている投稿を一覧表示する
            PostSearchContents.start(app);
        });

        postButton.setOnClickListener(v -> {
//            Intent intent = new Intent(app, PostCreateSubActivity.class);
//            app.startActivity(intent);
            ActivityRunner.start(app, PostCreateSubActivity.class);
        });

    }

    // 表示する投稿リストを作成(投稿は一定数まで追加する)
    // 友人や他の人の投稿を一定数追加し、返す
    private static List<Post> getPostTokenList() {
        // 条件を満たしていればランダム値分だけポイントを付与されて、そのポイント順に並べ替える
        // ランダム値を大きくすれば、おすすめポストが上に来やすくできるはず
        List<Post> postList = SharedInfos.postMap.values().stream().sorted(Comparator.comparingLong(Post::getDate)).collect(Collectors.toList());
        Collections.reverse(postList);
        return postList;
    }
}
