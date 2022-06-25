package com.example.filingo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_FOR_GRAMMAR_TEST = "GRAMMAR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Filingo);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayStartFragment();
    }


    private void displayStartFragment() {
        StartFragment startFragment = StartFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.start_app_layout_fragment, startFragment).commit();
    }

    public void displayMainInfoFragment(){
        MainInfoFragment mainInfoFragment = MainInfoFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_frame_info_fragment, mainInfoFragment).addToBackStack(null).commit();
    }


    public void displayTestFragment(String topicName){
        TestFragment testFragment = TestFragment.newInstance(topicName);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_test_fragment, testFragment).addToBackStack(null).commit();
    }

    public void displayTensesInfo(){
        TensesInfo tensesInfo = TensesInfo.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_tenses_info, tensesInfo).addToBackStack(null).commit();
    }

    public void displayTenseInfo(int pos){
        TenseInfo tenseInfo = TenseInfo.newInstance(pos);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_tense_info, tenseInfo).addToBackStack(null).commit();
    }

    public void displayTestResult(String topicName, Integer lives, Integer numberOfRightAnswers, Integer currentTestWordsSize){
        TestResultFragment testResultFragment = TestResultFragment.newInstance(topicName,lives,numberOfRightAnswers, currentTestWordsSize);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_test_result, testResultFragment).addToBackStack(null).commit();
    }

}




