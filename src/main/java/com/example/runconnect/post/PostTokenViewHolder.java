package com.example.runconnect.post;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.runconnect.R;

import static com.example.runconnect.R.id.*;

public class PostTokenViewHolder extends RecyclerView.ViewHolder {
    private final ImageView iconImageButton;
    private final TextView nameText;
    private final TextView postText;
    private final ImageButton responseButton;
    private final ImageButton goodButton;
    //    private final ImageButton shareButton;
    private final TextView responseNumText;
    private final TextView goodNumText;
    //    private final TextView shareNumText;
    private final ImageView postImage;
    private final TextView dateText;

    public PostTokenViewHolder(View view) {
        super(view);
        iconImageButton = view.findViewById(userIconButton);
        nameText = view.findViewById(nameLabel);
        postText = view.findViewById(postLabel);
        responseButton = view.findViewById(R.id.responseButton);
        goodButton = view.findViewById(R.id.goodButton);
        responseNumText = view.findViewById(responseNumLabel);
        goodNumText = view.findViewById(goodNumLabel);
        postImage = view.findViewById(R.id.postImage);
        dateText = view.findViewById(dateLabel);
    }

    public ImageView getIconImageButton() {
        return iconImageButton;
    }

    public TextView getNameText() {
        return nameText;
    }

    public TextView getPostText() {
        return postText;
    }

    public ImageButton getResponseButton() {
        return responseButton;
    }

    public ImageButton getGoodButton() {
        return goodButton;
    }

//    public ImageButton getShareButton() {
//        return shareButton;
//    }

    public TextView getResponseNumText() {
        return responseNumText;
    }

    public TextView getGoodNumText() {
        return goodNumText;
    }

//    public TextView getShareNumText() {
//        return shareNumText;
//    }

    public ImageView getPostImage() {
        return postImage;
    }

    public TextView getDateText() {
        return dateText;
    }
}
