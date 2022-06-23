package com.example.filingo.adapters;

public class Topic{

    public Topic(Integer topicIcnUrl , String topicName){
        this.topicIcnUrl = topicIcnUrl;
        this.topicName = topicName;
    }

    public Integer topicIcnUrl;
    public String topicName;
    public Double topicProgress = 0.;//???
}