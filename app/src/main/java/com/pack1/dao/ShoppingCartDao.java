package com.pack1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pack1.models.ShoppingCart;

import java.util.ArrayList;

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

    // Phương thức lấy danh sách product_id từ giỏ hàng của người dùng
    public ArrayList<Integer> getProductIdsByUserId(int userId) {
        ArrayList<Integer> productIds = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT product_id FROM Shopping_cart WHERE user_id = ?", new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int productId = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
                productIds.add(productId);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return productIds;
    }
}
