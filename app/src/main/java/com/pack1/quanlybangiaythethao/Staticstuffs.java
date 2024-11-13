package com.pack1.quanlybangiaythethao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Staticstuffs {
    public static String DATE_FORMATTER = "dd-MM-yyyy";
    public static String ConvertDatetoString(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMATTER);
        // Chuyển đổi Date thành String
        String formattedDate = sdf.format(date);
        return formattedDate;

    }
    public static Date ConvertStringtoDate(String str)
    {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMATTER);

        try {
            // Chuyển đổi chuỗi thành đối tượng Date
            Date date = format.parse(str);
            return date;
        }catch (Exception e){
            return new Date();
        }
    }
    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        if(bitmap == null)
            return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); // Có thể thay đổi định dạng và chất lượng
        return stream.toByteArray();
    }
    public static Bitmap byteArrayToBitmap(byte[] byteArray) {
        if(byteArray == null)
            return null;
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
    public static Bitmap convertImageUrlToBitmap(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Trả về null nếu có lỗi
        }
    }

    public static Bitmap uriToBitmap(Context context, Uri imageUri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
