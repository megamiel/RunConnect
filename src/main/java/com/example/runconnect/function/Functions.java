package com.example.runconnect.function;

import android.location.Location;
import android.net.Uri;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.runconnect.info.SharedInfos;
import com.example.runconnect.info.UniqueInfos;
import com.example.runconnect.library.Consts;
import com.example.runconnect.library.Pointer;
import com.example.runconnect.message.Message;
import com.example.runconnect.post.Post;
import com.example.runconnect.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Functions {
    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception ignored) {

        }
    }

    public static double getDistance(Location startLocation, Location endLocation) {
        float[] results = new float[1];
        if (startLocation == null || endLocation == null) {
            return 0;
        }
        Location.distanceBetween(startLocation.getLatitude(), startLocation.getLongitude(), endLocation.getLatitude(), endLocation.getLongitude(), results);
        return results[0];
    }

    public static UnaryFunction<Uri> setImageUri(AppCompatActivity app, ImageView imageView) {
        System.out.println("setImageUriが叩かれたよ");
        try {
            if (imageView == null) {
                throw new Exception();
            }
            return uri -> Glide.with(app).load(uri).into(imageView);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static User toUser(Map<String, Object> data) {
        String id = data.get("id").toString();
        String name = data.get("name").toString();
        int rank = Integer.parseInt(data.get("rank").toString());
        int rankPoint = Integer.parseInt(data.get("rankPoint").toString());
        double totalMovingDistance = Double.parseDouble(data.get("totalMovingDistance").toString());
        Object imageIdObj = data.get("imageId");
        String imageId;
        Pointer<Uri> imageUri = new Pointer<>(null);
        if (imageIdObj == null) {
            imageId = null;
        } else {
            imageId = data.get("imageId").toString();
            UniqueInfos.db.downloadImage(imageId, imageUri::set);
        }
        List<String> friendIdList = idsToList(data.get("friend").toString());
        List<String> friendRequestList = idsToList(data.get("friendRequest").toString());
        List<String> goodPostList = idsToList(data.get("goodPost").toString());
        return User.getExistingInstance(id, name, rank, rankPoint, totalMovingDistance, friendIdList, imageId, friendRequestList, goodPostList);
    }

    public static Post toPost(Map<String, Object> data) {
        String postId = data.get("postId").toString();
        String userId = data.get("userId").toString();
        String postText = data.get("postText").toString();
        String postImageId = data.get("postImageId").toString();
        Pointer<Uri> postImageUri = new Pointer<>(null);
        UniqueInfos.db.downloadImage(postImageId, postImageUri::set);
        int viewCount = Integer.parseInt(data.get("viewCount").toString());
        int goodCount = Integer.parseInt(data.get("goodCount").toString());
        List<String> responseIdList = Post.responseIdsToList(data.get("response").toString());
        long date = Long.parseLong(data.get("date").toString());

        Post post = Post.newInstance(SharedInfos.userMap.get(userId), postText, viewCount, goodCount, responseIdList, date, postImageId);
        post.setPostImageUri(postImageUri);
        post.setPostId(postId);
        return post;
    }

    public static Message toMessage(Map<String, Object> data) {
        String id = data.get("id").toString();
        User sendUser = SharedInfos.userMap.get(data.get("sendUser").toString());
        User receiveUser = SharedInfos.userMap.get(data.get("receiveUser").toString());
        long date = Long.parseLong(data.get("date").toString());
        String image = data.get("image").toString();
        String text = data.get("text").toString();
        return new Message(id, sendUser, receiveUser, date, image, text);
    }

    public static List<String> idsToList(String ids) {
        ArrayList<String> IdsList = new ArrayList<>();
        if (!ids.isEmpty()) {
            IdsList.addAll(Arrays.asList(ids.split(",")));
        }
        return IdsList;
    }

    public static String listToIds(List<String> list) {
        StringBuilder ids = new StringBuilder();
        list.forEach(str -> ids.append(str).append(','));
        return ids.toString();
    }

    // 現在時刻をyyyymmddhhmmssの形で表した日付情報で返す
    public static long getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        return Long.parseLong(String.format("%d%s%s%s%s%s", calendar.get(Calendar.YEAR), zeroFormat(calendar.get(Calendar.MONTH) + 1), zeroFormat(calendar.get(Calendar.DAY_OF_MONTH)), zeroFormat(calendar.get(Calendar.HOUR) + calendar.get(Calendar.AM_PM) * 12), zeroFormat(calendar.get(Calendar.MINUTE)), zeroFormat(calendar.get(Calendar.SECOND))));
    }

    // 一桁であれば二桁目に0を追加し返す
    public static String zeroFormat(int num) {
        return (num < 10 ? "0" : "") + num;
    }

    // yyyymmddhhmmssで表される日付情報をマップ情報へ変換して返す
    public static Map<String, Integer> toMap(long time) {
        Map<String, Integer> dateMap = new HashMap<>();
        char[] timeSplitArray = String.valueOf(time).toCharArray();
        String[] keys = "year,month,day,hour,minute,second".split(",");
        dateMap.put(keys[0], Integer.parseInt("" + join(timeSplitArray[0], timeSplitArray[1]) + join(timeSplitArray[2], timeSplitArray[3])));
        IntStream.range(1, 6).forEach(i -> dateMap.put(keys[i], join(timeSplitArray[(i - 1) * 2 + 4], timeSplitArray[(i - 1) * 2 + 5])));
        return dateMap;
    }

    // 何秒差があるか返す
    public static long secondDiff(Map<String, Integer> before, Map<String, Integer> after) {
        Map<String, Integer> secondMap = Map.of("second", 1, "minute", Consts.MINUTE, "hour", Consts.HOUR, "day", Consts.DAY, "month", Consts.MONTH, "year", Consts.YEAR);
        return Stream.of("second", "minute", "hour", "day", "month", "year").mapToLong(key -> ((long) after.get(key) - before.get(key)) * secondMap.get(key)).sum();
    }

    // 文字列や文字を結合し、整数に変換して返す(数値以外が含まれている場合、エラーが発生する)
    public static <C> int join(C a, C b) {
        return Integer.parseInt(a.toString() + b.toString());
    }

    public static String getRandomId() {
        StringBuilder randomId = new StringBuilder();
        IntStream.range(0, 5).forEach(i -> randomId.append(UniqueInfos.rnd.nextLong()));
        return randomId.toString();
    }
}
