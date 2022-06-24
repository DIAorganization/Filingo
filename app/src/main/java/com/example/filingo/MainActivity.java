package com.example.filingo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.filingo.adapters.*;
import com.example.filingo.database.Word;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements LetterAdapter.OnLetterClicked , TopicAdapter.OnTopicClicked , TenseAdapter.OnTenseClicked {

    private static final int POINTS_FOR_TESTING = 5; // every word in test have this
    private static final int POINTS_FOR_RIGHT_ANSWER = 5; // for each right answer
    private static final int POINTS_FOR_SUCCESSFUL_TEST = 5; // up to 2 life losing test
    private static final int POINTS_FOR_PERFECT_TEST = 5; // no life losing test


    private String currentTestTopicName = "";
    private static int numberOfTestToEndTesting = 0; // need to count number of test in testing
    private static int numberOfRightAnswers = 0;  // need to count right answers in testing
    private static int chosenAnswer = -1; // to track chosen answer in test
    private static String wordChosenByLetters = "";
    private static final int START_LIVES = 3;
    private static int lives = START_LIVES; // number of lives(hears) player currently have
    private static ArrayList<Word> allTopicWords; // we need all words from topic to get random answer options
    private static ArrayList<Word> currentTestWords = new ArrayList<>(); // words(up to 4) that are in the test now

    private static ArrayList<Integer> testKeys; // needed for test generation in random order


    private static ArrayList<Word> allTopicWordsFakeDBData; // delete after DB will be full working

    TextView testTopicName; // topic_name;
    ImageView wordImg; // word_img;
    TextView wordValueOnChooseScreen; // choose_word_value;
    TextView wordTranslateOnChooseScreen; // choose_word_translate;
    Button knowButton; // choose_know_button;
    Button learnButton; // choose_learn_button;

    ImageView heartFirst; // heart_1;
    ImageView heartSecond; // heart_2;
    ImageView heartThird; // heart_3;
    Button nextButton; // next_button;
    ImageView wordAudioImgButton; // word_audio_img_button; add listener to interact
    RecyclerView letterRecycler; // letter_chooser;
    TextView wordTranslateOnTestScreen; // test_word_translate;

    Button answerButtonBottomFirst; // answer_button_bottom_1;
    Button answerButtonBottomSecond; // answer_button_bottom_2;
    Button answerButtonBottomThird; // answer_button_bottom_3;
    Button answerButtonBottomFourth; // answer_button_bottom_4;

    Button answerButtonTopFirst; // answer_button_top_1;
    Button answerButtonTopSecond; // answer_button_top_2;
    Button answerButtonTopThird; // answer_button_top_3;
    Button answerButtonTopFourth; // answer_button_top_4;
    TextView wordValueOnTestScreen; // test_word_value;


    RecyclerView topicRecycler; // topic_chooser;
    RecyclerView tensesRecycler; // tenses_chooser;

    LetterAdapter letterAdapter;
    TopicAdapter topicAdapter;
    TenseAdapter tenseAdapter;

    // Results screen elements
    Button againButton;
    Button exitTestButton;
    TextView testProgressTextView;
    ProgressBar testProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Filingo);
        super.onCreate(savedInstanceState);

        //setTestAnimationWorking();
        //setTensesChoseView();

        generateFakeDBTopicWords(40);
        setTopicChoseView();
    }

    private void resetLives() {
        lives = START_LIVES;
        heartFirst.setVisibility(View.VISIBLE);
        heartSecond.setVisibility(View.VISIBLE);
        heartThird.setVisibility(View.VISIBLE);
    }

    private void loseLife() {
        Log.d("TAG", "Wrong answer. - heart");
        lives--;
        if(lives==2) heartThird.setVisibility(View.GONE);
        if(lives==1) heartSecond.setVisibility(View.GONE);
    }

    private void setLetterChooser(String word) {
        ArrayList<Character> listOfLetters = new ArrayList<>();
        for(int i=0; i<word.length(); i++) {
            listOfLetters.add(word.charAt(i));
        }
        Collections.shuffle(listOfLetters); // set random order

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        letterRecycler.setLayoutManager(layoutManager);
        letterAdapter = new LetterAdapter(this, listOfLetters, MainActivity.this);
        letterRecycler.setAdapter(letterAdapter);
        AudioTestFrameEnEn();
    }

    private void setChosenAnswer(int answer) {
        chosenAnswer=answer;
        // reset buttons colors
        answerButtonTopFirst.setBackground(getDrawable(R.drawable.test_button_background));
        answerButtonBottomFirst.setBackground(getDrawable(R.drawable.test_button_background));
        answerButtonTopSecond.setBackground(getDrawable(R.drawable.test_button_background));
        answerButtonBottomSecond.setBackground(getDrawable(R.drawable.test_button_background));
        answerButtonTopThird.setBackground(getDrawable(R.drawable.test_button_background));
        answerButtonBottomThird.setBackground(getDrawable(R.drawable.test_button_background));
        answerButtonTopFourth.setBackground(getDrawable(R.drawable.test_button_background));
        answerButtonBottomFourth.setBackground(getDrawable(R.drawable.test_button_background));

        switch(chosenAnswer) {
            case 0:
                answerButtonTopFirst.setBackgroundColor(getResources().getColor(R.color.light_gray));
                answerButtonBottomFirst.setBackgroundColor(getResources().getColor(R.color.light_gray));
                break;
            case 1:
                answerButtonTopSecond.setBackgroundColor(getResources().getColor(R.color.light_gray));
                answerButtonBottomSecond.setBackgroundColor(getResources().getColor(R.color.light_gray));
                break;
            case 2:
                answerButtonTopThird.setBackgroundColor(getResources().getColor(R.color.light_gray));
                answerButtonBottomThird.setBackgroundColor(getResources().getColor(R.color.light_gray));
                break;
            case 3:
                answerButtonTopFourth.setBackgroundColor(getResources().getColor(R.color.light_gray));
                answerButtonBottomFourth.setBackgroundColor(getResources().getColor(R.color.light_gray));
                break;
            default:

        }
    }

    private void setTestAnimationWorking(){
        setContentView(R.layout.test_fragment);

        ShapeableImageView img = findViewById(R.id.word_img);
        Button nextButton = findViewById(R.id.next_button);

        nextButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                Animation hideImg = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.hide_img);
                Animation showImg = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_img);
