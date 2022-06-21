package com.example.filingo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LetterAdapter.OnItemClicked {



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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.main_frame_info);




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

        //ChooseWord();


        ArrayList<Character> listOfLetters = new ArrayList<>();
        listOfLetters.add('o');
        listOfLetters.add('M');
        listOfLetters.add('s');
        listOfLetters.add('u');
        listOfLetters.add('e');



        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        letterRecycler.setLayoutManager(layoutManager);
        letterAdapter = new LetterAdapter(this, listOfLetters, MainActivity.this);
        letterRecycler.setAdapter(letterAdapter);

        AudioTestFrameEnEn();



       // TranslateTestFrameUaEn();
        //TranslateTestFrameEnUa();



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
    public void OnItemClicked(Character letter) {

    }
}

class LetterAdapter extends RecyclerView.Adapter<LetterAdapter.LetterViewHolder> {

    Context context;
    List<Character> letterList;
    View lastSelected;
    private OnItemClicked mListener;

    public LetterAdapter(Context context, List<Character> letterList , OnItemClicked mListener) {
        this.context = context;
        this.letterList = letterList;
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
                if(lastSelected != null)
                    ((CardView) lastSelected.findViewById(R.id.card_view_of_letter_item)).setCardBackgroundColor(context.getResources().getColor(R.color.backgroud_for_buttons));
                ((CardView) holder.itemView.findViewById(R.id.card_view_of_letter_item)).setBackgroundColor(context.getResources().getColor(R.color.light_gray));

                mListener.OnItemClicked(letterList.get(pos));
                lastSelected = holder.itemView;
            }
        });

    }

    public interface OnItemClicked {
        void OnItemClicked(Character letter);
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
