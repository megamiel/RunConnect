package com.example.runconnect.contents;

import static com.example.runconnect.R.id.rankingList;
import static com.example.runconnect.R.id.rankingTopButton;
import static com.example.runconnect.R.id.rankingYouButton;
import static com.example.runconnect.R.layout.ranking_layout;
import static com.example.runconnect.R.layout.ranking_token_layout;

import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runconnect.activity.main.RunConnectMainActivity;
import com.example.runconnect.library.ButtonRegister;
import com.example.runconnect.user.User;
import com.example.runconnect.info.SharedInfos;
import com.example.runconnect.info.UniqueInfos;
import com.example.runconnect.ranking.RankingTokenRecyclerAdapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankingContents {
    public static void start(){
        UniqueInfos.displayController.changeMainContents(ranking_layout);

        List<User> rankingTokenList = getRankingTokenList();
        RecyclerView recyclerView = UniqueInfos.displayController.setRecyclerView(rankingList, new RankingTokenRecyclerAdapter(rankingTokenList, ranking_token_layout));
        ButtonRegister.regist(RunConnectMainActivity.getInstance(),rankingTopButton, () -> recyclerView.smoothScrollToPosition(0));
        ButtonRegister.regist(RunConnectMainActivity.getInstance(),rankingYouButton, () -> recyclerView.smoothScrollToPosition(rankingTokenList.indexOf(UniqueInfos.getUser())));
    }
    // ランキングリストを作成、全てのユーザ情報を取得し、何かのポイント(ランクなど)で降順に並べ替えし返す
    private static List<User> getRankingTokenList() {
        List<User> rankingTokenList = Arrays.asList(SharedInfos.userMap.values().toArray(new User[0]));
        rankingTokenList.sort(Comparator.comparingInt(User::toPoint));
        Collections.reverse(rankingTokenList);
        return rankingTokenList;
    }}
