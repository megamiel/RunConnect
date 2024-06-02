package com.example.runconnect.contents;

import static com.example.runconnect.R.id.keywordSearchButton;
import static com.example.runconnect.R.id.keywordSearchEdit;
import static com.example.runconnect.R.id.keywordSearchList;
import static com.example.runconnect.R.layout.keyword_search_layout;
import static com.example.runconnect.R.layout.post_token_layout;

import android.graphics.Color;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.runconnect.info.SharedInfos;
import com.example.runconnect.info.UniqueInfos;
import com.example.runconnect.library.ButtonRegister;
import com.example.runconnect.post.Post;
import com.example.runconnect.post.PostTokenRecyclerAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PostSearchContents {

    // キーワード検索レイアウトを作成すること

    public static void start(AppCompatActivity app) {
        // 以下はid検索画面のメインコンテンツとなっているため、キーワード検索レイアウトに変更すること
        UniqueInfos.displayController.changeMainContents(keyword_search_layout);
        registerKeywordSearchButton(app);
    }

    // キーワード検索ボタンを登録
    private static void registerKeywordSearchButton(AppCompatActivity app) {

        ButtonRegister.regist(app, keywordSearchButton, () -> keywordSearchButtonEventHandling(app));
    }

    // キーワード検索ボタンを押された時の処理(ハッシュタグ検索的なやつもほしい)
    private static void keywordSearchButtonEventHandling(AppCompatActivity app) {

        EditText keywordSearchEditText = app.findViewById(keywordSearchEdit);
        String searchKeyword = keywordSearchEditText.getText().toString();
        keywordSearchEditText.setHintTextColor(searchKeyword.isEmpty() ? Color.RED : Color.GRAY);
        if (searchKeyword.isEmpty()) {
            return;
        }
        List<Post> searchKeywordList = SharedInfos.postMap.values().stream().filter(post-> post.getPostText().contains(searchKeyword)).sorted(Comparator.comparingLong(Post::getDate)).collect(Collectors.toList());
        Collections.reverse(searchKeywordList);
        UniqueInfos.displayController.setRecyclerView(keywordSearchList,new PostTokenRecyclerAdapter(searchKeywordList,post_token_layout));
    }
}
