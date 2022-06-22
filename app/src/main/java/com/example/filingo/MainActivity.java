package com.example.filingo;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.filingo.database.Word;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements LetterAdapter.OnLetterClicked , TopicAdapter.OnTopicClicked {

    private static int numberOfTestToEndTesting = 0; // need to count number of test in testing
    private static int numberOfRightAnswers = 0;  // need to count right answers in testing
    int chosenAnswer = -1; // to track chosen answer in test
    String wordChosenByLetters = "";

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
    LetterAdapter letterAdapter;
    TopicAdapter topicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTopicChoseView();
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
        heartFirst.setVisibility(View.VISIBLE);
        heartSecond.setVisibility(View.VISIBLE);
        heartThird.setVisibility(View.VISIBLE);
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
        heartFirst.setVisibility(View.VISIBLE);
        heartSecond.setVisibility(View.VISIBLE);
        heartThird.setVisibility(View.VISIBLE);
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
        heartFirst.setVisibility(View.VISIBLE);
        heartSecond.setVisibility(View.VISIBLE);
        heartThird.setVisibility(View.VISIBLE);
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
    }

    @Override
    public void OnTopicClicked(Topic topic) {
        launchTesting(topic.topicName, 5); // Start 10 tests
    }

    private void launchTesting(String testingTopic, int numberOfTests) {
        setTestFragment();
        testTopicName.setText(testingTopic);
        numberOfTestToEndTesting = numberOfTests;
        numberOfRightAnswers = 0;
        launchRandomTestOnTopic(testingTopic);
    }

    private void launchRandomTestOnTopic(String testingTopic) {
        numberOfTestToEndTesting--;
        if(numberOfTestToEndTesting < 0) return; // stop if we finish all tests
        // Words for testing(till we have full database)
        Word currentWord = new Word(); currentWord.id = 1; currentWord.english = "Book";
        {
            LinkedList<String> temp = new LinkedList<>();
            temp.add("Книга"); temp.add("Книжка"); temp.add("Том"); temp.add("Замовляти");
            currentWord.ukrainian = temp;
        }
        currentWord.audioUrl = null; currentWord.imageUrl = null; currentWord.topic = 1; currentWord.memoryFactor = 0;

        Word word2 = new Word(); word2.id = 1; word2.english = "House";
        {
            LinkedList<String> temp = new LinkedList<>();
            temp.add("Будинок"); temp.add("Дім"); temp.add("Житло"); temp.add("Квартирувати");
            word2.ukrainian = temp;
        }
        word2.audioUrl = null; word2.imageUrl = null; word2.topic = 1; word2.memoryFactor = 0;

        Word word3 = new Word(); word3.id = 1; word3.english = "Land";
        {
            LinkedList<String> temp = new LinkedList<>();
            temp.add("Земля"); temp.add("Край"); temp.add("Приземлятись"); temp.add("Причалювати");
            word3.ukrainian = temp;
        }
        word3.audioUrl = null; word3.imageUrl = null; word3.topic = 1; word3.memoryFactor = 0;

        Word word4 = new Word(); word4.id = 1; word4.english = "Fly";
        {
            LinkedList<String> temp = new LinkedList<>();
            temp.add("Політ"); temp.add("Летіти"); temp.add("Літати"); temp.add("Майоріти");
            word4.ukrainian = temp;
        }
        word4.audioUrl = null; word4.imageUrl = null; word4.topic = 1; word4.memoryFactor = 0;
        //

        int testType = (new Random()).nextInt(4); // random test type
        setChosenAnswer(-1);
        wordChosenByLetters = "";
        switch (testType) {
            case 0:
                ChooseWord();
                wordValueOnChooseScreen.setText(currentWord.english);
                wordTranslateOnChooseScreen.setText(word2.ukrainian.get(0));
                break;
            case 1:
                AudioTestFrameEnEn();
                setLetterChooser(currentWord.english);
                break;
            case 2:
                TranslateTestFrameUaEn();
                wordTranslateOnTestScreen.setText(currentWord.ukrainian.get(0));
                answerButtonBottomFirst.setText(currentWord.english);
                answerButtonBottomFirst.setOnClickListener(x -> {setChosenAnswer(0);});
                answerButtonBottomSecond.setText(word2.english);
                answerButtonBottomSecond.setOnClickListener(x -> setChosenAnswer(1));
                answerButtonBottomThird.setText(word4.english);
                answerButtonBottomThird.setOnClickListener(x -> setChosenAnswer(2));
                answerButtonBottomFourth.setText(word3.english);
                answerButtonBottomFourth.setOnClickListener(x -> setChosenAnswer(3));
                break;
            case 3:
                TranslateTestFrameEnUa();
                wordValueOnTestScreen.setText(currentWord.english);
                answerButtonTopFirst.setText(word3.ukrainian.get(0));
                answerButtonTopFirst.setOnClickListener(x -> setChosenAnswer(0));
                answerButtonTopSecond.setText(word4.ukrainian.get(0));
                answerButtonTopSecond.setOnClickListener(x -> setChosenAnswer(1));
                answerButtonTopThird.setText(currentWord.ukrainian.get(0));
                answerButtonTopThird.setOnClickListener(x -> setChosenAnswer(2));
                answerButtonTopFourth.setText(word2.ukrainian.get(0));
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
                        // we give + right answer if user click "know button"
                        break;
                    case 1:
                        Log.d("TAG", "Word Selected By Letters: "+wordChosenByLetters);
                        if(currentWord.english.equals(wordChosenByLetters)) numberOfRightAnswers++;
                        break;
                    case 2:
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
                        if(answerText.equals(currentWord.english)) numberOfRightAnswers++;
                        break;
                    case 3:
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
                        for(String s: currentWord.ukrainian) {
                            if(s.equals(answerText)) {
                                numberOfRightAnswers++;
                                break;
                            }
                        }
                        break;
                    default:
                        //
                }
                if(numberOfTestToEndTesting > 0) {
                    launchRandomTestOnTopic(testingTopic);
                } else {
                    Log.d("TAG", "Right Answers: "+numberOfRightAnswers);
                    setTopicChoseView();
                }
            }
        });
        knowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberOfRightAnswers++;
                if(numberOfTestToEndTesting > 0) {
                    launchRandomTestOnTopic(testingTopic);
                } else {
                    Log.d("TAG", "Right Answers: "+numberOfRightAnswers);
                    setTopicChoseView();
                }
            }
        });
        learnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(numberOfTestToEndTesting > 0) {
                    launchRandomTestOnTopic(testingTopic);
                } else {
                    Log.d("TAG", "Right Answers: "+numberOfRightAnswers);
                    setTopicChoseView();
                }
            }
        });
    }
}

