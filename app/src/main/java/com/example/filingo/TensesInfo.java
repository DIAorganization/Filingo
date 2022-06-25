package com.example.filingo;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filingo.adapters.TenseAdapter;
import com.example.filingo.adapters.Topic;
import com.example.filingo.adapters.TopicAdapter;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TensesInfo extends Fragment implements TenseAdapter.OnTenseClicked{

    public View rootView;
    private Context thiscontext;
    private RecyclerView tensesRecycler;
    private TenseAdapter tenseAdapter;

    public static List<String> listOfTenses = new ArrayList<>();

    public static TensesInfo newInstance() {
        TensesInfo fragment = new TensesInfo();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tenses_info_layout, container, false);
        thiscontext = container.getContext();
        setTensesChoseView();
        return rootView;
    }

    private void setTensesChoseView() {
        tensesRecycler = rootView.findViewById(R.id.tenses_chooser);

        listOfTenses.add("Present Simple");
        listOfTenses.add("Present Continuous");
        listOfTenses.add("Present Perfect");
        listOfTenses.add("Present Perfect Continuous");
        listOfTenses.add("Past Simple");
        listOfTenses.add("Past Continuous");
        listOfTenses.add("Past Perfect");
        listOfTenses.add("Past Perfect Continuous");
        listOfTenses.add("Future Simple");
        listOfTenses.add("Future Continuous");
        listOfTenses.add("Future Perfect");
        listOfTenses.add("Future Perfect Continuous");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(thiscontext, RecyclerView.VERTICAL, false);
        tensesRecycler.setLayoutManager(layoutManager);
        tenseAdapter = new TenseAdapter(thiscontext, listOfTenses, TensesInfo.this);
        tensesRecycler.setAdapter(tenseAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void OnTenseClicked(int pos) {((MainActivity)getActivity()).displayTenseInfo(pos); }
}