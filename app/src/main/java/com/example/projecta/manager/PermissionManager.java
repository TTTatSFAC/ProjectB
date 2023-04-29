package com.example.projecta.manager;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionManager {

    public static final int PERMISSION_REQUEST_SETTING = 8000;
    public static final int PERMISSION_REQUEST_CALL_PHONE = 8001;
    public static final int PERMISSION_REQUEST_EXTERNAL_STORAGE_WRITE = 8002;
    public static final int PERMISSION_REQUEST_CAMERA = 8003;
    public static final int PERMISSION_REQUEST_LOCATION = 8003;

    PermissionManager() {

    }

    public static boolean isReadExternalStorageGranted(AppCompatActivity activity) {
        return ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestReadExternalStoragePermission(AppCompatActivity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[] { Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_EXTERNAL_STORAGE_WRITE);
    }

    public static boolean isWriteExternalStorageGranted(AppCompatActivity activity) {
        return ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestWriteExternalStoragePermission(AppCompatActivity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_EXTERNAL_STORAGE_WRITE);
    }

    public static boolean isCameraGranted(AppCompatActivity activity) {
        return ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestCameraPermission(AppCompatActivity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[] { Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CAMERA);
    }
    public static void requestMultiPermission(AppCompatActivity activity, String[] permissions,
                                       int requestCode) {
        ActivityCompat.requestPermissions(activity,
                permissions,
                requestCode);
    }
}
