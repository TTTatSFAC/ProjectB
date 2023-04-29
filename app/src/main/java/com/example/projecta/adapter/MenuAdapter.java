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

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    ArrayList<String> eventArrayList;
    String location;
    Context context;

    public MenuAdapter(ArrayList<String> eventArrayList, String location, Context context) {
        this.eventArrayList = eventArrayList;
        this.location = location;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_items, parent, false);
        return new MenuAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String event = eventArrayList.get(position);
        holder.textView.setText(event);

        holder.menuContainer.setOnClickListener(v->{

        });
    }

    @Override
    public int getItemCount() {
        return eventArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView menuImg;
        TextView textView;
        ConstraintLayout menuContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            menuImg = itemView.findViewById(R.id.menu_img);
            textView = itemView.findViewById(R.id.menu_name);
            menuContainer = itemView.findViewById(R.id.menu_container);
        }
    }

}
