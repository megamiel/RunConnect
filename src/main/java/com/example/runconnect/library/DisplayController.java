package com.example.runconnect.library;

import static com.example.runconnect.R.id.mainContents;

import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runconnect.function.NullaryFunction;

public class DisplayController {
    private AppCompatActivity app;

    public DisplayController(AppCompatActivity app) {
        this.app = app;
    }

    public void setContents(int layoutId) {
        app.setContentView(layoutId);
    }

    public void changeMainContents(int layoutId) {
        LinearLayout layout = app.findViewById(mainContents);
        layout.removeAllViews();
        app.getLayoutInflater().inflate(layoutId, layout);
    }

    // デフォルトはメインアクティビティだが、引数を追加で渡すことでサブアクティビティにも設定できる(変更先のリニアレイアウトのidも必要)
    public void changeMainContents(int layoutId,AppCompatActivity app,int linearId){
        LinearLayout layout = app.findViewById(linearId);
        layout.removeAllViews();
        app.getLayoutInflater().inflate(layoutId, layout);
    }

    // リサイクラービュー
    // id:リサイクラービューのid
    // Adapter:トークン
    public <V extends RecyclerView.ViewHolder> RecyclerView setRecyclerView(int id, RecyclerView.Adapter<V> adapter) {
        RecyclerView recyclerView = app.findViewById(id);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(app, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(app));
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    // 最後のappはデフォルトでメインアクティビティになっているが、引数で他のアクティビティを指定することもできる
    public <V extends RecyclerView.ViewHolder> RecyclerView setRecyclerView(int id, RecyclerView.Adapter<V> adapter,AppCompatActivity app) {
        RecyclerView recyclerView = app.findViewById(id);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(app, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(app));
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    // リサイクラービュー(リストの一番上、一番下に辿り着いたときに実行してほしい処理を引数として渡せる)
    public <V extends RecyclerView.ViewHolder> RecyclerView setRecyclerView(int id, RecyclerView.Adapter<V> adapter, NullaryFunction headFunction, NullaryFunction tailFunction) {
        RecyclerView recyclerView = setRecyclerView(id, adapter);
        recyclerView.addOnScrollListener(new ExtendedOnScrollListener(headFunction, tailFunction));
        return recyclerView;
    }


}