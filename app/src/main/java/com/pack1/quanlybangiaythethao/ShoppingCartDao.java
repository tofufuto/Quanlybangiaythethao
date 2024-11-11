package com.pack1.quanlybangiaythethao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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
}
