package com.example.projecta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.projecta.adapter.EventAdapter;
import com.example.projecta.database.UserDB;
import com.example.projecta.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public class EventActivity extends AppCompatActivity {

    ArrayList<String> eventArrayList;
    HashMap<String, Boolean> eventHashMap;
    RecyclerView recyclerView;
    Button confirmBtn;
    Toolbar toolbar;

    String userId;

    public void getUser() {
        userId = getIntent().getStringExtra("UUID");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("選擇活動");
        setSupportActionBar(toolbar);

        getUser();

        initEvents();
        initRecyclerView();

        confirmBtn = findViewById(R.id.event_btn);
        confirmBtn.setOnClickListener(view -> {
            ArrayList<String> selectedEvent = new ArrayList<>();
            for (String event : eventArrayList) {
                Log.d("TAG", "onCreate: hashing" + event + ":" + eventHashMap.get(event));
                if (eventHashMap.get(event)) {
                    selectedEvent.add(event);
                }
            }

            UserDB userDB = new UserDB();
            userDB.writeNewEvents(userId, selectedEvent);
            userDB.writeNewUserId(userId);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("EVENTS", selectedEvent);
            bundle.putString("UUID", userId);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            intent.setClass(this, MenuActivity.class);

            startActivity(intent);
        });

    }

    public void initRecyclerView() {
        recyclerView = findViewById(R.id.event_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(new EventAdapter(eventArrayList, eventHashMap, this));
    }

    public void initEvents() {
        addEventArray();

        eventHashMap = new HashMap<>();
        for(String event : eventArrayList) {
           eventHashMap.put(event, false);
        }
    }

    public void addEventArray() {
        eventArrayList = new ArrayList<>();
        eventArrayList.add("行山");
        eventArrayList.add("廣場舞");
        eventArrayList.add("捉棋");
        eventArrayList.add("看書");
        eventArrayList.add("煮飯");
        eventArrayList.add("麻雀");
        eventArrayList.add("攝影");
        eventArrayList.add("編織");
        eventArrayList.add("粵劇");
        eventArrayList.add("品茶");
        eventArrayList.add("露營");
        eventArrayList.add("閒聊");
    }
}