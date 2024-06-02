package com.example.runconnect.ranking;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RankingTokenViewHolder extends RecyclerView.ViewHolder {
    private ImageView iconImage;
    private TextView rankingNumText;
    private TextView rankingNameText;
    private TextView rankPointText;

    public RankingTokenViewHolder(View view, int rankingNumId, int rankingNameId, int iconImageId, int rankPointLabelId) {
        super(view);
        iconImage = view.findViewById(iconImageId);
        rankingNumText = view.findViewById(rankingNumId);
        rankingNameText = view.findViewById(rankingNameId);
        rankPointText = view.findViewById(rankPointLabelId);
    }

    public ImageView getIconImage() {
        return iconImage;
    }

    public TextView getRankingNumText() {
        return rankingNumText;
    }

    public TextView getRankingNameText() {
        return rankingNameText;
    }

    public TextView getRankPointText() {
        return rankPointText;
    }
}

