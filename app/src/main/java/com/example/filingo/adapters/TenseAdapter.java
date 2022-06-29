package com.example.filingo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filingo.MainActivity;
import com.example.filingo.R;

import java.util.List;

public class TenseAdapter extends RecyclerView.Adapter<TenseAdapter.TenseViewHolder> {

    public Context context;
    public List<String> tenseNames;
    private OnTenseClicked mListener;

    public TenseAdapter(Context context, List<String> tenseNames , OnTenseClicked mListener) {
        this.context = context;
        this.tenseNames = tenseNames;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public TenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tenses_chooser_item, parent, false);
        return new TenseViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final TenseViewHolder holder, int position) {
        holder.tense.setText( tenseNames.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MainActivity.mediaPlayerArrayList.get(0).isPlaying() && !MainActivity.mediaPlayerArrayList.get(1).isPlaying() && !MainActivity.mediaPlayerArrayList.get(1).isPlaying())
                    MainActivity.getRandomMediaPlayer().start();
                int pos = holder.getAdapterPosition();
                mListener.OnTenseClicked(pos);
            }
        });

    }

    public interface OnTenseClicked {
        void OnTenseClicked(int pos);
    }

    @Override
    public int getItemCount() {
        return tenseNames.size();
    }


    public static final class TenseViewHolder extends RecyclerView.ViewHolder{
        TextView tense;
        public TenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tense = itemView.findViewById(R.id.tenses_name);
        }
    }

}
