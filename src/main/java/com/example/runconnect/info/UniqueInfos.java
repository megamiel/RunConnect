package com.example.runconnect.info;

import com.example.runconnect.library.DisplayController;
import com.example.runconnect.library.FirebaseDatabase;
import com.example.runconnect.library.Pointer;
import com.example.runconnect.message.Message;
import com.example.runconnect.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UniqueInfos {
    public static String id;
    public static Pointer<Boolean> isLoading = new Pointer<>(true);
    public static Pointer<Boolean> isRunning = new Pointer<>(true);
    public static DisplayController displayController;
    public static FirebaseDatabase db = new FirebaseDatabase();
    public static Random rnd = new Random();

    public static boolean isBooting;

    public static Map<String, List<Message>> youMessageMap = new HashMap<>();

    // ユーザのインスタンスを返す
    public static User getUser() {
        return SharedInfos.userMap.get(id);
    }

    public static void addYouMessage(Message message) {
        // メッセージ相手のidをkeyとしてメッセージをリストに追加していく
        User partner = message.getSendUser() == getUser() ? message.getReceiveUser() : message.getSendUser();

        if (!youMessageMap.containsKey(partner.getId())) {
            youMessageMap.put(partner.getId(), new ArrayList<>());
        }
        youMessageMap.get(partner.getId()).add(message);
    }

    public static List<Message> messageMapToList() {
        return youMessageMap.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }
}
