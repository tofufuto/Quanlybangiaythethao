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

    public boolean isShoppingCartExist(ShoppingCart shoppingCart) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "SELECT * FROM  Shopping_cart WHERE user_id = ? AND product_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(shoppingCart.getUserId()), String.valueOf(shoppingCart.getProductId())});
        boolean isExist = cursor.moveToFirst();
        cursor.close();
        db.close();
        return isExist;
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
    public int getShoppingCartID(int productID) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "SELECT cart_id FROM Shopping_cart WHERE product_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(productID)});
        int shoppingCartID = -1;
        if (cursor.moveToFirst()) {
            shoppingCartID = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return shoppingCartID;
    }
    public void deleteCart(int cartID) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "DELETE FROM Shopping_cart WHERE cart_id = ?";
        db.execSQL(query, new String[]{String.valueOf(cartID)});
        db.close();
    }
}
