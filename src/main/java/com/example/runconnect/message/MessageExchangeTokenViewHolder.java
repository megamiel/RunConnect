package com.example.runconnect.message;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.runconnect.R;

public class MessageExchangeTokenViewHolder extends RecyclerView.ViewHolder {
    private final View view;

    public MessageExchangeTokenViewHolder(View view) {
        super(view);
        this.view = view;
    }

    public View getView() {
        return view;
    }

    public ImageButton getMessageIconImageButton() {
        try {
            return view.findViewById(R.id.messageIconImageButton);
        } catch (Exception ex) {
            return null;
        }
    }

    public TextView getMessageTextText() {
        return view.findViewById(R.id.messageTextLabel);
    }

    public TextView getMessageTimeText() {
        return view.findViewById(R.id.messageTimeLabel);
    }
}
