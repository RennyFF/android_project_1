package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface HistoryDAO {
    @Insert
    public void addHistory(History history);
    @Update
    public void updateHistory(History history);
    @Delete
    public void deleteHistory(History history);
    @Query("select * from HistoryDB")
    public List<History> getAllHistory();
    @Query("select * from HistoryDB where id==:id")
    public History getHistory(int id);
}
