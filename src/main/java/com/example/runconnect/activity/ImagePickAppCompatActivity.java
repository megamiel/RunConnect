package com.example.runconnect.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.appcompat.app.AppCompatActivity;

import com.example.runconnect.function.UnaryFunction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Random;
import java.util.stream.IntStream;

public class ImagePickAppCompatActivity extends AppCompatActivity {
    private static Random rnd=new Random();
    private static String imageId;
    private static final int REQUEST_GALLERY = 0;
    private UnaryFunction<String> function;

    protected void startPickImageActivity(UnaryFunction<String> function) {
        this.function=function;
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
                imageId = randomId.toString();
                StorageReference reference = storage.getReference().child(imageId);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                reference.putBytes(baos.toByteArray());
                function.exe(imageId);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public String getImageId(){
        return imageId;
    }
}
