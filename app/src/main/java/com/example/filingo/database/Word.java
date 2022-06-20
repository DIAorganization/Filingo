package com.example.filingo.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.LinkedList;

@Entity
class Word {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "english")
    public String english;

    @ColumnInfo(name = "ukrainian")
    public LinkedList<String> ukrainian;

    @ColumnInfo(name = "audio")
    public String audioUrl;

    @ColumnInfo(name = "image")
    public String imageUrl;

    @ColumnInfo(name = "topic")
    public int topic;

    @ColumnInfo(name = "memoryFactor")
    public double memoryFactor;
}
