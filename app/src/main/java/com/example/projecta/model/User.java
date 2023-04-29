package com.example.projecta.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.UUID;

public class User {
    String userId;
    String phoneNumber, name, address;
    Boolean gender;
    ArrayList<String> events;

    public User() {
    }

    public void generateUUID() {
        userId = UUID.randomUUID().toString();
    }

    public User(String userId, String phoneNumber, String name, Boolean gender, String address, ArrayList<String> eventList) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.gender = gender;
        this.address = address;
        this.events = eventList;
    }

    @NonNull
    @Override
    public String toString() {
        String mString = super.toString();
        mString = mString.concat("\nuserId = " + userId)
                .concat("\nphoneNumber = " + phoneNumber)
                .concat("\nname = " + name)
                .concat("\ngender = " + (gender ? "male" : "female"))
                .concat("\naddress = " + address);
        if (events != null) {
            mString = mString.concat("\nevents =");
            for (String event : events) {
                mString = mString.concat(" " + event);
            }
        }
        return mString;
    }

    public String getUserId() {
        return userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    public Boolean getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public ArrayList<String> getEvents() {
        return events;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEvents(ArrayList<String> events) {
        this.events = events;
    }

}
