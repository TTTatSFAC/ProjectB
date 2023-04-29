package com.example.projecta;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.projecta.database.UserDB;
import com.google.gson.Gson;

public class SexActivity extends AppCompatActivity {

    ImageView maleImg, femaleImg;
    Toolbar toolbar;
    String userId;

    public void getUser() {
        userId = getIntent().getStringExtra("UUID");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sex);

        toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("選擇性別");
        setSupportActionBar(toolbar);

        getUser();

        maleImg = findViewById(R.id.male_button);
        femaleImg = findViewById(R.id.female_button);

        maleImg.setOnClickListener(view -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SexActivity.this);
            alertDialogBuilder.setTitle("你的性別是: 公公")
                    .setNegativeButton("否", null)
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            goNextPage(true);
                        }
                    })
                    .create()
                    .show();
        });

        femaleImg.setOnClickListener(view -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SexActivity.this);
            alertDialogBuilder.setTitle("你的性別是: 婆婆")
                    .setNegativeButton("否", null)
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            goNextPage(false);
                        }
                    })
                    .create()
                    .show();
        });

    }

    private void goNextPage(boolean male) {
        Intent intent = new Intent();

        UserDB userDB = new UserDB();
        userDB.writeNewUserGender(userId, male);

        intent.putExtra("UUID", userId);
        intent.setClass(SexActivity.this, NameActivity.class);
        startActivity(intent);
    }

}