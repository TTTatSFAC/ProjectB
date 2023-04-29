package com.example.projecta.model;

public class ChatMsg {
    private String username;
    private String message;
    private long time;
    private String uuid;
    private String imagePath;

    public ChatMsg() {}

    public ChatMsg(String username, String message, long time, String uuid) {
        this.username = username;
        this.message = message;
        this.time = time;
        this.uuid = uuid;
    }

    public ChatMsg(String username, String message, long time, String uuid, String imagePath) {
        this.username = username;
        this.message = message;
        this.time = time;
        this.uuid = uuid;
        this.imagePath = imagePath;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public long getTime() {
        return time;
    }

    public String getUUID() {
        return uuid;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
