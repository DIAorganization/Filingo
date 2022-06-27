package com.example.filingo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filingo.adapters.LetterAdapter;
import com.example.filingo.database.TestRepository;
import com.example.filingo.database.Word;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;

public class TestFragment extends Fragment implements LetterAdapter.OnLetterClicked{

    private static final int POINTS_FOR_TESTING = 5; // every word in test have this
    private static final int POINTS_FOR_RIGHT_ANSWER = 5; // for each right answer
    private static final int POINTS_FOR_SUCCESSFUL_TEST = 5; // up to 2 life losing test
    private static final int POINTS_FOR_PERFECT_TEST = 5; // no life losing test


    private static int numberOfTestToEndTesting = 0; // need to count number of test in testing
    private static int numberOfRightAnswers = 0;  // need to count right answers in testing
    private static int chosenAnswer; // to track chosen answer in test
    private static String wordChosenByLetters = "";
    private static final int START_LIVES = 3;
    private static int lives; // number of lives(hears) player currently have
    private static ArrayList<Word> allTopicWords; // we need all words from topic to get random answer options
    private static ArrayList<Word> currentTestWords = new ArrayList<>(); // words(up to 4) that are in the test now

    private static ArrayList<Integer> testKeys; // needed for test generation in random order
    private static boolean isDecisionMade; // to check if we go to the next test
    private static String currentWordEnglish;
    private static boolean isUnskippableAnimationRunning;


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

    AppCompatButton answerButtonBottomFirst; // answer_button_bottom_1;
    AppCompatButton answerButtonBottomSecond; // answer_button_bottom_2;
    AppCompatButton answerButtonBottomThird; // answer_button_bottom_3;
    AppCompatButton answerButtonBottomFourth; // answer_button_bottom_4;

    AppCompatButton answerButtonTopFirst; // answer_button_top_1;
    AppCompatButton answerButtonTopSecond; // answer_button_top_2;
    AppCompatButton answerButtonTopThird; // answer_button_top_3;
    AppCompatButton answerButtonTopFourth; // answer_button_top_4;
    TextView wordValueOnTestScreen; // test_word_value;

    LetterAdapter letterAdapter;

    ShapeableImageView bonusIcon;

    TextView numberOfTestView;


    public View rootView;
    private Context thiscontext;
    private static String topicName;


