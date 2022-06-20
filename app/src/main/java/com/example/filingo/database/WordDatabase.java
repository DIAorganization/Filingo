package com.example.filingo.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@TypeConverters({ListConverter.class})
@Database(entities = {Word.class}, views = {DatabaseWordView.class},  version = 1)
abstract class WordDatabase extends RoomDatabase {
    public abstract WordDao wordDao();

}
