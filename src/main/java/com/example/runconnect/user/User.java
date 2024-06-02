package com.example.runconnect.user;

import com.example.runconnect.function.NullaryFunction;
import com.example.runconnect.info.SharedInfos;
import com.example.runconnect.info.UniqueInfos;
import com.example.runconnect.library.FirebaseDatabase;
import com.example.runconnect.library.Pointer;
import com.example.runconnect.post.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class User {

    // 投稿idリスト
    // いいねした投稿idリスト
    // ランクごとにランクアップに必要なランクポイントの設定
    private String imageId;
    private String id;
    private String name;
    private int rank;
    private int rankPoint;
    private double totalMovingDistance;
    private List<String> friendIdList;
    private List<String> friendRequestList;
    private List<String> goodPostList;

    private static int[] requireRankPoints = new int[99];

    private User(String id, String name, int rank, int rankPoint, double totalMovingDistance, List<String> friendIdList, String imageId, List<String> friendRequestList, List<String> goodPostList) {
        this.id = id;
        this.name = name;
        this.rank = rank;
        this.rankPoint = rankPoint;
        this.totalMovingDistance = totalMovingDistance;
        this.friendIdList = friendIdList;
        this.imageId = imageId;
        this.friendRequestList = friendRequestList;
        this.goodPostList = goodPostList;
    }

    public String getImageId() {
        return imageId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRank() {
        return rank;
    }

    public int getRankPoint() {
        return rankPoint;
    }

    public double getTotalMovingDistance() {
        return totalMovingDistance;
    }

    public void addTotalMovingDinstance(double dist) {
        totalMovingDistance += dist;
    }

    public List<String> getFriendIdList() {
        return friendIdList;
    }

    public List<String> getFriendRequestList() {
        return friendRequestList;
    }


    public List<String> getGoodPostList() {
        return goodPostList;
    }

    // 申請される側のインスタンスにaddFriendRequest、引数に申請する側のidを渡す
    public void addFriendRequest(String id) {
        User requestUser = SharedInfos.userMap.get(id);

        if (requestUser.getFriendIdList().contains(this.id)) {
            requestUser.getFriendIdList().remove(getId());
            getFriendIdList().remove(requestUser.getId());
        }
        if (requestUser.getFriendRequestList().contains(this.id)) {
            requestUser.getFriendRequestList().remove(this.id);
            addFriend(id);
        } else {
            if (friendRequestList.contains(id)) {
                return;
            }
            friendRequestList.add(id);
        }
        save(UniqueInfos.db, () -> System.out.println("保存完了"));
        requestUser.save(UniqueInfos.db, () -> System.out.println("保存完了"));
    }

    public void addGoodPostList(String id) {
        Post post = SharedInfos.postMap.get(id);
        if (!goodPostList.contains(id)) {
            goodPostList.add(id);
            post.incrementGoodCount();
        }
        save(UniqueInfos.db, () -> System.out.println("ユーザ側に反映完了"));
        post.save(UniqueInfos.db, () -> System.out.println("グッド反映完了"));
    }

    public void addFriend(String id) {
        if (friendIdList.contains(id)) {
            return;
        }
        friendIdList.add(id);
        SharedInfos.userMap.get(id).getFriendIdList().add(this.id);
    }

    public void addRankPoint(int rankPoint) {
        this.rankPoint += rankPoint;
        if (rank > requireRankPoints.length) {
            return;
        }
        while (this.rankPoint >= requireRankPoints[rank - 1]) {
            this.rankPoint -= requireRankPoints[rank - 1];
            rank++;
            if (rank > requireRankPoints.length) {
                break;
            }
        }
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("name", name);
        data.put("rank", rank);
        data.put("rankPoint", rankPoint);
        data.put("totalMovingDistance", totalMovingDistance);
        data.put("imageId", imageId);
        StringBuilder friendIdsString = new StringBuilder();
        friendIdList.forEach(friendId -> friendIdsString.append(friendId).append(','));
        data.put("friend", friendIdsString.toString());
        StringBuilder friendRequestIdsString = new StringBuilder();
        friendRequestList.forEach(requestId -> friendRequestIdsString.append(requestId).append(','));
        data.put("friendRequest", friendRequestIdsString.toString());
        StringBuilder goodPostIdsString = new StringBuilder();
        goodPostList.forEach(goodPostId -> goodPostIdsString.append(goodPostId).append(','));
        data.put("goodPost", goodPostIdsString.toString());

        StringBuilder messageIdsString = new StringBuilder();
        if (this == UniqueInfos.getUser()) {
            UniqueInfos.messageMapToList().forEach(message -> messageIdsString.append(message.getId()).append(','));
            data.put("message", messageIdsString.toString());
        } else {
            UniqueInfos.db.getFireStoreInstance().collection("users").document(id).get().addOnCompleteListener(task -> {
                Object messageData = task.getResult().getData().get("message");
                if (messageData == null) {
                    data.put("message", "");
                } else {
                    data.put("message", messageData);
                }
            });
        }
        return data;
    }


    public Map<String, Object> toFirstMap() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("name", name);
        data.put("rank", rank);
        data.put("rankPoint", rankPoint);
        data.put("totalMovingDistance", totalMovingDistance);
        data.put("imageId", imageId);
        StringBuilder friendIdsString = new StringBuilder();
        friendIdList.forEach(friendId -> friendIdsString.append(friendId).append(','));
        data.put("friend", friendIdsString.toString());
        StringBuilder friendRequestIdsString = new StringBuilder();
        friendRequestList.forEach(requestId -> friendRequestIdsString.append(requestId).append(','));
        data.put("friendRequest", friendRequestIdsString.toString());
        StringBuilder goodPostIdsString = new StringBuilder();
        goodPostList.forEach(goodPostId -> goodPostIdsString.append(goodPostId).append(','));
        data.put("goodPost", goodPostIdsString.toString());
        data.put("message", "");
        return data;
    }

    public void save(FirebaseDatabase db, NullaryFunction function) {
        db.write("users", getId(), toMap(), function);
    }

    public void firstSave(FirebaseDatabase db, NullaryFunction function) {
        db.write("users", getId(), toFirstMap(), function);
    }

    public int toPoint() {
        return IntStream.rangeClosed(2, rank).map(i -> requireRankPoints[i - 2]).sum() + rankPoint;
    }

    public static User getExistingInstance(String id, String name, int rank, int rankPoint, double totalMovingDistance, List<String> friendIdList, String imageId, List<String> friendRequestList, List<String> goodPostList) {
        return new User(id, name, rank, rankPoint, totalMovingDistance, friendIdList, imageId, friendRequestList, goodPostList);
    }

    // 新規ユーザの場合、このメソッドでインスタンスを作成する
    public static User getNewInstance(String id, String name) {
        return new User(id, name, 1, 0, 0, new ArrayList<>(), "", new ArrayList<>(), new ArrayList<>());
    }

    // スマホに自身のユーザidを保存していなければtrue,保存していればfalse
    public static boolean isNew() {
        return UniqueInfos.id == null || !SharedInfos.userMap.containsKey(UniqueInfos.id);
    }


    public static int getRequireRankPoint(int rank) {
        if (rank - 1 < requireRankPoints.length) {
            return requireRankPoints[rank - 1];
        } else {
            System.out.println(rank);
            System.out.println("ふぉーるすのほうをとおったよ");
            return 0;
        }
    }

    // 必要ランクポイントの設定は後回しとする
    public static void setRequireRankPoints() {
        requireRankPoints[0] = 1000;
        IntStream.range(1, requireRankPoints.length).forEach(i -> {
            requireRankPoints[i] = (int) (requireRankPoints[i - 1] * 1.07);
        });
    }
}

