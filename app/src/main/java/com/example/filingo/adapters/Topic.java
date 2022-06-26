package com.example.filingo.adapters;

public class Topic{

    public Topic(Integer topicIcnUrl , String topicName, Integer topicProgress){
        this.topicIcnUrl = topicIcnUrl;
        this.topicName = topicName;
        this.topicProgress = topicProgress;
    }

    public Integer topicIcnUrl;
    public String topicName;
    public Integer topicProgress = 0;
}