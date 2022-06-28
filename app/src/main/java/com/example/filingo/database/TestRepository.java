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

    public static void update(Observer lis , LifecycleOwner lf){
        allWords.observe(lf,lis);
    }


    /*Need application context from getApplicationContext() to init db.
    Has to be initialized BEFORE STARTED state of app to work correctly.
    so use it in onCreate or onStart.
     */
    public static void initWordDatabase(Application application){
        dao = WordDatabase.getDatabase(application).wordDao();
        allWords = dao.getAll();
        allWords.observeForever(observer);

    }

    private static void insert(Word word){
        WordDatabase.databaseWriteExecutor.execute(() -> dao.insert(word));
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
