package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GenerateHistoryDAO {
    @Insert
    public void addGenerateHistory(GenerateHistory history);
    @Update
    public void updateGenerateHistory(GenerateHistory history);
    @Delete
    public void deleteGenerateHistory(GenerateHistory history);
    @Query("delete from GenerateHistoryDB where id == :id ")
    public void deleteById(int id);
    @Query("select * from GenerateHistoryDB")
    public List<GenerateHistory> getAllGenerateHistory();
    @Query("select * from GenerateHistoryDB order by id desc limit 1")
    public GenerateHistory getLastGHEntry();
}
