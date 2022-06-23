package com.example.filingo.database;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Comparator;
import java.util.LinkedList;

@Entity(tableName = "wordTable")
public
class Word {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "english")
    @NonNull
    public String english;

    @ColumnInfo(name = "ukrainian")
    @NonNull
    public LinkedList<String> ukrainian;

    @ColumnInfo(name = "audio")
    public String audioUrl;

    @ColumnInfo(name = "image")
    public String imageUrl;

    @ColumnInfo(name = "topic")
    public int topic;

    @ColumnInfo(name = "memoryFactor")
    public int memoryFactor;

    @Ignore
    public static Comparator<Word> memoryFactorComparator = (word, t1) -> Integer.compare(word.memoryFactor, t1.memoryFactor);

    @NonNull
    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", english='" + english + '\'' +
                ", ukrainian=" + ukrainian +
                ", audioUrl='" + audioUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", topic=" + topic +
                ", memoryFactor=" + memoryFactor +
                '}';
    }
}
