package com.example.filingo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.filingo.database.TestRepository;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_FOR_GRAMMAR_TEST = "GRAMMAR";
    public static final String BONUS_NUMBER = "BONUS_NUMBER";


    public static  MediaPlayer trueSound;
    public static  MediaPlayer falseSound;
    public static ArrayList<MediaPlayer> mediaPlayerArrayList = new ArrayList<>();


    public StartFragment startFragment;
    public MainInfoFragment mainInfoFragment;
    public TestFragment testFragment;
    public TensesInfo tensesInfo;
    public TenseInfo tenseInfo;
    public TestResultFragment testResultFragment;


    public FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TestRepository.initWordDatabase(getApplication());
        setTheme(R.style.Theme_Filingo);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkStartScreen();
        setAudio();
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFS,Context.MODE_PRIVATE);
        TestFragment.numberOfBonuses = sharedPreferences.getInt(BONUS_NUMBER,1);

    }

    public void setAudio(){
        falseSound = MediaPlayer.create(this, R.raw.false_sound);
        trueSound = MediaPlayer.create(this, R.raw.true_sound);

        MediaPlayer press_button_1 = MediaPlayer.create(this, R.raw.press_button_1);
        MediaPlayer press_button_2 = MediaPlayer.create(this, R.raw.press_button_2);
        MediaPlayer press_button_3 = MediaPlayer.create(this, R.raw.press_button_3);
        mediaPlayerArrayList.add(press_button_1);
        mediaPlayerArrayList.add(press_button_2);
        mediaPlayerArrayList.add(press_button_3);
    }


    public static MediaPlayer getRandomMediaPlayer(){
        Random random = new Random();
        return mediaPlayerArrayList.get(random.nextInt(3));
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
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(startFragment!= null && !startFragment.isHidden()){
            fragmentTransaction.replace(R.id.main_frame_info_fragment, mainInfoFragment).show(mainInfoFragment).hide(startFragment).commit();
        }
        else if (testResultFragment!= null && !testResultFragment.isHidden())
            fragmentTransaction.replace(R.id.main_frame_info_fragment, mainInfoFragment).show(mainInfoFragment).hide(testResultFragment).commit();
        else
            fragmentTransaction.replace(R.id.main_frame_info_fragment, mainInfoFragment).show(mainInfoFragment).commit();

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        displayMainInfoFragment();
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
        tensesInfo = TensesInfo.newInstance();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_tenses_info, tensesInfo).hide(mainInfoFragment).addToBackStack(null).commit();
    }

    public void displayTenseInfo(int pos){
        tenseInfo = TenseInfo.newInstance(pos);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_tense_info, tenseInfo).hide(tensesInfo).hide(mainInfoFragment).addToBackStack(null).commit();
    }

    public void displayTestResult(String topicName, Integer lives, Integer numberOfRightAnswers, Integer currentTestWordsSize) {
        testResultFragment = TestResultFragment.newInstance(topicName, lives, numberOfRightAnswers, currentTestWordsSize);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_test_result, testResultFragment).hide(testFragment).commit();
    }
}