class LetterAdapter extends RecyclerView.Adapter<LetterAdapter.LetterViewHolder> {

    Context context;
    List<Character> letterList;
    List<Boolean> lettersAreChosen;
    View lastSelected;
    private OnLetterClicked mListener;

    public LetterAdapter(Context context, List<Character> letterList , OnLetterClicked mListener) {
        this.context = context;
        this.letterList = letterList;
        this.lettersAreChosen = new ArrayList<>();
        for(int i=0; i<letterList.size(); i++) { // fill with 'false' value
            lettersAreChosen.add(false);
        }
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public LetterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.letter_chooser_item, parent, false);
        return new LetterViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final LetterViewHolder holder, int position) {
        holder.letter.setText( letterList.get(position) + "");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
             //   if(lastSelected != null)
               //     ((CardView) lastSelected.findViewById(R.id.card_view_of_letter_item)).setCardBackgroundColor(context.getResources().getColor(R.color.backgroud_for_buttons));
                ((CardView) holder.itemView.findViewById(R.id.card_view_of_letter_item)).setCardBackgroundColor(context.getResources().getColor(R.color.light_gray));

                mListener.OnLetterClicked(pos);
                lastSelected = holder.itemView;
            }
        });

    }

    public interface OnLetterClicked {
        void OnLetterClicked(int pos);
    }

    @Override
    public int getItemCount() {
        return letterList.size();
    }


    public static final class LetterViewHolder extends RecyclerView.ViewHolder{
        TextView letter;
        public LetterViewHolder(@NonNull View itemView) {
            super(itemView);
            letter = itemView.findViewById(R.id.letter);
        }
    }

}

class Topic{

    public Topic(Integer topicIcnUrl , String topicName){
        this.topicIcnUrl = topicIcnUrl;
        this.topicName = topicName;
    }

    public Integer topicIcnUrl;
    public String topicName;
    public Double topicProgress = 0.;//???
}

class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    Context context;
    List<Topic> topicList;
    private OnTopicClicked mListener;

    public TopicAdapter(Context context, List<Topic> topicList , OnTopicClicked mListener) {
        this.context = context;
        this.topicList = topicList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.topic_chooser_item, parent, false);
        return new TopicViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final TopicViewHolder holder, int position) {
        holder.topicIcn.setImageResource(topicList.get(position).topicIcnUrl);
        holder.topicName.setText( topicList.get(position).topicName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                mListener.OnTopicClicked(topicList.get(pos));
            }
        });

    }

    public interface OnTopicClicked {
        void OnTopicClicked(Topic topic);
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }


    public static final class TopicViewHolder extends RecyclerView.ViewHolder{
        ImageView topicIcn;
        TextView topicName;
        Double topicProgress;//???

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            topicIcn = itemView.findViewById(R.id.topic_icn);
            topicName  = itemView.findViewById(R.id.topic_name);
        }
    }

}
