package com.example.projecta;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.projecta.database.UserDB;
import com.example.projecta.model.User;

public class NameActivity extends AppCompatActivity {
    EditText nameEdit;
    Button confirmBtn;
    TextView textView;
    String name;
    Toolbar toolbar;

    String userId;

    public void getUser() {
        userId = getIntent().getStringExtra("UUID");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("你的名字");
        setSupportActionBar(toolbar);

        getUser();

        nameEdit = findViewById(R.id.editTextTextPersonName);
        confirmBtn = findViewById(R.id.confrim_button);
        textView = findViewById(R.id.name_tv);

        confirmBtn.setOnClickListener(view -> {
            name = nameEdit.getText().toString();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NameActivity.this);
            alertDialogBuilder.setTitle("你的名字是: " + name)
                    .setNegativeButton("否", null)
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            goNextPage(name);
                        }
                    })
                    .create()
                    .show();
        });
    }

    private void goNextPage(String name) {
        Intent intent = new Intent();

        UserDB userDB = new UserDB();
        userDB.writeNewUserName(userId, name);

        intent.putExtra("UUID", userId);
        intent.setClass(NameActivity.this, AddressActivity.class);
        startActivity(intent);
    }

}