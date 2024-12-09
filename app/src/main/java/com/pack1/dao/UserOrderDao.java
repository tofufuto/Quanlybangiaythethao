package com.pack1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.pack1.quanlybangiaythethao.Staticstuffs;
import com.pack1.models.UserOrder;

public class UserOrderDao {
    DatabaseHelper dbHelper;
    public UserOrderDao (Context context)
    {
        dbHelper = new DatabaseHelper(context);
    }
    public long  addUserOrder (UserOrder userOrder)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantity",userOrder.getQuantity());
        values.put("total_price",userOrder.getTotalPrice());
        values.put("status",userOrder.getStatus());
        values.put("dateorder", Staticstuffs.ConvertDatetoString( userOrder.getDateOrder()));
        values.put("user_id",userOrder.getUserId());
        values.put("product_id",userOrder.getProductId());
        long rs = db.insert("User_order",null,values);
        db.close();
        return rs;
    }
}
