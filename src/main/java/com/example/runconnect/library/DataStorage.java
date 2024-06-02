package com.example.runconnect.library;

import android.content.Context;
import android.preference.PreferenceManager;

public class DataStorage {
    public static void write(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).apply();
    }

    public static String read(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, null);
    }
}
