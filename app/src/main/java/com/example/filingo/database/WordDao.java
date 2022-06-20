package com.example.filingo.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
interface WordDao {
    @Query("SELECT * FROM word")
    Single<List<Word>> getAll();

    @Query("SELECT * FROM word WHERE id IN (:wordIds)")
    Single<List<Word>> loadAllByIds(int[] wordIds);

    @Query("SELECT * FROM word WHERE english LIKE :eng LIMIT 1")
    Single<Word> findByName(String eng);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(Word... words);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Word word);

    @Delete
    Completable delete(Word word);
}
