package com.example.projecta.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projecta.ChatActivity;
import com.example.projecta.R;
import com.example.projecta.model.User;

import java.util.ArrayList;

public class EventMenuAdapter extends RecyclerView.Adapter<EventMenuAdapter.ViewHolder> {

    User user;
    ArrayList<String> eventArrayList;
    Context context;

    public EventMenuAdapter(User user, Context context) {
        this.user = user;
        this.eventArrayList = user.getEvents();
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImg;
        TextView textView;
        ConstraintLayout eventContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImg = itemView.findViewById(R.id.event_menu_img);
            textView = itemView.findViewById(R.id.event_menu_name);
            eventContainer = itemView.findViewById(R.id.event_menu_container);
        }
    }

    @NonNull
    @Override
    public EventMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_menu_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventMenuAdapter.ViewHolder holder, int position) {
        String event = eventArrayList.get(position);
        holder.textView.setText(event);

        holder.eventContainer.setOnClickListener(v->{
            Intent intent = new Intent();
            intent.putExtra("NAME", user.getName());
            intent.putExtra("ADDRESS", user.getAddress());
            intent.putExtra("EVENT", event);
            intent.setClass(context, ChatActivity.class);
            context.startActivity(new Intent(intent));
        });
    }

    @Override
    public int getItemCount() {
        return eventArrayList.size();
    }
}
