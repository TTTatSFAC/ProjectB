package com.example.projecta.manager;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PhotoManager {

    PhotoManager() {

    }

    public static String getFileName() {
        Date d = Calendar.getInstance().getTime();
        String ts = new SimpleDateFormat("yyyyMMddHHmmss-SSS").format(d);
        String fileName = ts.concat(".jpg");

        return fileName;
    }

    public static File getFileDir() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File dir = new File(filepath + "/ElderGathering/uploads");
        dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        // 檢查資料夾是否存在
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }

    public static int getDeviceRotation(Activity activity){
        int degree = 0;
        degree = activity.getWindowManager().getDefaultDisplay().getRotation();
        return degree;
    }

    // 取得圖片旋轉的角度
    public static int getRotationDegree(String path) {
        int degree = 0;

        try {
            //使用exif類取得或設定圖片的細部參數，此處只處理旋轉角度
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return degree;
        }
        return degree;
    }
    // 使用指定的角度旋轉圖片
    public static Bitmap rotateBitmap(Bitmap bitmap, int degree) {
        Bitmap returnBitmap = null;

        //根據角度生成旋轉矩陣，並設定取得的需旋轉角度
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        //創建一個有角度的圖，寬高與傳入的圖一樣
        try {
            returnBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return returnBitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }

    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    // bitmap 壓縮並寫入檔案
    public static File bitmapToFile(File file, String cameraFileName,  Bitmap bitmap) {
        File uploadFile = null;

        Matrix matrix = new Matrix();
        matrix.setScale(0.5f, 0.5f);
        bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, os);
        byte[] bitmapData = os.toByteArray();

        try {
            uploadFile = new File(file, cameraFileName);

            FileOutputStream fos = new FileOutputStream(uploadFile);
            fos.write(bitmapData);
            fos.flush();
            fos.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return uploadFile;
    }

    //壓縮來源圖片
    public static File compressUploadPhoto(File tempFile, String cameraPath, String cameraFileName) throws IOException {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        options.inSampleSize = calculateInSampleSize(options, 480, 800);//解析度
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(cameraPath, options);

        int degree = getRotationDegree(cameraPath);
        bitmap = rotateBitmap(bitmap, degree);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        byte[] bitmapData = byteArrayOutputStream.toByteArray();

        File uploadFile = new File(tempFile, cameraFileName);
        uploadFile.createNewFile();

        FileOutputStream fos = new FileOutputStream(uploadFile);
        fos.write(bitmapData);
        fos.flush();
        fos.close();

        return uploadFile;
    }
}
