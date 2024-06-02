package com.example.runconnect.library;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runconnect.function.NullaryFunction;

public class ExtendedOnScrollListener extends RecyclerView.OnScrollListener {
    private NullaryFunction headFunction;
    private NullaryFunction tailFunction;

    public ExtendedOnScrollListener(NullaryFunction headFunction, NullaryFunction tailFunction) {
        this.headFunction = headFunction;
        this.tailFunction = tailFunction;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        // 先頭
        if (!recyclerView.canScrollVertically(-1)) {
            headFunction.exe();
        }

        // 末尾
        if (!recyclerView.canScrollVertically(1)) {
            tailFunction.exe();
        }
    }
}
