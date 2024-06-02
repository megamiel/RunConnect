package com.example.runconnect.friend;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AnotherFriendTokenViewHolder extends RecyclerView.ViewHolder {
    private ImageView anotherIconImage;
    private TextView anotherNameText;
    private TextView anotherRankText;
    private Button anotherFriendRequestButton;

    public AnotherFriendTokenViewHolder(@NonNull View view, int iconImageId, int nameLabelId, int rankLabelId, int friendRequestButtonId) {
        super(view);
        anotherIconImage = view.findViewById(iconImageId);
        anotherNameText = view.findViewById(nameLabelId);
        anotherRankText = view.findViewById(rankLabelId);
        anotherFriendRequestButton = view.findViewById(friendRequestButtonId);
    }

    public ImageView getAnotherIconImage() {
        return anotherIconImage;
    }

    public TextView getAnotherNameText() {
        return anotherNameText;
    }

    public TextView getAnotherRankText() {
        return anotherRankText;
    }

    public Button getAnotherFriendRequestButton() {
        return anotherFriendRequestButton;
    }
}
