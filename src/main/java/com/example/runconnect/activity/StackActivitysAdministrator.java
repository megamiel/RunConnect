package com.example.runconnect.activity;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class StackActivitysAdministrator {
    public static List<AppCompatActivity> parentActivityList = new ArrayList<>();
    public static AppCompatActivity thisActivity=null;
    public static void finishAllSubActivity(){
        if (!parentActivityList.isEmpty()) {
            AppCompatActivity parent =parentActivityList.get(parentActivityList.size() - 1);
            while (parent instanceof PostResponseSubActivity) {
                parent.finish();
                parent = parentActivityList.remove(parentActivityList.size() - 1);
            }
        }
        if(thisActivity instanceof PostResponseSubActivity){
            thisActivity.finish();
        }
    }
}
