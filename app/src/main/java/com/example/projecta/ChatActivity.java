package com.example.projecta;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class ChatActivity extends AppCompatActivity {

    String name, address, event;
    Toolbar toolbar;
    ChatroomFragment chatroomFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        name = getIntent().getStringExtra("NAME");
        address = getIntent().getStringExtra("ADDRESS");
        event = getIntent().getStringExtra("EVENT");

        toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle(event + "聊天室");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> {
            startActivity(new Intent(ChatActivity.this, MenuActivity.class));
        });

        Log.d(TAG, "onCreate: name = " + name + " | address = " + address + " | event = " + event);

        chatroomFragment = new ChatroomFragment(this, name, address, event, getWindow());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.chat_fragment_container, chatroomFragment, "ChatFragment").commit();
    }
}