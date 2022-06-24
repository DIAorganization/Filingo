package com.example.filingo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;


@Dao
interface WordDao {
    @Query("SELECT * FROM wordTable")
    LiveData<List<Word>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Word word);

    @Update
    int update(Word word);


}
