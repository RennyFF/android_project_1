package com.example.myapplication;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "HistoryDB")
public class History {
    @ColumnInfo(name = "id")
            @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "date_time")
    String date_time;
    @ColumnInfo(name = "result_text")
    String result_text;
    @ColumnInfo(name = "type")
    String type;
    @Ignore
    public History() {
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getResult_text() {
        return result_text;
    }

    public void getResult_text(String result_text) {
        this.result_text = result_text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public History(String date_time, String result_text, String type) {
        this.date_time = date_time;
        this.result_text = result_text;
        this.type = type;
    }
}
