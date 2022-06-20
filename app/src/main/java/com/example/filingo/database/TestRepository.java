package com.example.filingo.database;

import android.content.Context;

import androidx.room.Room;

import java.util.LinkedList;

public abstract class TestRepository {
    private static WordDatabase db;
    private static WordDao dao;

    //Need application context from getApplicationContext() to init db
    public static boolean initWordDatabase(Context appContext){
        //db = Room.databaseBuilder(appContext, WordDatabase.class, "WordDatabase").createFromAsset("databases/myWord.db").build();
        db = Room.databaseBuilder(appContext, WordDatabase.class, "WordDatabase").build();
        dao = db.wordDao();
        return true;
    }


    //doesn't work yet
    private static void testDb(){
        Word testWord = new Word();
        testWord.id = 1;
        testWord.english = "test";
        {
            LinkedList<String> temp = new LinkedList<>();
            temp.add("testUkr1");
            temp.add("testUkr2");
            testWord.ukrainian = temp;
        }
        testWord.audioUrl = null;
        testWord.imageUrl = null;
        testWord.topic = 1;
        testWord.memoryFactor = 0;
        dao.insert(testWord);
    }

}
