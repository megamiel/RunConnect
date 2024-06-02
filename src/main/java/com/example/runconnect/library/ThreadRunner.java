package com.example.runconnect.library;

public class ThreadRunner {
    public static void start(Runnable runnable){
        new Thread(runnable).start();
    }
}
