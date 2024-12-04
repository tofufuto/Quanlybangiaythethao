package com.pack1.quanlybangiaythethao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

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
    public User getUserById(int userId)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from User where user_id = ?",new String[]{""+userId});
        int uId ;
        String userName ;
        String password ;
        String fname ;
        String lname ;
        Date birth ;
        String role ;
        String gender ;
        String numbers ;
        String gmail ;
        Bitmap avatar;
        if(cursor.moveToNext())
        {
              uId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
              userName = cursor.getString(cursor.getColumnIndexOrThrow("username"));
              password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
              fname = cursor.getString(cursor.getColumnIndexOrThrow("fname"));
              lname = cursor.getString(cursor.getColumnIndexOrThrow("lname"));
              birth = Staticstuffs.ConvertStringtoDate( cursor.getString(cursor.getColumnIndexOrThrow("birth")));
              role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
              gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
              numbers = cursor.getString(cursor.getColumnIndexOrThrow("numbers"));
              gmail = cursor.getString(cursor.getColumnIndexOrThrow("gmail"));
              avatar = Staticstuffs.byteArrayToBitmap( cursor.getBlob(cursor.getColumnIndexOrThrow("avatar")));
              db.close();
              cursor.close();
              return new User(userName,password,fname,lname,birth,role,gender,numbers,gmail,avatar,uId);
        }
        db.close();
        cursor.close();
        return null;
    }
    public ArrayList<User> getAllEmployees() {
        ArrayList<User> empList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from User", null);
        if (cursor.moveToNext()) {
            do {
                int user_id = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                String fname = cursor.getString(cursor.getColumnIndexOrThrow("fname"));
                String lname = cursor.getString(cursor.getColumnIndexOrThrow("lname"));
                Date birth = Staticstuffs.ConvertStringtoDate(cursor.getString(cursor.getColumnIndexOrThrow("birth")));
                String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
                String gmail = cursor.getString(cursor.getColumnIndexOrThrow("gmail"));
                String numbers = cursor.getString(cursor.getColumnIndexOrThrow("numbers"));
                Bitmap avatar = Staticstuffs.byteArrayToBitmap(cursor.getBlob(cursor.getColumnIndexOrThrow("avatar")));
                User employee = new User(username, password, fname, lname, birth, role, gender, numbers, gmail, avatar, user_id);
                empList.add(employee);
            } while (cursor.moveToNext()) ;
        }
        cursor.close();
        db.close();
        return empList;
    }
}
