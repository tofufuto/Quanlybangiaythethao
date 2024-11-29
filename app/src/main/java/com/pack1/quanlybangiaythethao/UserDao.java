package com.pack1.quanlybangiaythethao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

//Dao data access object
public class UserDao {
    private DatabaseHelper dbHelper;
    public UserDao(Context context)
    {
        dbHelper = new DatabaseHelper(context);
    }

    public long addUser(User user)//-1 nếu không thêm dc
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
        values.put("avatar", Staticstuffs.bitmapToByteArray( user.getAvatar()));
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
}
