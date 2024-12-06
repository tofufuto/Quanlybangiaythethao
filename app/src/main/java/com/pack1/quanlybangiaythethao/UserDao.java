package com.pack1.quanlybangiaythethao;

import static com.pack1.quanlybangiaythethao.DatabaseHelper.USER_TABLE;
import static com.pack1.quanlybangiaythethao.Staticstuffs.bitmapToByteArray;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

//Dao data access object
public class UserDao {

    private static DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public UserDao(SQLiteDatabase db) {
        this.db = db;
    }
    public UserDao(Context context)
    {
        dbHelper = new DatabaseHelper(context);
    }

    public static long addUser(User user)//-1 nếu không thêm dc
    {
        //try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("username",user.getUserName());
            values.put("password",user.getPassword());
            values.put("fname", user.getFname());
            values.put("lname", user.getLname());
            values.put("birth", Staticstuffs.ConvertDatetoString(user.getBirth()));
            values.put("role", user.getRole());
            values.put("gender", user.getGender());
            values.put("dateadded", Staticstuffs.ConvertDatetoString(user.getDateAdded()));
            values.put("avatar", bitmapToByteArray(user.getAvatar()));
            values.put("numbers",user.getNumbers());
            values.put("gmail",user.getGmail());
            long rs = db.insert("User", null, values);
            db.close();
            return rs;
        //}catch (Exception e)
        //{
            //
        //}
       // return -1;
    }
    public static boolean checkUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE " +
                username + password + "=?", new String[]{username, password});
        return cursor.getCount() > 0;// 1 neu hop le
    }
//



}

