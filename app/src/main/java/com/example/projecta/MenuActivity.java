package com.example.projecta;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.projecta.adapter.EventMenuAdapter;
import com.example.projecta.database.UserDB;
import com.example.projecta.model.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    String userId;
    User user;
    RecyclerView recyclerView;
    Toolbar toolbar;
    ArrayList<String> events;
    DatabaseReference mDatabase;

    public void initialization() {
        events = new ArrayList<>();
        initDatabase();
        initView();
    }

    public boolean phoneNumberExist(String phoneNumber) {
        UserDB userDB = new UserDB();
        DatabaseReference userRef = userDB.getDatabase().child("users").getRef();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getPhoneNumber().equals(phoneNumber)) {
                        //TODO: login
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return false;
    }

    private void getUserInfo() {
        userId = getSharedPreferences("user", MODE_PRIVATE).getString("UUID", "");
        Log.e(TAG, "onCreate: userId = " + userId);

        mDatabase.child("users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if (user == null)
                    return;
                Log.d("firebase", user.toString());
                events = user.getEvents();
                Log.d(TAG, "onCreate: events size = " + events.size());
                setupRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void initView() {
        toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("得閒飲茶");

        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.event_menu_rv);
    }

    public void initDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = new UserDB().getDatabase();
    }

    public void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new EventMenuAdapter(user, this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initialization();
        getUserInfo();
        phoneNumberExist("54059511");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false)
                    .setTitle("登出")
                    .setMessage("確定要登出嗎?")
                    .setNegativeButton("否", null)
                    .setPositiveButton("是", (dialogInterface, i) -> {
                        AuthUI.getInstance().signOut(this)
                                .addOnCompleteListener(task -> {
                                    Toast.makeText(MenuActivity.this, "已成功登出", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this, MainActivity.class));
                                });
                    })
                    .create()
                    .show();
        }
        return true;
    }
}