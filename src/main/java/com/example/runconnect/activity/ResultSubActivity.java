package com.example.runconnect.activity;

import static com.example.runconnect.R.id.elapsedTimeLabel;
import static com.example.runconnect.R.id.movingDistanceLabel;
import static com.example.runconnect.R.id.progressBar;
import static com.example.runconnect.R.id.rankUpNotifyLabel;
import static com.example.runconnect.R.id.requireAndRankPointLabel;
import static com.example.runconnect.R.id.resultFinishButton;
import static com.example.runconnect.R.layout.result_layout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.runconnect.user.User;
import com.example.runconnect.info.UniqueInfos;

public class ResultSubActivity extends AppCompatActivity {

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(result_layout);
        TextView movingDistanceText = findViewById(movingDistanceLabel);
        TextView elapsedTimeText = findViewById(elapsedTimeLabel);
        double movingDistance = RunningSubActivity.getMovingDistance();
        int elapsedTime = RunningSubActivity.getElapsedTime();

        // 移動距離が1000m以上であれば、km単位で表示する

        String printedMovingDistance;
        if (movingDistance >= 1000) {
            printedMovingDistance = (int) (movingDistance / 100) / 10.0 + " km";
        } else {
            printedMovingDistance = (int) movingDistance + " m";
        }

        movingDistanceText.setText(printedMovingDistance);

        elapsedTimeText.setText(elapsedTime + "秒");

        // 移動距離や経過時間からランクポイントを算出
        // まだどのようにポイントを決定するか決めていないため、現在では経過時間が取得ポイントとなるように作成してある

        int rankPoint = (int) (movingDistance * elapsedTime / 100);


        User user = UniqueInfos.getUser();
        user.addTotalMovingDinstance(movingDistance);
        int beforeRank = user.getRank();
        user.addRankPoint(rankPoint);
        int afterRank = user.getRank();
        int afterRankPoint = user.getRankPoint();

        int requireRankPoint = User.getRequireRankPoint(afterRank);
        int afterPercent;
        if (requireRankPoint == 0) {
            afterPercent = 100;
        } else {
            afterPercent = afterRankPoint * 100 / requireRankPoint;
        }
        ProgressBar bar = findViewById(progressBar);
        bar.setProgress(afterPercent, false);

        TextView requireAndRankPointText = findViewById(requireAndRankPointLabel);
        requireAndRankPointText.setText(afterRankPoint + "/" + requireRankPoint);

        if (beforeRank < afterRank) {
            TextView rankUpNotifyText = findViewById(rankUpNotifyLabel);
            rankUpNotifyText.setText(String.format("          ランクアップ！ HR:%d → %d", beforeRank, afterRank));
        }


        user.save(UniqueInfos.db, () -> System.out.println("保存完了"));
        findViewById(resultFinishButton).setOnClickListener(v -> finish());
    }

}