    public static TestFragment newInstance(String topicName) {
        TestFragment.topicName = topicName;
        TestFragment fragment = new TestFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.test_fragment, container, false);
        thiscontext = container.getContext();
        isUnskippableAnimationRunning=false;
        currentTestWords.clear();
        chosenAnswer = -1;
        currentWordEnglish="";
        isDecisionMade=false;
        lives = START_LIVES;
        testKeys=null;
        if(!topicName.equals(MainActivity.KEY_FOR_GRAMMAR_TEST)){
            launchTesting(topicName);
        }else{
            //If pressed GRAMMAR button use test for en ua to grammar;
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void resetLives() {
        lives = START_LIVES;
        heartFirst.setVisibility(View.VISIBLE);
        heartSecond.setVisibility(View.VISIBLE);
        heartThird.setVisibility(View.VISIBLE);
    }

    private void loseLife() {
        Toast.makeText(getContext(), "Your lose one life", Toast.LENGTH_LONG).show();
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

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(thiscontext, RecyclerView.HORIZONTAL, false);
        letterRecycler.setLayoutManager(layoutManager);
        letterAdapter = new LetterAdapter(thiscontext, listOfLetters, TestFragment.this);
        letterRecycler.setAdapter(letterAdapter);
        AudioTestFrameEnEn();
    }

    private void setChosenAnswer(int answer, boolean isCorrect) {
        if(isDecisionMade) {
            Toast.makeText(getContext(), "You can't change your answer", Toast.LENGTH_SHORT).show();
            return; // Can't reselect answer
        }

        // reset buttons colors
        answerButtonTopFirst.setBackground((Drawable) ((Context)(((MainActivity)getActivity()))).getResources().getDrawable(R.drawable.test_button_background));
        answerButtonBottomFirst.setBackground((Drawable) ((Context)(((MainActivity)getActivity()))).getResources().getDrawable(R.drawable.test_button_background));
        answerButtonTopSecond.setBackground((Drawable) ((Context)(((MainActivity)getActivity()))).getResources().getDrawable(R.drawable.test_button_background));
        answerButtonBottomSecond.setBackground((Drawable) ((Context)(((MainActivity)getActivity()))).getResources().getDrawable(R.drawable.test_button_background));
        answerButtonTopThird.setBackground((Drawable) ((Context)(((MainActivity)getActivity()))).getResources().getDrawable(R.drawable.test_button_background));
        answerButtonBottomThird.setBackground((Drawable) ((Context)(((MainActivity)getActivity()))).getResources().getDrawable(R.drawable.test_button_background));
        answerButtonTopFourth.setBackground((Drawable) ((Context)(((MainActivity)getActivity()))).getResources().getDrawable(R.drawable.test_button_background));
        answerButtonBottomFourth.setBackground((Drawable) ((Context)(((MainActivity)getActivity()))).getResources().getDrawable(R.drawable.test_button_background));

        if(answer>=0) {
            isDecisionMade=true;
        }
        chosenAnswer=answer;

        switch(chosenAnswer) {
            case 0:
                if(chosenAnswer>=0) {
                    if (isCorrect) {
                        answerButtonTopFirst.setBackground(getResources().getDrawable(R.drawable.right_test_button_background));
                        answerButtonBottomFirst.setBackground(getResources().getDrawable(R.drawable.right_test_button_background));
                    } else {
                        answerButtonTopFirst.setBackground(getResources().getDrawable(R.drawable.wrong_test_button_background));
                        answerButtonBottomFirst.setBackground(getResources().getDrawable(R.drawable.wrong_test_button_background));
                    }
                }
                break;
            case 1:
                if(chosenAnswer>=0) {
                    if (isCorrect) {
                        answerButtonTopSecond.setBackground(getResources().getDrawable(R.drawable.right_test_button_background));
                        answerButtonBottomSecond.setBackground(getResources().getDrawable(R.drawable.right_test_button_background));
                    } else {
                        answerButtonTopSecond.setBackground(getResources().getDrawable(R.drawable.wrong_test_button_background));
                        answerButtonBottomSecond.setBackground(getResources().getDrawable(R.drawable.wrong_test_button_background));
                    }
                }
                break;
            case 2:
                if(chosenAnswer>=0) {
                    if (isCorrect) {
                        answerButtonTopThird.setBackground(getResources().getDrawable(R.drawable.right_test_button_background));
                        answerButtonBottomThird.setBackground(getResources().getDrawable(R.drawable.right_test_button_background));
                    } else {
                        answerButtonTopThird.setBackground(getResources().getDrawable(R.drawable.wrong_test_button_background));
                        answerButtonBottomThird.setBackground(getResources().getDrawable(R.drawable.wrong_test_button_background));
                    }
                }
                break;
            case 3:
                if(chosenAnswer>=0) {
                    if (isCorrect) {
                        answerButtonTopFourth.setBackground(getResources().getDrawable(R.drawable.right_test_button_background));
                        answerButtonBottomFourth.setBackground(getResources().getDrawable(R.drawable.right_test_button_background));
                    } else {
                        answerButtonTopFourth.setBackground(getResources().getDrawable(R.drawable.wrong_test_button_background));
                        answerButtonBottomFourth.setBackground(getResources().getDrawable(R.drawable.wrong_test_button_background));
                    }
                }
                break;
            default:

        }
    }

    private void launchAnimation(boolean toLeft, boolean nextTestHasImage, boolean thisTestHasImage, String nextImageFileName) {
        if(!nextTestHasImage && !thisTestHasImage) return; // no animation
        ShapeableImageView img = rootView.findViewById(R.id.word_img);
        Animation swapImgToLeft = AnimationUtils.loadAnimation(thiscontext, R.anim.swap_img_to_left);
        Animation swapImgToRight = AnimationUtils.loadAnimation(thiscontext, R.anim.swap_img_to_right);
        Animation swapImgFromLeft = AnimationUtils.loadAnimation(thiscontext, R.anim.swap_img_from_left);
        Animation swapImgFromRight = AnimationUtils.loadAnimation(thiscontext, R.anim.show_img_from_right);

        int nextTestType = -1;
        if(testKeys!=null) nextTestType = testKeys.get(numberOfTestToEndTesting-1)/4;
        int finalNextTestType = nextTestType;

        swapImgToLeft.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isUnskippableAnimationRunning = true;
                hideTestUI();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                //img.setImageResource(R.drawable.hardware_icn);
                if(nextTestHasImage) img.startAnimation(swapImgFromRight);
                else displayUiAfterAnimation(finalNextTestType);
            }

        });

        swapImgToRight.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isUnskippableAnimationRunning=true;
                hideTestUI();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                //img.setImageResource(R.drawable.hardware_icn);
                if(nextTestHasImage) img.startAnimation(swapImgFromLeft);
                else displayUiAfterAnimation(finalNextTestType);
            }

        });

        swapImgFromLeft.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                if(nextImageFileName!=null) setTestImage(nextImageFileName);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                displayUiAfterAnimation(finalNextTestType);
                isUnskippableAnimationRunning = false;
            }

        });

        swapImgFromRight.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                if(nextImageFileName!=null) setTestImage(nextImageFileName);
                if(!thisTestHasImage) hideTestUI();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                displayUiAfterAnimation(finalNextTestType);
                isUnskippableAnimationRunning = false;
            }

        });

        if(!thisTestHasImage) img.startAnimation(swapImgFromRight);
        else if(toLeft) img.startAnimation(swapImgToLeft);
        else img.startAnimation(swapImgToRight);
    }

    private void displayUiAfterAnimation(int nextTestType) {
        switch(nextTestType) {
            case 0:
                AudioTestFrameEnEn();
                break;
            case 1:
                TranslateTestFrameUaEn();
                break;
            case 2:
                TranslateTestFrameEnUa();
                break;
            default:
                ChooseWord();
                break;
        }
    }

    private void setTestResultView() {
        ((MainActivity)getActivity()).displayTestResult(topicName,lives,numberOfRightAnswers, currentTestWords.size());
    }

    private void setTestFragment() {
        testTopicName = rootView.findViewById(R.id.topic_name);
        wordImg = rootView.findViewById(R.id.word_img);
        wordValueOnChooseScreen = rootView.findViewById(R.id.choose_word_value);
        wordTranslateOnChooseScreen = rootView.findViewById(R.id.choose_word_translate);
        knowButton = rootView.findViewById(R.id.choose_know_button);
        learnButton = rootView.findViewById(R.id.choose_learn_button);
        heartFirst = rootView.findViewById(R.id.heart_1);
        heartSecond = rootView.findViewById(R.id.heart_2);
        heartThird = rootView.findViewById(R.id.heart_3);

        // Make hearts visible from start in every testing
        heartFirst.setVisibility(View.VISIBLE);
        heartSecond.setVisibility(View.VISIBLE);
        heartThird.setVisibility(View.VISIBLE);

        // Bonus Icon
        bonusIcon = rootView.findViewById(R.id.bonus_icn_test);

        numberOfTestView=rootView.findViewById(R.id.test_fragment_test_number);

        nextButton = rootView.findViewById(R.id.next_button);
        wordAudioImgButton = rootView.findViewById(R.id.word_audio_img_button);
        letterRecycler = rootView.findViewById(R.id.letter_chooser);
        wordTranslateOnTestScreen = rootView.findViewById(R.id.test_word_translate);
        answerButtonBottomFirst = rootView.findViewById(R.id.answer_button_bottom_1);
        answerButtonBottomSecond = rootView.findViewById(R.id.answer_button_bottom_2);
        answerButtonBottomThird = rootView.findViewById(R.id.answer_button_bottom_3);
        answerButtonBottomFourth = rootView.findViewById(R.id.answer_button_bottom_4);
        answerButtonTopFirst = rootView.findViewById(R.id.answer_button_top_1);
        answerButtonTopSecond = rootView.findViewById(R.id.answer_button_top_2);
        answerButtonTopThird = rootView.findViewById(R.id.answer_button_top_3);
        answerButtonTopFourth = rootView.findViewById(R.id.answer_button_top_4);
        wordValueOnTestScreen = rootView.findViewById(R.id.test_word_value);
    }

    public void ChooseWord(){
        wordImg.setVisibility(View.VISIBLE);
        numberOfTestView.setVisibility(View.GONE);
        wordValueOnChooseScreen.setVisibility(View.VISIBLE);
        wordTranslateOnChooseScreen.setVisibility(View.VISIBLE);
        knowButton.setVisibility(View.VISIBLE);
        learnButton.setVisibility(View.VISIBLE);
        heartFirst.setVisibility(View.GONE);
        heartSecond.setVisibility(View.GONE);
        heartThird.setVisibility(View.GONE);
        bonusIcon.setVisibility(View.GONE);
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

    private void hideTestUI() {
        wordValueOnChooseScreen.setVisibility(View.GONE);
        wordTranslateOnChooseScreen.setVisibility(View.GONE);
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
        wordAudioImgButton.setVisibility(View.GONE);
    }

    public void AudioTestFrameEnEn(){
        wordImg.setVisibility(View.VISIBLE);
        numberOfTestView.setVisibility(View.VISIBLE);
        wordValueOnChooseScreen.setVisibility(View.GONE);
        wordTranslateOnChooseScreen.setVisibility(View.GONE);
        knowButton.setVisibility(View.GONE);
        learnButton.setVisibility(View.GONE);
        // Don't uncomment. With this lives reset after every test part
        //heartFirst.setVisibility(View.VISIBLE);
        //heartSecond.setVisibility(View.VISIBLE);
        //heartThird.setVisibility(View.VISIBLE);
        bonusIcon.setVisibility(View.VISIBLE);
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
        numberOfTestView.setVisibility(View.VISIBLE);
        wordValueOnChooseScreen.setVisibility(View.GONE);
        wordTranslateOnChooseScreen.setVisibility(View.GONE);
        knowButton.setVisibility(View.GONE);
        learnButton.setVisibility(View.GONE);
        // Don't uncomment. With this lives reset after every test part
        //heartFirst.setVisibility(View.VISIBLE);
        //heartSecond.setVisibility(View.VISIBLE);
        //heartThird.setVisibility(View.VISIBLE);
        bonusIcon.setVisibility(View.VISIBLE);
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
        numberOfTestView.setVisibility(View.VISIBLE);
        wordValueOnChooseScreen.setVisibility(View.GONE);
        wordTranslateOnChooseScreen.setVisibility(View.GONE);
        knowButton.setVisibility(View.GONE);
        learnButton.setVisibility(View.GONE);
        // Don't uncomment. With this lives reset after every test part
        //heartFirst.setVisibility(View.VISIBLE);
        //heartSecond.setVisibility(View.VISIBLE);
        //heartThird.setVisibility(View.VISIBLE);
        bonusIcon.setVisibility(View.VISIBLE);
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
    public void OnLetterClicked(int pos) {
        if(!letterAdapter.lettersAreChosen.get(pos)) { // we can't add same letter twice
            wordChosenByLetters+=letterAdapter.letterList.get(pos);
            letterAdapter.lettersAreChosen.set(pos, true);
        }
        isDecisionMade=true;
        for(Boolean l: letterAdapter.lettersAreChosen) {
            if(!l) isDecisionMade=false;
        }
        if(isDecisionMade) {
            for (int childCount = letterRecycler.getChildCount(), i = 0; i < childCount; ++i) {
                final RecyclerView.ViewHolder holder = letterRecycler.getChildViewHolder(letterRecycler.getChildAt(i));
                if(wordChosenByLetters.equals(currentWordEnglish))
                    holder.itemView.findViewById(R.id.card_view_of_letter_item).setBackground(getResources().getDrawable(R.drawable.right_test_button_background));
                else
                    holder.itemView.findViewById(R.id.card_view_of_letter_item).setBackground(getResources().getDrawable(R.drawable.wrong_test_button_background));
            }
        }
    }


    private void launchTesting(String testingTopic) {
        setTestFragment();
        testTopicName.setText(testingTopic);
        currentTestWords.clear();
        ChooseWord();
        Toast.makeText(getContext(), "Your must select 4 words for the test", Toast.LENGTH_SHORT).show();
        launchWordsForLearningDemonstration(testingTopic);
    }

    private void launchWordsForLearningDemonstration(String topicName) {
        // When DB will be ready replace allTopicWords getting by method that get all words(sorted with memoryFactor) from topic
        PriorityQueue<Word> pqAllWord = TestRepository.getWordsByTopic(1);
        allTopicWords = new ArrayList<>(pqAllWord.size());; // create by copying, need them to get random answer options
        while (!pqAllWord.isEmpty()) {
            allTopicWords.add(pqAllWord.poll());
        }

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
                getFragmentManager().beginTransaction().remove((Fragment) TestFragment.this).commitAllowingStateLoss();
            }
            return;
        }
        Collections.shuffle(demonstrationWords); // random demonstration order
        wordValueOnChooseScreen.setText(demonstrationWords.get(0).english);
        wordTranslateOnChooseScreen.setText(demonstrationWords.get(0).ukrainian.get(0));
        setTestImage(demonstrationWords.get(0).imageUrl);

        knowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isUnskippableAnimationRunning) return; // wait till animation ends
                if(demonstrationWords.size()>1)
                    launchAnimation(true, true, true, demonstrationWords.get(1).imageUrl);
                demonstrationWords.get(0).memoryFactor+=50;
                demonstrationWords.remove(0);
                if(demonstrationWords.size()>0) {
                    wordValueOnChooseScreen.setText(demonstrationWords.get(0).english);
                    wordTranslateOnChooseScreen.setText(demonstrationWords.get(0).ukrainian.get(0));
                    //setTestImage(demonstrationWords.get(0).imageUrl);
                } else {
                    Log.d("TAG", "All words have been demonstrated. Start new cycle");
                    launchWordsForLearningDemonstration(topicName);
                }
            }
        });

        learnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isUnskippableAnimationRunning) return; // wait till animation ends
                if(demonstrationWords.size()>1 && currentTestWords.size()<3)
                    launchAnimation(false, true, true, demonstrationWords.get(1).imageUrl);
                currentTestWords.add(demonstrationWords.get(0));
                demonstrationWords.remove(0);
                if(currentTestWords.size()>3) {
                    startTesting();
                } else if(demonstrationWords.size()>0) {
                    wordValueOnChooseScreen.setText(demonstrationWords.get(0).english);
                    wordTranslateOnChooseScreen.setText(demonstrationWords.get(0).ukrainian.get(0));
                    //setTestImage(demonstrationWords.get(0).imageUrl);
                } else {
                    launchWordsForLearningDemonstration(topicName);
                }
            }
        });
    }

    private void setTestImage(String imageFileName) {
        try
        {
            // get input stream
            InputStream ims = getActivity().getAssets().open("images/"+imageFileName);
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            wordImg.setImageDrawable(d);
            ims .close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            wordImg.setImageResource(R.drawable.hardware_icn);
            Log.d("TAG", "Can't load image "+imageFileName);
        }
    }

    private void startTesting() {
        for(Word word : currentTestWords) {
            Log.d("TAG", "You will learn: "+word.english);
            word.memoryFactor+=POINTS_FOR_TESTING;
        }
        numberOfTestToEndTesting=currentTestWords.size()*3; // 3 test for each word
        testKeys = new ArrayList<>();
        for(int i=0; i<numberOfTestToEndTesting; i++) testKeys.add(i);
        //Collections.shuffle(testKeys);
        numberOfRightAnswers = 0;
        launchTest();
        resetLives();
    }

    private void launchTest() {
        isDecisionMade = false;
        numberOfTestToEndTesting--;
        numberOfTestView.setText((currentTestWords.size()*3-numberOfTestToEndTesting)+"/"+(currentTestWords.size()*3));
        if(numberOfTestToEndTesting < 0) return; // stop if we finish all tests
        int testKey = testKeys.get(numberOfTestToEndTesting);

        Word currentWord = currentTestWords.get(testKey%4);
        currentWordEnglish = currentWord.english;

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



        int testType = testKey/4; // test type
        setChosenAnswer(-1, true);
        wordChosenByLetters = "";

        if(numberOfTestToEndTesting==currentTestWords.size()-1) { // set for first image, animation will do it for others
            setTestImage(currentWord.imageUrl);
        }
        switch (testType) {
            case 0:
                AudioTestFrameEnEn();
                setLetterChooser(currentWord.english);
                break;
            case 1:
                TranslateTestFrameUaEn();
                wordTranslateOnTestScreen.setText(currentWord.ukrainian.get(0));
                answerButtonBottomFirst.setText(testOptions.get(0).english);
                answerButtonBottomFirst.setOnClickListener(x -> setChosenAnswer(0, answerButtonBottomFirst.getText().equals(currentWord.english)));
                answerButtonBottomSecond.setText(testOptions.get(1).english);
                answerButtonBottomSecond.setOnClickListener(x -> setChosenAnswer(1, answerButtonBottomSecond.getText().equals(currentWord.english)));
                answerButtonBottomThird.setText(testOptions.get(2).english);
                answerButtonBottomThird.setOnClickListener(x -> setChosenAnswer(2, answerButtonBottomThird.getText().equals(currentWord.english)));
                answerButtonBottomFourth.setText(testOptions.get(3).english);
                answerButtonBottomFourth.setOnClickListener(x -> setChosenAnswer(3, answerButtonBottomFourth.getText().equals(currentWord.english)));
                break;
            case 2:
                TranslateTestFrameEnUa();
                wordValueOnTestScreen.setText(currentWord.english);
                answerButtonTopFirst.setText(testOptions.get(0).ukrainian.get(0));
                answerButtonTopFirst.setOnClickListener(x -> {
                    boolean isRight = false;
                    for(int i=0; i < currentWord.ukrainian.size(); i++) {
                        if(currentWord.ukrainian.get(i).equals(answerButtonTopFirst.getText().toString())) {
                            isRight=true;
                            break;
                        }
                    }
                    setChosenAnswer(0, isRight);
                });
                answerButtonTopSecond.setText(testOptions.get(1).ukrainian.get(0));
                answerButtonTopSecond.setOnClickListener(x -> {
                    boolean isRight = false;
                    for(int i=0; i < currentWord.ukrainian.size(); i++) {
                        if(currentWord.ukrainian.get(i).equals(answerButtonTopSecond.getText().toString())) {
                            isRight=true;
                            break;
                        }
                    }
                    setChosenAnswer(1, isRight);
                });
                answerButtonTopThird.setText(testOptions.get(2).ukrainian.get(0));
                answerButtonTopThird.setOnClickListener(x -> {
                    boolean isRight = false;
                    for(int i=0; i < currentWord.ukrainian.size(); i++) {
                        if(currentWord.ukrainian.get(i).equals(answerButtonTopThird.getText().toString())) {
                            isRight=true;
                            break;
                        }
                    }
                    setChosenAnswer(2, isRight);
                });
                answerButtonTopFourth.setText(testOptions.get(3).ukrainian.get(0));
                answerButtonTopFourth.setOnClickListener(x -> {
                    boolean isRight = false;
                    for(int i=0; i < currentWord.ukrainian.size(); i++) {
                        if(currentWord.ukrainian.get(i).equals(answerButtonTopFourth.getText().toString())) {
                            isRight=true;
                            break;
                        }
                    }
                    setChosenAnswer(3, isRight);
                });
                break;
            default:
                //
        }

        wordAudioImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer player = new MediaPlayer();
                try {
                    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    String dataSourceStr = "https://ssl.gstatic.com/dictionary/static/sounds/20200429/"+currentWord.english+"--_gb_1.mp3";
                    player.setDataSource(dataSourceStr);
                    player.prepare();
                    player.start();
                } catch (Exception ex) {
                    // try another audio
                    try {
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        String dataSourceStr = "https://api.dictionaryapi.dev/media/pronunciations/en/"+currentWord.english+"-us.mp3";
                        player.setDataSource(dataSourceStr);
                        player.prepare();
                        player.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "This word haven't got audio or you have no connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isDecisionMade) {
                    if (testType==0)
                        Toast.makeText(getContext(), "All letters must be selected", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getContext(), "Chose something", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "Chose some option to go further");
                    return;
                }
                if(numberOfTestToEndTesting>0) {
                    int nextTestType = testKeys.get(numberOfTestToEndTesting-1)/4;
                    boolean nextTestHasImage = nextTestType!=2;
                    boolean thisTestHasImage = testType!=2;
                    int nextTestKey = testKeys.get(numberOfTestToEndTesting-1);
                    String nextTestImage = currentTestWords.get(nextTestKey%4).imageUrl;
                    launchAnimation(true, nextTestHasImage, thisTestHasImage, nextTestImage);
                }
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