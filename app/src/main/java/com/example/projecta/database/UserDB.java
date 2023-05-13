package com.example.projecta.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class UserDB {

    private DatabaseReference mDatabase;
    private StorageReference mFirestore;

    public UserDB() {
        mDatabase = FirebaseDatabase.getInstance("https://eldergathering-3d82d-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    }

    public void setDatabase() {
        mDatabase = FirebaseDatabase.getInstance("https://eldergathering-3d82d-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    }

    public DatabaseReference getDatabase() {
        setDatabase();
        return mDatabase;
    }

    public void setFirestore() {
        mFirestore = FirebaseStorage.getInstance("gs://eldergathering-3d82d.appspot.com/").getReference();
    }

    public StorageReference getFirestore() {
        setFirestore();
        return mFirestore;
    }

    public void writeNewUserPhoneNumber(String userId, String phoneNumber) {
        mDatabase.child("users").child(userId).child("phoneNumber").setValue(phoneNumber);
    }

    public void writeNewUserGender(String userId, Boolean gender) {
        mDatabase.child("users").child(userId).child("gender").setValue(gender);
    }


    public void writeNewUserName(String userId, String name) {
        mDatabase.child("users").child(userId).child("name").setValue(name);
    }

    public void writeNewUserAddress(String userId, String address) {
        mDatabase.child("users").child(userId).child("address").setValue(address);
    }

    public void writeNewEvents(String userId, ArrayList<String> selectedEvent) {
        mDatabase.child("users").child(userId).child("events").setValue(selectedEvent);
    }

    public void writeNewUserId(String userId) {
        mDatabase.child("users").child(userId).child("userId").setValue(userId);
    }

}