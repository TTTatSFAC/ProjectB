package com.example.projecta.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projecta.R;

import java.util.ArrayList;
import java.util.HashMap;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    ArrayList<String> eventArrayList;
    HashMap<String, Boolean> eventHash;
    Context context;

    public EventAdapter(ArrayList<String> eventArrayList, HashMap<String, Boolean> eventHash, Context context) {
        this.eventArrayList = eventArrayList;
        this.eventHash = eventHash;
        this.context = context;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView eventImg, checkBoxImg, checkedBoxImg;
        TextView textView;
        ConstraintLayout eventContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImg = itemView.findViewById(R.id.event_img);
            checkBoxImg = itemView.findViewById(R.id.check_box_img);
            checkedBoxImg = itemView.findViewById(R.id.checked_box_img);
            textView = itemView.findViewById(R.id.event_name);
            eventContainer = itemView.findViewById(R.id.event_container);
        }
    }

    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        String event = eventArrayList.get(position);
        holder.textView.setText(event);

        holder.eventContainer.setOnClickListener(v->{
            boolean select = eventHash.get(event);
            eventHash.replace(event, select, !select);

            if (!select) {
                holder.checkBoxImg.setVisibility(View.INVISIBLE);
                holder.checkedBoxImg.setVisibility(View.VISIBLE);
            } else {
                holder.checkBoxImg.setVisibility(View.VISIBLE);
                holder.checkedBoxImg.setVisibility(View.INVISIBLE);
            }

        });

    }

    @Override
    public int getItemCount() {
        return eventArrayList.size();
    }
}
