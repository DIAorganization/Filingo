package com.example.filingo.database;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicBoolean;


public abstract class TestRepository {
    private static WordDao dao;
    private static LiveData<List<Word>> allWords;
    public static LinkedList<Word> allWordsList = new LinkedList<>();
    final static Observer<List<Word>> observer = words -> {
        allWordsList.clear();
        allWordsList.addAll(words);
    };


    //Need application context from getApplicationContext() to init db
    public static void initWordDatabase(Application application){
        //db = Room.databaseBuilder(appContext, WordDatabase.class, "WordDatabase").createFromAsset("databases/myWord.db").build();
        //db = Room.databaseBuilder(appContext, WordDatabase.class, "WordDatabase").build();
        dao = WordDatabase.getDatabase(application).wordDao();
        allWords = dao.getAll();
        allWords.observeForever(observer);
    }



    @SuppressLint("NewApi")
    public static void testDb(){
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
        insert(testWord);
        testWord.id = 2;
        insert(testWord);
        testWord.id = 3;
        testWord.topic =2;
        insert(testWord);
        System.out.println(getWordsByTopic(1));
    }

    private static void insert(Word word){
        WordDatabase.databaseWriteExecutor.execute(() -> {dao.insert(word); System.out.println("done");});
    }

    //IDE порекомендувала так зробити бо PriorityQueue тільки з цієї версії api(24)
    @SuppressLint("NewApi")
    public static PriorityQueue<Word> getWordsByTopic(int topic){
        PriorityQueue<Word> result = new PriorityQueue<>(Word.memoryFactorComparator);
        if(allWordsList.isEmpty()) return result;
        for(Word w : allWordsList){
            if(w.topic == topic)
                result.add(w);
        }
        return result;
    }


    //true if something updated. false if nothing changed
    public static boolean updateWord(Word word){
        AtomicBoolean result = new AtomicBoolean(false);
        WordDatabase.databaseWriteExecutor.execute(() -> result.set(dao.update(word) == 0));
        return result.get();
    }


}
