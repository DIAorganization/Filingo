package com.example.filingo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filingo.adapters.Topic;
import com.example.filingo.adapters.TopicAdapter;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Random;

public class MainInfoFragment extends Fragment implements TopicAdapter.OnTopicClicked {

    public View rootView;
    private Context thiscontext;
    private RecyclerView topicRecycler; // topic_chooser;
    private TopicAdapter topicAdapter; // tenses_chooser;

    private  ArrayList<Topic> listOfTopics = new ArrayList<>();

    AppCompatButton learnButton;
    AppCompatButton grammarButton;
    AppCompatButton randomButton;

    public static MainInfoFragment newInstance() {
        MainInfoFragment fragment = new MainInfoFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.main_frame_info, container, false);
        thiscontext = container.getContext();
        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(MainActivity.SHARED_PREFS,Context.MODE_PRIVATE);

        String ICN_URI = sharedPreferences.getString(StartFragment.ICN_URI,"");
        String USER_NAME = sharedPreferences.getString(StartFragment.USER_NAME,"");

        ShapeableImageView userIcn = rootView.findViewById(R.id.user_icn);
        TextView textView = rootView.findViewById(R.id.user_greetings);

        if(ICN_URI!= null && !ICN_URI.equals(""))
            userIcn.setImageURI(Uri.parse(ICN_URI));
        textView.setText("Hi " + USER_NAME + ", are you ready to know more ?");


        topicRecycler = rootView.findViewById(R.id.topic_chooser);
        //set default topics names and icons

        listOfTopics.add(new Topic(R.drawable.hardware_icn, "General"));
        listOfTopics.add(new Topic(R.drawable.hardware_icn, "Hardware"));
        listOfTopics.add(new Topic(R.drawable.hardware_icn, "Software"));
        listOfTopics.add(new Topic(R.drawable.hardware_icn, "Travelling"));
        listOfTopics.add(new Topic(R.drawable.hardware_icn, "Studying"));
        listOfTopics.add(new Topic(R.drawable.hardware_icn, "Business"));

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
                ((MainActivity)getActivity()).displayTensesInfo();
            }
        });


        grammarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).displayTestFragment(MainActivity.KEY_FOR_GRAMMAR_TEST);
            }
        });


        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
                int index = random.nextInt(listOfTopics.size() + 1);
                ((MainActivity)getActivity()).displayTestFragment(listOfTopics.get(index).topicName);
            }
        });

        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void OnTopicClicked(Topic topic) {
        ((MainActivity)getActivity()).displayTestFragment(topic.topicName);
    }
}