package com.example.projecta.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projecta.R;
import com.example.projecta.model.District;

import java.util.ArrayList;
import java.util.HashMap;

public class RegionAdapter extends RecyclerView.Adapter<RegionAdapter.ViewHolder>{

    ArrayList<String> mData;
    ArrayList<Boolean> visibleList = new ArrayList<>();
    HashMap<String, District> map;
    Context context;
    String userId;

    RecyclerView mRecyclerView;

    public RegionAdapter(ArrayList<String> data, HashMap<String, District> map, Context context, String userId) {
        mData = data;
        this.map = map;
        this.context = context;
        this.userId = userId;
        for (String ignored : mData)
            visibleList.add(false);
    }

    @NonNull
    @Override
    public RegionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.region_items, parent, false);
        mRecyclerView = (RecyclerView) parent;
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RegionAdapter.ViewHolder holder, int position) {
        holder.tvRegion.setText("        >    " + mData.get(position));
        District district = map.get(mData.get(position));
        holder.imgRegion.setImageResource(district.getDistrictIcon());

        AddressAdapter addressAdapter = new AddressAdapter(district.getAddressList(),
                context, mData.get(position), userId);

        if (visibleList.get(position)) {
            holder.rvRegion.setVisibility(View.VISIBLE);
        } else {
            holder.rvRegion.setVisibility(View.GONE);
        }

        holder.rvRegion.setNestedScrollingEnabled(false);
        holder.rvRegion.setLayoutManager(new LinearLayoutManager(context));
        holder.rvRegion.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        holder.rvRegion.setAdapter(addressAdapter);
        holder.layoutRegion.setOnClickListener(view -> setVisible(!visibleList.get(position), position));
        holder.rvRegion.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_MOVE) {
                    rv.getParent().requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRegion;
        ImageView imgRegion;
        RecyclerView rvRegion;
        ConstraintLayout layoutRegion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRegion = itemView.findViewById(R.id.tv_region);
            imgRegion = itemView.findViewById(R.id.ic_region);
            rvRegion = itemView.findViewById(R.id.rv_region);
            layoutRegion = itemView.findViewById(R.id.address_layout);
        }
    }

    public void setVisible(boolean visible, int pos) {
        visibleList.set(pos, visible);
        this.notifyDataSetChanged();
    }
}
