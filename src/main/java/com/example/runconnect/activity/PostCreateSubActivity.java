package com.example.runconnect.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.runconnect.R;
import com.example.runconnect.activity.main.RunConnectMainActivity;
import com.example.runconnect.function.Functions;
import com.example.runconnect.info.UniqueInfos;
import com.example.runconnect.library.ButtonRegister;
import com.example.runconnect.library.Pointer;
import com.example.runconnect.post.Post;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.example.runconnect.R.layout.*;
import static com.example.runconnect.R.id.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class PostCreateSubActivity extends AppCompatActivity {
    public static String id = null;
    public static boolean isEditing;
    private static final int REQUEST_GALLERY = 1;
    Pointer<String> pickImageId = new Pointer<>("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(post_create_layout);

        // 投稿内容作成の設定、投稿ボタンを押されたらeditTextからテキスト読みだしたりしてインスタンス生成してがんばれ
        // 以下の方法で投稿を行うことができる


        EditText inputPostText = findViewById(R.id.postText);
        inputPostText.requestFocus();

        ImageView pickImage=findViewById(friendIconImageButton);
        UniqueInfos.db.downloadImage(UniqueInfos.getUser().getImageId(), Functions.setImageUri(this, pickImage));

        findViewById(pickImagePostButton).setOnClickListener(v -> {
            startPickImageActivity();
        });


        ButtonRegister.regist(this, postSaveButton, () -> {
            String text = inputPostText.getText().toString();
            // 写真が選ばれているか確認し、あるならidを、なければ""を代入するプログラムを書かねば
            // 投稿する写真を選べるようにする
            if (text.isEmpty() && pickImageId.get().isEmpty()) {
                return;
            }
            Post post = Post.newInstance(UniqueInfos.getUser(), text, pickImageId.get());
            post.save(UniqueInfos.db, () -> System.out.println("投稿しました"));
            id = post.getPostId();
            Toast.makeText(this, "投稿しました", Toast.LENGTH_SHORT).show();
            finish();
        });

        ButtonRegister.regist(this, postCancelButton, this::finish);
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
                String randomId = Functions.getRandomId();
                StorageReference reference = storage.getReference().child(randomId);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                reference.putBytes(baos.toByteArray());
                ImageView pickImagePost = findViewById(pickedImage);
                pickImagePost.setImageBitmap(bitmap);
                pickImagePost.setScaleType(ImageView.ScaleType.FIT_XY);
                pickImageId.set(randomId);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
