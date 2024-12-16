package com.pack1.quanlybangiaythethao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.pack1.models.ProducImage;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Staticstuffs {
    public static final String ADMIN_USER_NAME = "Admin";
    public static final String ADMIN_PASSWORD = "123";
    public static final String DEV_USER_NAME = "Dev";
    public static final String DEV_PASSWORD = "123";
    public static final String NGUOIDUNG = "CUSTOMER";
    public static final String NHANVIEN = "EMPLOYEE";
    public static final String QUANTRI = "ADMIN";
    public static final String MALE = "Nam";
    public static final String FEMALE = "Nữ";
    public static String DATE_FORMATTER = "dd-MM-yyyy";
    public static int defaultUserAvatar = R.drawable.user;
    public static String ORDER_STATUS_PENDIND = "Đang xử lý";
    public static String ORDER_STATUS_CONFIRMED = "Đã xử lý";
    public static String ORDER_STATUS_DELIVERING = "Đang giao";
    public static String ORDER_STATUS_DELIVERD = "Đã giao";
    public static String SP_ROLE = "ROLE";
    public static String SP_IS_SIGNIN = "isSignIn";
    public static String SP_CURRENT_USER_ID = "currentUserId";
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

    public static ArrayList<Bitmap> productImageListToBitmapList(ArrayList<ProducImage> pArr)
    {
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for (int i =0;i< pArr.size();i++)
        {
            bitmaps.add(pArr.get(i).getImage());
        }
        return  bitmaps;
    }


}
