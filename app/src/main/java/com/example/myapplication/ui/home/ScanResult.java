package com.example.myapplication.ui.home;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.myapplication.History;

public class ScanResult implements Parcelable {
    public static int ID = 0;
    public static String TYPE = "unknown";
    public static String DATE = "01/01/1990, 00:00";
    public static String RESULT_TEXT = "";

    public ScanResult(String TYPE, String DATE, String RESULT_TEXT){
        this.TYPE = TYPE;
        this.DATE = DATE;
        this.RESULT_TEXT = RESULT_TEXT;
    }
    public ScanResult(int ID, String TYPE, String DATE, String RESULT_TEXT){
        this.ID = ID;
        this.TYPE = TYPE;
        this.DATE = DATE;
        this.RESULT_TEXT = RESULT_TEXT;
    }

    protected ScanResult(Parcel in) {
    }

    public static final Creator<ScanResult> CREATOR = new Creator<ScanResult>() {
        @Override
        public ScanResult createFromParcel(Parcel in) {
            return new ScanResult(in);
        }

        @Override
        public ScanResult[] newArray(int size) {
            return new ScanResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
    }
}
