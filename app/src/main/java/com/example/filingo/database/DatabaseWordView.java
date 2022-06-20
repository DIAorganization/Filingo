package com.example.filingo.database;

import androidx.room.DatabaseView;
import androidx.room.TypeConverters;

import java.util.List;


//not sure how it works and will be used yet
@DatabaseView("SELECT word.id, word.english, word.ukrainian, word.audio, word.image, word.topic, word.memoryFactor FROM word ")
public class DatabaseWordView {
    public int id;
    public String english;
    @TypeConverters({ListConverter.class})
    public List<String> ukrainian;
    public String audioUrl;
    public String imageUrl;
    public int topic;
    public double memoryFactor;
}
