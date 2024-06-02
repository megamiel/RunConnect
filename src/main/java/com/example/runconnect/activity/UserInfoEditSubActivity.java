package com.example.runconnect.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.runconnect.R.id.*;
import static com.example.runconnect.R.layout.*;

import com.example.runconnect.activity.main.RunConnectMainActivity;
import com.example.runconnect.info.UniqueInfos;
import com.example.runconnect.user.User;
import com.example.runconnect.function.Functions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Random;
import java.util.stream.IntStream;

public class UserInfoEditSubActivity extends AppCompatActivity {
    public static boolean isEditing;
    private static final int REQUEST_GALLERY = 1;

    private Random rnd;
    private static String randomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(user_info_edit_layout);

        rnd = new Random();

        User user = UniqueInfos.getUser();
        EditText nameEditText = findViewById(nameEditLabel);
        nameEditText.setText(user.getName());
        String beforeImageId = user.getImageId();
        randomId = beforeImageId;
        ImageButton iconEditButton = findViewById(userIconEditButton);
        UniqueInfos.db.downloadImage(user.getImageId(), Functions.setImageUri(RunConnectMainActivity.getInstance(), iconEditButton));
        iconEditButton.setOnClickListener(v -> {
            startPickImageActivity();
        });

        findViewById(infoEditCancelButton).setOnClickListener(v -> {
            isEditing = false;
            finish();
        });

        findViewById(infoEditSaveButton).setOnClickListener(v -> {
            user.setName(nameEditText.getText().toString());
            if (!beforeImageId.equals(randomId)) {
                user.setImageId(randomId);
                ImageButton userInfoImageButton = RunConnectMainActivity.getInstance().findViewById(userInfoButton);
                UniqueInfos.db.downloadImage(user.getImageId(), Functions.setImageUri(RunConnectMainActivity.getInstance(), userInfoImageButton));
            }
            // テスト段階が終わり、全てのアイコンが単一のものからしか参照されなくなったらコメントアウトなくす
//            RunConnectMainActivity.db.deleteImage(beforeImageId);
            user.save(UniqueInfos.db, () -> System.out.println("保存完了"));
            isEditing = false;
            finish();
        });
    }

    private void startPickImageActivity() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    // 写真選択されたときのイベント処理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            try (InputStream in = getContentResolver().openInputStream(intent.getData())) {
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StringBuilder randomId = new StringBuilder();
                IntStream.range(0, 10).mapToLong(i -> rnd.nextLong()).forEach(randomId::append);
                UserInfoEditSubActivity.randomId = randomId.toString();
                StorageReference reference = storage.getReference().child(UserInfoEditSubActivity.randomId);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                reference.putBytes(baos.toByteArray());
                ImageButton iconEditbutton = findViewById(userIconEditButton);
                iconEditbutton.setImageBitmap(bitmap);
                iconEditbutton.setScaleType(ImageView.ScaleType.FIT_XY);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        isEditing = false;
        finish();
    }
}
