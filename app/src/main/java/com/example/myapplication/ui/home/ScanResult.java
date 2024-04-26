package com.example.myapplication.ui.home;

public class ScanResult {
    public static String TYPE = "unknown";
    public static String DATE = "01/01/1990, 00:00";
    public static String RESULT_TEXT = "";

    ScanResult(String TYPE, String DATE, String RESULT_TEXT){
        this.TYPE = TYPE;
        this.DATE = DATE;
        this.RESULT_TEXT = RESULT_TEXT;
    }
}
