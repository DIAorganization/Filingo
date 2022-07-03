package com.example.filingo.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filingo.R;

import java.util.ArrayList;
import java.util.List;

public class LetterAdapter extends RecyclerView.Adapter<LetterAdapter.LetterViewHolder> {

    public Context context;
    public List<Character> letterList;
    public List<Boolean> lettersAreChosen;
    public View lastSelected;
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
        super.onViewRecycled(holder);
        ((CardView) holder.itemView.findViewById(R.id.card_view_of_letter_item)).setCardBackgroundColor(context.getResources().getColor(R.color.backgroud_for_buttons));
        if(lettersAreChosen.get(position))
            ((CardView) holder.itemView.findViewById(R.id.card_view_of_letter_item)).setCardBackgroundColor(context.getResources().getColor(R.color.light_gray));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                //   if(lastSelected != null)
                //     ((CardView) lastSelected.findViewById(R.id.card_view_of_letter_item)).setCardBackgroundColor(context.getResources().getColor(R.color.backgroud_for_buttons));
                ((CardView) view.findViewById(R.id.card_view_of_letter_item)).setCardBackgroundColor(context.getResources().getColor(R.color.light_gray));
                holder.wasChosen = true;
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
        public boolean wasChosen;
        public LetterViewHolder(@NonNull View itemView) {
            super(itemView);
            letter = itemView.findViewById(R.id.letter);
        }
    }

}