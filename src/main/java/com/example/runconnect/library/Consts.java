package com.example.runconnect.library;

public class Consts {
    public static final int MINUTE = 60;
    public static final int HOUR = MINUTE * 60;
    public static final int DAY = HOUR * 24;
    public static final int MONTH = DAY * 30;
    public static final int YEAR = MONTH * 12;
    public static final String[] TIME_UNIT_ARRAY = {"年", "か月", "日", "時間", "分", "秒"};
    public static final int[] SECOND_ARRAY = {Consts.YEAR, Consts.MONTH, Consts.DAY, Consts.HOUR, Consts.MINUTE, 1};
}
