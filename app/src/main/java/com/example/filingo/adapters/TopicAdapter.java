package com.example.filingo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filingo.MainActivity;
import com.example.filingo.R;

import java.util.List;



public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    public Context context;
    public List<Topic> topicList;
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