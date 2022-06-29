package com.example.filingo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filingo.adapters.Topic;
import com.example.filingo.adapters.TopicAdapter;
import com.example.filingo.database.TestRepository;
import com.example.filingo.database.Word;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class MainInfoFragment extends Fragment implements TopicAdapter.OnTopicClicked {

    public View rootView;
    private Context thiscontext;
    private RecyclerView topicRecycler; // topic_chooser;
    private TopicAdapter topicAdapter; // tenses_chooser;

    public static String[] topicNames = {
            "Family",
            "Traits",
            "Food",
            "Body parts",
            "Accommodation",
            "Travelling"
    };

    private  ArrayList<Topic> listOfTopics = new ArrayList<>();

    AppCompatButton learnButton;
    AppCompatButton grammarButton;
    AppCompatButton randomButton;
    ProgressBar mainProgressBar;

    public static MainInfoFragment newInstance() {
        MainInfoFragment fragment = new MainInfoFragment();
        return fragment;
    }

    public final Observer<List<Word>> observer = words -> {
        UpdateUi();
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.main_frame_info, container, false);
        thiscontext = container.getContext();
        TestRepository.update(observer, this);
        return rootView;
    }

    public void UpdateUi(){

        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(MainActivity.SHARED_PREFS,Context.MODE_PRIVATE);

        String ICN_URI = sharedPreferences.getString(StartFragment.ICN_URI,"");
        String USER_NAME = sharedPreferences.getString(StartFragment.USER_NAME,"");

        ShapeableImageView userIcn = rootView.findViewById(R.id.user_icn);
        TextView textView = rootView.findViewById(R.id.user_greetings);

        if(ICN_URI!= null && !ICN_URI.equals(""))
            userIcn.setImageURI(Uri.parse(ICN_URI));
        textView.setText("Hi " + USER_NAME + ", are you ready to know more ?");

        topicRecycler = rootView.findViewById(R.id.topic_chooser);

        int allTopicsMemoryFactorSum=0;
        int numberOfAllWords=0;
        ArrayList<Integer> topicProgresses = new ArrayList<>();
        for(int i=0; i<topicNames.length; i++) {
            PriorityQueue<Word> allTopicWords = TestRepository.getWordsByTopic(i+1);
            int memoryFactorSum=0;
            for(Word w: allTopicWords) {
                memoryFactorSum += w.memoryFactor;
                allTopicsMemoryFactorSum += w.memoryFactor;
            }
            numberOfAllWords+=allTopicWords.size();
            double topicProgress = ((double) memoryFactorSum) / (allTopicWords.size()*100);
            topicProgress*=100;
            topicProgresses.add((int)Math.round(topicProgress));
        }
        double mainProgress = ((double) allTopicsMemoryFactorSum) / (numberOfAllWords*100);
        mainProgress*=100;
        mainProgressBar = rootView.findViewById(R.id.progressBar);
        mainProgressBar.setProgress((int)(Math.round(mainProgress)));

        //set default topics names and icons
        listOfTopics.add(new Topic(R.drawable.hardware_icn, topicNames[0], topicProgresses.get(0)));
        listOfTopics.add(new Topic(R.drawable.hardware_icn, topicNames[1], topicProgresses.get(1)));
        listOfTopics.add(new Topic(R.drawable.hardware_icn, topicNames[2], topicProgresses.get(2)));
        listOfTopics.add(new Topic(R.drawable.hardware_icn, topicNames[3], topicProgresses.get(3)));
        listOfTopics.add(new Topic(R.drawable.hardware_icn, topicNames[4], topicProgresses.get(4)));
        listOfTopics.add(new Topic(R.drawable.hardware_icn, topicNames[5], topicProgresses.get(5)));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(thiscontext, RecyclerView.VERTICAL, false);
        topicRecycler.setLayoutManager(layoutManager);
        topicAdapter = new TopicAdapter(thiscontext, listOfTopics, MainInfoFragment.this);
        topicRecycler.setAdapter(topicAdapter);


        learnButton = rootView.findViewById(R.id.main_frame_grammar_info_button);
        grammarButton = rootView.findViewById(R.id.main_frame_grammar_test_button);
        randomButton = rootView.findViewById(R.id.main_frame_random_test_button);

        learnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MainActivity.mediaPlayerArrayList.get(0).isPlaying() && !MainActivity.mediaPlayerArrayList.get(1).isPlaying() && !MainActivity.mediaPlayerArrayList.get(1).isPlaying())
                    MainActivity.getRandomMediaPlayer().start();
                ((MainActivity)getActivity()).displayTensesInfo();

            }
        });


        grammarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MainActivity.mediaPlayerArrayList.get(0).isPlaying() && !MainActivity.mediaPlayerArrayList.get(1).isPlaying() && !MainActivity.mediaPlayerArrayList.get(1).isPlaying())
                    MainActivity.getRandomMediaPlayer().start();
                ((MainActivity)getActivity()).displayTestFragment(MainActivity.KEY_FOR_GRAMMAR_TEST);

            }
        });


        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MainActivity.mediaPlayerArrayList.get(0).isPlaying() && !MainActivity.mediaPlayerArrayList.get(1).isPlaying() && !MainActivity.mediaPlayerArrayList.get(1).isPlaying())
                    MainActivity.getRandomMediaPlayer().start();
                Random random = new Random();
                int index = random.nextInt(listOfTopics.size());
                ((MainActivity)getActivity()).displayTestFragment(listOfTopics.get(index).topicName);

            }
        });


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void OnTopicClicked(Topic topic) {
        if(topic.topicProgress<100) {
            ((MainActivity)getActivity()).displayTestFragment(topic.topicName);
        } else {
            Toast.makeText(getContext(),"You have max progress in this topic", Toast.LENGTH_SHORT).show();
        }
    }
}