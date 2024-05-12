package com.example.myapplication;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "GenerateHistoryDB")
public class GenerateHistory {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "text")
    String text;
    @ColumnInfo(name = "type")
    String type;
    @Ignore
    public GenerateHistory() {
    }


    public String getText() {
        return text;
    }

    public void getText(String result_text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public GenerateHistory(String text, String type) {
        this.text = text;
        this.type = type;
    }
}
