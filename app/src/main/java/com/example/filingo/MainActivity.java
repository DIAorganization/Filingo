package com.example.filingo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.filingo.database.TestRepository;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_FOR_GRAMMAR_TEST = "GRAMMAR";

    public StartFragment startFragment;
    public MainInfoFragment mainInfoFragment;
    public TestFragment testFragment;
    public TensesInfo tensesInfo;
    public TenseInfo tenseInfo;
    public TestResultFragment testResultFragment;

    public FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Filingo);
        TestRepository.initWordDatabase(getApplication());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkStartScreen();
    }

    public void checkStartScreen() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, 0);
        String ICN_URI = sharedPreferences.getString(StartFragment.ICN_URI,null);
        String USER_NAME = sharedPreferences.getString(StartFragment.USER_NAME,null);

        if (ICN_URI == null || USER_NAME == null) {
            displayStartFragment();
        } else {
            displayMainInfoFragment();
        }
    }


    public void displayStartFragment() {
        startFragment = StartFragment.newInstance();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.start_app_layout_fragment, startFragment).commit();
    }


    public void displayMainInfoFragment(){
        mainInfoFragment = MainInfoFragment.newInstance();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); // clear fragment stack
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(startFragment!= null)
            fragmentTransaction.replace(R.id.main_frame_info_fragment, mainInfoFragment).show(mainInfoFragment).hide(startFragment).commit();
        else if (testResultFragment!= null && !testResultFragment.isHidden())
            fragmentTransaction.replace(R.id.main_frame_info_fragment, mainInfoFragment).show(mainInfoFragment).hide(testResultFragment).commit();
        else
            fragmentTransaction.replace(R.id.main_frame_info_fragment, mainInfoFragment).show(mainInfoFragment).commit();

    }


    public void displayTestFragment(String topicName){
        displayMainInfoFragment();
        testFragment = TestFragment.newInstance(topicName);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(!mainInfoFragment.isHidden())
            fragmentTransaction.replace(R.id.main_test_fragment, testFragment).show(testFragment).hide(mainInfoFragment).addToBackStack(null).commit();
        else if(testResultFragment!= null && !testResultFragment.isHidden())
            fragmentTransaction.replace(R.id.main_test_fragment, testFragment).show(testFragment).hide(testResultFragment).addToBackStack(null).commit();
    }

    public void displayTensesInfo(){
        displayMainInfoFragment();
        tensesInfo = TensesInfo.newInstance();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_tenses_info, tensesInfo).hide(mainInfoFragment).addToBackStack(null).commit();
    }

    public void displayTenseInfo(int pos){
        displayMainInfoFragment();
        tenseInfo = TenseInfo.newInstance(pos);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_tense_info, tenseInfo).hide(tensesInfo).addToBackStack(null).commit();
    }

    public void displayTestResult(String topicName, Integer lives, Integer numberOfRightAnswers, Integer currentTestWordsSize) {
        displayMainInfoFragment();
        testResultFragment = TestResultFragment.newInstance(topicName, lives, numberOfRightAnswers, currentTestWordsSize);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_test_result, testResultFragment).hide(testFragment).commit();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}




