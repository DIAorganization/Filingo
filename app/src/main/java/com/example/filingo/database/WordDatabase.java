package com.example.filingo.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@TypeConverters({ListConverter.class})
@Database(entities = {Word.class},  version = 1, exportSchema = false)
abstract class WordDatabase extends RoomDatabase {
    public abstract WordDao wordDao();

    private static volatile WordDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static WordDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WordDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    WordDatabase.class, "word_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
