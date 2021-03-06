package com.example.filingo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Random;

public class TestResultFragment extends Fragment {

    public View rootView;
    private Context thiscontext;

    // Results screen elements
    private Button againButton;
    private Button exitTestButton;
    private TextView testProgressTextView;
    private ProgressBar testProgressBar;

    public static String topicName;
    public static Integer lives;
    public static Integer numberOfRightAnswers;
    public static Integer currentTestWordsSize;


    public static TestResultFragment newInstance(String topicName, Integer lives, Integer numberOfRightAnswers, Integer currentTestWordsSize) {
        TestResultFragment.topicName = topicName;
        TestResultFragment.lives = lives;
        TestResultFragment.numberOfRightAnswers = numberOfRightAnswers;
        TestResultFragment.currentTestWordsSize = currentTestWordsSize;
        TestResultFragment fragment = new TestResultFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.finish_test, container, false);
        thiscontext = container.getContext();
        setTestResultView();


        return rootView;
    }

    private void setTestResultView() {
        againButton = rootView.findViewById(R.id.finish_test_again_button);
        exitTestButton = rootView.findViewById(R.id.finish_test_exit_button);
        testProgressTextView = rootView.findViewById(R.id.finish_test_progress_text);
        testProgressBar = rootView.findViewById(R.id.finish_test_progressBar);


        againButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MainActivity.mediaPlayerArrayList.get(0).isPlaying() && !MainActivity.mediaPlayerArrayList.get(1).isPlaying() && !MainActivity.mediaPlayerArrayList.get(1).isPlaying())
                    MainActivity.getRandomMediaPlayer().start();
               ((MainActivity)getActivity()).displayTestFragment(topicName);
            }
        });

        exitTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MainActivity.mediaPlayerArrayList.get(0).isPlaying() && !MainActivity.mediaPlayerArrayList.get(1).isPlaying() && !MainActivity.mediaPlayerArrayList.get(1).isPlaying())
                    MainActivity.getRandomMediaPlayer().start();
               ((MainActivity)getActivity()).displayMainInfoFragment();
            }
        });


        if(lives<=0){
            MainActivity.falseSound.start();
            testProgressTextView.setText("Test has been FAILED\nYour Progress: " + numberOfRightAnswers+"/"+(currentTestWordsSize*3));
        }else{
            Random random = new Random();
            if(random.nextInt(100) < 90){
                Toast.makeText(getContext(), "You win bonus!", Toast.LENGTH_SHORT).show();
                TestFragment.numberOfBonuses ++;
                SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(MainActivity.SHARED_PREFS,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(MainActivity.BONUS_NUMBER,TestFragment.numberOfBonuses);
                editor.apply();
            }
            MainActivity.trueSound.start();
            testProgressTextView.setText("Test has been PASSED\nYour Progress: " + numberOfRightAnswers+"/"+(currentTestWordsSize*3));
        }

        double progress = numberOfRightAnswers/(currentTestWordsSize*3.);
        testProgressBar.setProgress((int)(100 * progress));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}