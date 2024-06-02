package com.example.runconnect.friend;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class FriendTokenViewHolder extends RecyclerView.ViewHolder {
    private ImageView iconImage;
    private TextView friendNameText;
    private TextView friendRankText;

    public FriendTokenViewHolder(View view, int friendNameId, int friendRankId,int iconImageId) {
        super(view);
        iconImage=view.findViewById(iconImageId);
        friendNameText = view.findViewById(friendNameId);
        friendRankText = view.findViewById(friendRankId);
    }

    public ImageView getIconImage(){
        return iconImage;
    }
    public TextView getFriendNameText() {
        return friendNameText;
    }

    public TextView getFriendRankText() {
        return friendRankText;
    }
}
