package com.pack1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pack1.models.ShoppingCart;

public class ShoppingCartDao {
    private DatabaseHelper dbHelper;

    public ShoppingCartDao(Context context)
    {
        dbHelper = new DatabaseHelper(context);
    }
    public long addShoppingCart(ShoppingCart shoppingCart)
    {
        SQLiteDatabase db =  dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", shoppingCart.getUserId());
        values.put("product_id", shoppingCart.getProductId());
        long rs = db.insert("Shopping_cart",null,values);
        db.close();
        return rs;
    }
    public boolean isShoppingCartExist(ShoppingCart shoppingCart) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "SELECT * FROM  Shopping_cart WHERE user_id = ? AND product_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(shoppingCart.getUserId()), String.valueOf(shoppingCart.getProductId())});
        boolean isExist = cursor.moveToFirst();
        cursor.close();
        db.close();
        return isExist;
    }
}
