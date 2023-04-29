package com.example.projecta.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projecta.EventActivity;
import com.example.projecta.R;
import com.example.projecta.SexActivity;
import com.example.projecta.database.UserDB;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder>{

    ArrayList<String> mData;
    Context context;
    String region, userId;

    public AddressAdapter(ArrayList<String> data, Context context, String region, String userId) {
        mData = data;
        this.context = context;
        this.region = region;
        this.userId = userId;

    }

    @NonNull
    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: data = " + mData.get(position));
        holder.tvAddress.setText("             >    " + mData.get(position));
        holder.itemView.setOnClickListener(v-> {
            Log.d(TAG, "onBindViewHolder: " + mData.get(position));
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("你住係" + mData.get(position) + "?")
                    .setNegativeButton("否", null)
                    .setPositiveButton("是", (dialogInterface, i) -> {
                        Intent intent = new Intent();
                        UserDB userDB = new UserDB();
                        userDB.writeNewUserAddress(userId, region);
                        intent.putExtra("UUID", userId);
                        intent.setClass(context, EventActivity.class);
                        context.startActivity(intent);
                    })
                    .create()
                    .show();
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tv_address);
        }
    }

}
