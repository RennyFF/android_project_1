package com.example.myapplication;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {GenerateHistory.class}, version = 1)
public abstract class GenerateHistoryDB extends RoomDatabase {
    public abstract GenerateHistoryDAO getGenerateHistoryDAO();
}