/*
                if (img.getVisibility() == View.GONE) {
                    img.setVisibility(View.VISIBLE);
                }else{

                }
*/


                hideImg.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        img.setImageResource(R.drawable.hardware_icn);
                        img.startAnimation(showImg);
                    }

                });
                img.startAnimation(hideImg);
               // img.setVisibility(View.GONE);

               // img.setImageResource(R.drawable.hardware_icn);
            //    img.setVisibility(View.VISIBLE);
                //img.startAnimation(showImg);
            }
            });


    }

    private void setTestResultView() {
        setContentView(R.layout.finish_test);
        againButton=findViewById(R.id.finish_test_again_button);
        exitTestButton=findViewById(R.id.finish_test_exit_button);
        testProgressTextView=findViewById(R.id.finish_test_progress_text);
        testProgressBar=findViewById(R.id.finish_test_progressBar);

        againButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchTesting(currentTestTopicName);
            }
        });

        exitTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTopicChoseView();
            }
        });

        testProgressTextView.setText("Test has been PASSED\nYour Progress: " + numberOfRightAnswers+"/"+(currentTestWords.size()*3));
        if(lives<=0)
            testProgressTextView.setText("Test has been FAILED\nYour Progress: " + numberOfRightAnswers+"/"+(currentTestWords.size()*3));
        testProgressBar.setProgress(numberOfRightAnswers/(currentTestWords.size()*3));
    }

    private void setTensesChoseView() {
        setContentView(R.layout.tenses_info_layout);
        tensesRecycler = findViewById(R.id.tenses_chooser);

        List<String> listOfTenses = new ArrayList<>();
        listOfTenses.add("Present Simple");
        listOfTenses.add("Present Simple");
        listOfTenses.add("Present Simple");
        listOfTenses.add("Present Simple");
        listOfTenses.add("Present Simple");
        listOfTenses.add("Present Simple");
        listOfTenses.add("Present Simple");
        listOfTenses.add("Present Simple");
        listOfTenses.add("Present Simple");
        listOfTenses.add("Present Simple");
        listOfTenses.add("Present Simple");
        listOfTenses.add("Present Simple");




        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        tensesRecycler.setLayoutManager(layoutManager);
        tenseAdapter = new TenseAdapter(this, listOfTenses, MainActivity.this);
        tensesRecycler.setAdapter(tenseAdapter);
    }

    private void setTopicChoseView() {
        setContentView(R.layout.main_frame_info);
        topicRecycler = findViewById(R.id.topic_chooser);

        ArrayList<Topic> listOfTopics = new ArrayList<>();
        listOfTopics.add(new Topic(R.drawable.hardware_icn, "General"));
        listOfTopics.add(new Topic(R.drawable.hardware_icn, "Hardware"));
        listOfTopics.add(new Topic(R.drawable.hardware_icn, "Software"));
        listOfTopics.add(new Topic(R.drawable.hardware_icn, "Travelling"));
        listOfTopics.add(new Topic(R.drawable.hardware_icn, "Studying"));
        listOfTopics.add(new Topic(R.drawable.hardware_icn, "Business"));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        topicRecycler.setLayoutManager(layoutManager);
        topicAdapter = new TopicAdapter(this, listOfTopics, MainActivity.this);
        topicRecycler.setAdapter(topicAdapter);
    }

    private void setTestFragment() {
        setContentView(R.layout.test_fragment);
        testTopicName = findViewById(R.id.topic_name);
        wordImg = findViewById(R.id.word_img);
        wordValueOnChooseScreen = findViewById(R.id.choose_word_value);
        wordTranslateOnChooseScreen = findViewById(R.id.choose_word_translate);
        knowButton = findViewById(R.id.choose_know_button);
        learnButton = findViewById(R.id.choose_learn_button);
        heartFirst = findViewById(R.id.heart_1);
        heartSecond = findViewById(R.id.heart_2);
        heartThird = findViewById(R.id.heart_3);

        // Make hearts visible from start in every testing
        heartFirst.setVisibility(View.VISIBLE);
        heartSecond.setVisibility(View.VISIBLE);
        heartThird.setVisibility(View.VISIBLE);

        nextButton = findViewById(R.id.next_button);
        wordAudioImgButton = findViewById(R.id.word_audio_img_button);
        letterRecycler = findViewById(R.id.letter_chooser);
        wordTranslateOnTestScreen = findViewById(R.id.test_word_translate);
        answerButtonBottomFirst = findViewById(R.id.answer_button_bottom_1);
        answerButtonBottomSecond = findViewById(R.id.answer_button_bottom_2);
        answerButtonBottomThird = findViewById(R.id.answer_button_bottom_3);
        answerButtonBottomFourth = findViewById(R.id.answer_button_bottom_4);
        answerButtonTopFirst = findViewById(R.id.answer_button_top_1);
        answerButtonTopSecond = findViewById(R.id.answer_button_top_2);
        answerButtonTopThird = findViewById(R.id.answer_button_top_3);
        answerButtonTopFourth = findViewById(R.id.answer_button_top_4);
        wordValueOnTestScreen = findViewById(R.id.test_word_value);
    }

    public void ChooseWord(){
        wordImg.setVisibility(View.VISIBLE);
        wordValueOnChooseScreen.setVisibility(View.VISIBLE);
        wordTranslateOnChooseScreen.setVisibility(View.VISIBLE);
        knowButton.setVisibility(View.VISIBLE);
        learnButton.setVisibility(View.VISIBLE);
        heartFirst.setVisibility(View.GONE);
        heartSecond.setVisibility(View.GONE);
        heartThird.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        wordAudioImgButton.setVisibility(View.GONE);
        letterRecycler.setVisibility(View.GONE);
        wordTranslateOnTestScreen.setVisibility(View.GONE);
        answerButtonBottomFirst.setVisibility(View.GONE);
        answerButtonBottomSecond.setVisibility(View.GONE);
        answerButtonBottomThird.setVisibility(View.GONE);
        answerButtonBottomFourth.setVisibility(View.GONE);
        answerButtonTopFirst.setVisibility(View.GONE);
        answerButtonTopSecond.setVisibility(View.GONE);
        answerButtonTopThird.setVisibility(View.GONE);
        answerButtonTopFourth.setVisibility(View.GONE);
        wordValueOnTestScreen.setVisibility(View.GONE);
    }

    public void AudioTestFrameEnEn(){
        wordImg.setVisibility(View.VISIBLE);
        wordValueOnChooseScreen.setVisibility(View.GONE);
        wordTranslateOnChooseScreen.setVisibility(View.GONE);
        knowButton.setVisibility(View.GONE);
        learnButton.setVisibility(View.GONE);
        // Don't uncomment. With this lives reset after every test part
        //heartFirst.setVisibility(View.VISIBLE);
        //heartSecond.setVisibility(View.VISIBLE);
        //heartThird.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);
        wordAudioImgButton.setVisibility(View.VISIBLE);
        letterRecycler.setVisibility(View.VISIBLE);
        wordTranslateOnTestScreen.setVisibility(View.GONE);
        answerButtonBottomFirst.setVisibility(View.GONE);
        answerButtonBottomSecond.setVisibility(View.GONE);
        answerButtonBottomThird.setVisibility(View.GONE);
        answerButtonBottomFourth.setVisibility(View.GONE);
        answerButtonTopFirst.setVisibility(View.GONE);
        answerButtonTopSecond.setVisibility(View.GONE);
        answerButtonTopThird.setVisibility(View.GONE);
        answerButtonTopFourth.setVisibility(View.GONE);
        wordValueOnTestScreen.setVisibility(View.GONE);
    }


    public void TranslateTestFrameUaEn(){
        wordImg.setVisibility(View.VISIBLE);
        wordValueOnChooseScreen.setVisibility(View.GONE);
        wordTranslateOnChooseScreen.setVisibility(View.GONE);
        knowButton.setVisibility(View.GONE);
        learnButton.setVisibility(View.GONE);
        // Don't uncomment. With this lives reset after every test part
        //heartFirst.setVisibility(View.VISIBLE);
        //heartSecond.setVisibility(View.VISIBLE);
        //heartThird.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);
        wordAudioImgButton.setVisibility(View.GONE);
        letterRecycler.setVisibility(View.GONE);
        wordTranslateOnTestScreen.setVisibility(View.VISIBLE);
        answerButtonBottomFirst.setVisibility(View.VISIBLE);
        answerButtonBottomSecond.setVisibility(View.VISIBLE);
        answerButtonBottomThird.setVisibility(View.VISIBLE);
        answerButtonBottomFourth.setVisibility(View.VISIBLE);
        answerButtonTopFirst.setVisibility(View.GONE);
        answerButtonTopSecond.setVisibility(View.GONE);
        answerButtonTopThird.setVisibility(View.GONE);
        answerButtonTopFourth.setVisibility(View.GONE);
        wordValueOnTestScreen.setVisibility(View.GONE);
    }

    public void TranslateTestFrameEnUa(){
        wordImg.setVisibility(View.GONE);
        wordValueOnChooseScreen.setVisibility(View.GONE);
        wordTranslateOnChooseScreen.setVisibility(View.GONE);
        knowButton.setVisibility(View.GONE);
        learnButton.setVisibility(View.GONE);
        // Don't uncomment. With this lives reset after every test part
        //heartFirst.setVisibility(View.VISIBLE);
        //heartSecond.setVisibility(View.VISIBLE);
        //heartThird.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);
        wordAudioImgButton.setVisibility(View.VISIBLE);
        letterRecycler.setVisibility(View.GONE);
        wordTranslateOnTestScreen.setVisibility(View.GONE);
        answerButtonBottomFirst.setVisibility(View.GONE);
        answerButtonBottomSecond.setVisibility(View.GONE);
        answerButtonBottomThird.setVisibility(View.GONE);
        answerButtonBottomFourth.setVisibility(View.GONE);
        answerButtonTopFirst.setVisibility(View.VISIBLE);
        answerButtonTopSecond.setVisibility(View.VISIBLE);
        answerButtonTopThird.setVisibility(View.VISIBLE);
        answerButtonTopFourth.setVisibility(View.VISIBLE);
        wordValueOnTestScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void OnTenseClicked(int pos) {

    }

    @Override
    public void OnLetterClicked(int pos) {
        if(!letterAdapter.lettersAreChosen.get(pos)) { // we can't add same letter twice
            wordChosenByLetters+=letterAdapter.letterList.get(pos);
            letterAdapter.lettersAreChosen.set(pos, true);
        }
    }

    @Override
    public void OnTopicClicked(Topic topic) {
        launchTesting(topic.topicName); // Start tests
    }

    private void launchTesting(String testingTopic) {
        currentTestTopicName=testingTopic;
        setTestFragment();
        testTopicName.setText(testingTopic);
        currentTestWords.clear();
        ChooseWord();
        launchWordsForLearningDemonstration(testingTopic);
    }

    private void generateFakeDBTopicWords(int numberOfWords) {
        allTopicWordsFakeDBData = new ArrayList<>();
        for(int i=0; i<numberOfWords; i++) {
            Word word = new Word(); word.id = i; word.english = "word"+i;
            {
                LinkedList<String> temp = new LinkedList<>();
                temp.add("переклад"+i);  temp.add("переклад"+i+"-"+1);
                temp.add("переклад"+i+"-"+2); temp.add("переклад"+i+"-"+3);
                word.ukrainian = temp;
            }
            word.audioUrl = null; word.imageUrl = null; word.topic = 1; word.memoryFactor = 0;
            allTopicWordsFakeDBData.add(word);
        }
    }

    private void launchWordsForLearningDemonstration(String topicName) {

        // When DB will be ready replace allTopicWords getting by method that get all words(sorted with memoryFactor) from topic
        allTopicWords = new ArrayList<>(allTopicWordsFakeDBData); // create by copying, need them to get random answer options

        ArrayList<Word> filteredWords = new ArrayList<>(allTopicWords);

        // Remove words with max memory factor
        // Remove words that wad previously added to current learning/test list
        Iterator<Word> itr = filteredWords.iterator();
        while (itr.hasNext()) {
            Word word = itr.next();
            if (word.memoryFactor>=100 || currentTestWords.contains(word)) {
                itr.remove();
            }
        }


        // Get first 20 (by memoryFactor)
        ArrayList<Word> demonstrationWords = new ArrayList<>();
        for(int i=0; i < 20 && i < filteredWords.size(); i++) {
            demonstrationWords.add(filteredWords.get(i)); // add up to 20 words
        }
        if(demonstrationWords.size()==0) {
            if(currentTestWords.size()>0) { // we can't have 4 word, we need do test with what we can
                startTesting();
            } else {
                Log.d("TAG", "No word to demonstrate. You learn every word");
                setTopicChoseView();
            }
            return;
        }
        Collections.shuffle(demonstrationWords); // random demonstration order
        wordValueOnChooseScreen.setText(demonstrationWords.get(0).english);
        wordTranslateOnChooseScreen.setText(demonstrationWords.get(0).ukrainian.get(0));

        knowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                demonstrationWords.get(0).memoryFactor+=50;
                demonstrationWords.remove(0);
                if(demonstrationWords.size()>0) {
                    wordValueOnChooseScreen.setText(demonstrationWords.get(0).english);
                    wordTranslateOnChooseScreen.setText(demonstrationWords.get(0).ukrainian.get(0));
                } else {
                    Log.d("TAG", "All words have been demonstrated. Start new cycle");
                    launchWordsForLearningDemonstration(topicName);
                }
            }
        });

        learnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTestWords.add(demonstrationWords.get(0));
                demonstrationWords.remove(0);
                if(currentTestWords.size()>3) {
                    startTesting();
                } else if(demonstrationWords.size()>0) {
                    wordValueOnChooseScreen.setText(demonstrationWords.get(0).english);
                    wordTranslateOnChooseScreen.setText(demonstrationWords.get(0).ukrainian.get(0));
                } else {
                    launchWordsForLearningDemonstration(topicName);
                }
            }
        });
    }

    private void startTesting() {
        for(Word word : currentTestWords) {
            Log.d("TAG", "You will learn: "+word.english);
            word.memoryFactor+=POINTS_FOR_TESTING;
        }
        numberOfTestToEndTesting=currentTestWords.size()*3; // 3 test for each word
        testKeys = new ArrayList<>();
        for(int i=0; i<numberOfTestToEndTesting; i++) testKeys.add(i);
        Collections.shuffle(testKeys);
        numberOfRightAnswers = 0;
        launchTest();
        resetLives();
    }

    private void launchTest() {
        numberOfTestToEndTesting--;
        if(numberOfTestToEndTesting < 0) return; // stop if we finish all tests
        int testKey = testKeys.get(numberOfTestToEndTesting);

        Log.d("TAG", "Test "+(currentTestWords.size()*3-numberOfTestToEndTesting)+"/"+(currentTestWords.size()*3));
        Word currentWord = currentTestWords.get(testKey/4);

        // Additional words for testing options(till we have full database)
        ArrayList<Word> testOptions = new ArrayList<>();
        testOptions.add(currentWord);

        for(int i=0; i<3; i++) {  // Add incorrect options
            Word optionWord = allTopicWords.get((new Random()).nextInt(allTopicWords.size()));
            for(int j=0; j<testOptions.size(); j++) {
                if(optionWord.id == testOptions.get(j).id) {
                    optionWord = allTopicWords.get((new Random()).nextInt(allTopicWords.size()));
                    j=-1; // start loop again
                    continue;
                }
            }
            testOptions.add(optionWord);
        }
        Collections.shuffle(testOptions);



        int testType = testKey%3; // test type
        setChosenAnswer(-1);
        wordChosenByLetters = "";

        switch (testType) {
            case 0:
                AudioTestFrameEnEn();
                setLetterChooser(currentWord.english);
                break;
            case 1:
                TranslateTestFrameUaEn();
                wordTranslateOnTestScreen.setText(currentWord.ukrainian.get(0));
                answerButtonBottomFirst.setText(testOptions.get(0).english);
                answerButtonBottomFirst.setOnClickListener(x -> {setChosenAnswer(0);});
                answerButtonBottomSecond.setText(testOptions.get(1).english);
                answerButtonBottomSecond.setOnClickListener(x -> setChosenAnswer(1));
                answerButtonBottomThird.setText(testOptions.get(2).english);
                answerButtonBottomThird.setOnClickListener(x -> setChosenAnswer(2));
                answerButtonBottomFourth.setText(testOptions.get(3).english);
                answerButtonBottomFourth.setOnClickListener(x -> setChosenAnswer(3));
                break;
            case 2:
                TranslateTestFrameEnUa();
                wordValueOnTestScreen.setText(currentWord.english);
                answerButtonTopFirst.setText(testOptions.get(0).ukrainian.get(0));
                answerButtonTopFirst.setOnClickListener(x -> setChosenAnswer(0));
                answerButtonTopSecond.setText(testOptions.get(1).ukrainian.get(0));
                answerButtonTopSecond.setOnClickListener(x -> setChosenAnswer(1));
                answerButtonTopThird.setText(testOptions.get(2).ukrainian.get(0));
                answerButtonTopThird.setOnClickListener(x -> setChosenAnswer(2));
                answerButtonTopFourth.setText(testOptions.get(3).ukrainian.get(0));
                answerButtonTopFourth.setOnClickListener(x -> setChosenAnswer(3));
                break;
            default:
                //
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answerText = "";
                switch (testType) {
                    case 0:
                        Log.d("TAG", "Word Selected By Letters: "+wordChosenByLetters);
                        if(currentWord.english.equals(wordChosenByLetters)) {
                            numberOfRightAnswers++;
                            currentWord.memoryFactor+=POINTS_FOR_RIGHT_ANSWER;
                        } else {
                            loseLife();
                            if(lives==0) {
                                setTestResultView(); // test failed, back to menu
                            }
                        }
                        break;
                    case 1:
                        switch (chosenAnswer) {
                            case 0:
                                answerText = answerButtonBottomFirst.getText().toString();
                                break;
                            case 1:
                                answerText = answerButtonBottomSecond.getText().toString();
                                break;
                            case 2:
                                answerText = answerButtonBottomThird.getText().toString();
                                break;
                            case 3:
                                answerText = answerButtonBottomFourth.getText().toString();
                                break;
                        }
                        if(answerText.equals(currentWord.english)) {
                            numberOfRightAnswers++;
                            currentWord.memoryFactor+=POINTS_FOR_RIGHT_ANSWER;
                        } else {
                            loseLife();
                            if(lives==0) {
                                setTestResultView(); // test failed, back to menu
                            }                        }
                        break;
                    case 2:
                        switch (chosenAnswer) {
                            case 0:
                                answerText = answerButtonTopFirst.getText().toString();
                                break;
                            case 1:
                                answerText = answerButtonTopSecond.getText().toString();
                                break;
                            case 2:
                                answerText = answerButtonTopThird.getText().toString();
                                break;
                            case 3:
                                answerText = answerButtonTopFourth.getText().toString();
                                break;
                        }
                        for(int i=0; i < currentWord.ukrainian.size(); i++) {
                            if(currentWord.ukrainian.get(i).equals(answerText)) {
                                numberOfRightAnswers++;
                                currentWord.memoryFactor+=POINTS_FOR_RIGHT_ANSWER;
                                break;
                            } else if(i==currentWord.ukrainian.size()-1) { // wrong answer
                                loseLife();
                                if(lives==0) {
                                    setTestResultView(); // test failed, back to menu
                                }                            }
                        }
                        break;
                    default:
                        //
                }
                if(numberOfTestToEndTesting > 0) {
                    launchTest();
                } else {
                    if(lives>0) {
                        for(Word word: currentTestWords)
                            word.memoryFactor+=POINTS_FOR_SUCCESSFUL_TEST;
                    }
                    if(lives>2) {
                        for(Word word: currentTestWords)
                            word.memoryFactor+=POINTS_FOR_PERFECT_TEST;
                    }
                    Log.d("TAG", "Right Answers: "+numberOfRightAnswers);
                    setTestResultView();
                }
            }
        });
    }
}




