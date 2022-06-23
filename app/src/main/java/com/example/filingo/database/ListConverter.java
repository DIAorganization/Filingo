package com.example.filingo.database;

import androidx.room.TypeConverter;

import java.util.LinkedList;

class ListConverter {
    @TypeConverter
    public String fromLinkedList(LinkedList<String> list){
        StringBuilder builder = new StringBuilder();
        for(String s : list){
            builder.append(s);
            builder.append(';');
        }
        builder.deleteCharAt(builder.length()-1);
        return builder.toString();
    }

    @TypeConverter
    public LinkedList<String> toLinkedList(String str){
        LinkedList<String> result = new LinkedList<>();
        String[] sArr = str.split(";");
        for(String s : sArr){
            if(s != null && !s.isEmpty()){
                result.add(s);
            }
        }
        return result;
    }
}
