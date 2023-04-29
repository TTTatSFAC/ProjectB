package com.example.projecta.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projecta.R;
import com.example.projecta.database.UserDB;
import com.example.projecta.model.ChatMsg;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ChatAdapter extends FirebaseRecyclerAdapter<ChatMsg, ChatAdapter.ChatMsgHolder> {

    private static final String TAG = "";
    String uuid;
    Context context;

    public ChatAdapter(@NonNull FirebaseRecyclerOptions<ChatMsg> options, String uuid, Context context) {
        super(options);
        this.uuid = uuid;
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatAdapter.ChatMsgHolder holder, int position, @NonNull ChatMsg chatMsg) {
        String sentTime = String.valueOf(new SimpleDateFormat("hh:mm a", Locale.TRADITIONAL_CHINESE)
                .format(chatMsg.getTime()));

        if (!chatMsg.getUUID().equals(uuid)) {
            holder.otherUserLayout.setVisibility(View.VISIBLE);
            holder.userLayout.setVisibility(View.GONE);

            if (TextUtils.isEmpty(chatMsg.getImagePath())) {
                holder.tvTimeOther.setVisibility(View.VISIBLE);
                holder.tvMsgOther.setVisibility(View.VISIBLE);
                holder.tvImgTimeOther.setVisibility(View.GONE);
                holder.imgOther.setVisibility(View.GONE);

                holder.tvMsgOther.setText(chatMsg.getMessage());
                holder.tvTimeOther.setText(sentTime);
            } else {
                holder.tvTimeOther.setVisibility(View.GONE);
                holder.tvMsgOther.setVisibility(View.GONE);
                holder.tvImgTimeOther.setVisibility(View.VISIBLE);
                holder.imgOther.setVisibility(View.VISIBLE);

                StorageReference firestore = new UserDB().getFirestore();
                Log.d(TAG, "onBindViewHolder: imagePath = " + chatMsg.getImagePath());
                firestore = firestore.child("images/" + chatMsg.getImagePath());

                firestore.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(context)
                        .load(uri)
                        .into(holder.imgOther)).addOnFailureListener(e -> {
                            e.printStackTrace();
                            Toast.makeText(context, "圖片下載失敗", Toast.LENGTH_SHORT).show();
                });
                holder.tvImgTimeOther.setText(sentTime);
//                holder.imgOther.setOnClickListener(v -> showImage(chatMsg));
            }

            holder.tvUserOther.setText(chatMsg.getUsername());

        } else {
            holder.userLayout.setVisibility(View.VISIBLE);
            holder.otherUserLayout.setVisibility(View.GONE);


            Log.d(TAG, "onBindViewHolder: image path = " + chatMsg.getImagePath());
            if (TextUtils.isEmpty(chatMsg.getImagePath())) {
                holder.tvTimeUser.setVisibility(View.VISIBLE);
                holder.tvMsgUser.setVisibility(View.VISIBLE);
                holder.tvImgTimeUser.setVisibility(View.GONE);
                holder.imgUser.setVisibility(View.GONE);

                holder.tvMsgUser.setText(chatMsg.getMessage());
                holder.tvTimeUser.setText(sentTime);
            } else {
                holder.tvTimeUser.setVisibility(View.GONE);
                holder.tvMsgUser.setVisibility(View.GONE);
                holder.tvImgTimeUser.setVisibility(View.VISIBLE);
                holder.imgUser.setVisibility(View.VISIBLE);

                StorageReference firestore = new UserDB().getFirestore();
                firestore = firestore.child("images/" + chatMsg.getImagePath());
                firestore.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(context)
                        .load(uri)
                        .into(holder.imgUser)).addOnFailureListener(e -> e.printStackTrace());

                holder.tvImgTimeUser.setText(sentTime);
//                holder.imgUser.setOnClickListener(v -> showImage(chatMsg));
            }
        }

        holder.tvMsgOther.setOnClickListener(v->copyToClipBoard(chatMsg.getMessage(), context));
        holder.tvMsgUser.setOnClickListener(v->copyToClipBoard(chatMsg.getMessage(), context));
    }

    private void copyToClipBoard(String string, Context context) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("message", string);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(context, "已複製訊息!", Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public ChatAdapter.ChatMsgHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom, parent, false);
        return new ChatMsgHolder(v);
    }

    class ChatMsgHolder extends RecyclerView.ViewHolder {
        TextView tvUserOther, tvMsgOther, tvTimeOther, tvImgTimeOther;
        ImageView imgOther;
        TextView tvMsgUser, tvTimeUser, tvImgTimeUser;
        ImageView imgUser;
        RelativeLayout otherUserLayout, userLayout;
        public ChatMsgHolder(@NonNull View itemView) {
            super(itemView);
            otherUserLayout = itemView.findViewById(R.id.other_user_layout);
            tvUserOther = itemView.findViewById(R.id.tv_user_other);
            tvMsgOther = itemView.findViewById(R.id.tv_msg_other);
            tvTimeOther = itemView.findViewById(R.id.tv_time_other);
            tvImgTimeOther = itemView.findViewById(R.id.tv_time_img_other);
            imgOther = itemView.findViewById(R.id.img_msg_other);

            userLayout = itemView.findViewById(R.id.user_layout);
            tvMsgUser = itemView.findViewById(R.id.tv_msg_user);
            tvTimeUser = itemView.findViewById(R.id.tv_time_user);
            tvImgTimeUser = itemView.findViewById(R.id.tv_time_img_user);
            imgUser = itemView.findViewById(R.id.img_msg_user);

        }
    }
}
