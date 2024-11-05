package com.pack1.quanlybangiaythethao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ProductDao {
    private DatabaseHelper dbHelper;

    public ProductDao(Context context)
    {
        dbHelper = new DatabaseHelper(context);
    }
    public long addProduct(Product product) //-1 nếu không thêm dc
    {
        SQLiteDatabase db;
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",product.getName());
        values.put("quantity",product.getQuantity());
        values.put("size",product.getSize());
        values.put("color",product.getColor());
        values.put("gender",product.getGender());
        values.put("price",product.getPrice());
        values.put("brand",product.getBrand());
        values.put("description",product.getDescription());
        values.put("dateadded",Staticstuffs.ConvertDatetoString(product.getDateAdded()));
        values.put("pd_image",Staticstuffs.bitmapToByteArray(product.getPdImage()));
        values.put("rating",product.getRating());
        long rs = db.insert("Product",null,values);
        db.close();
        return rs;
    }
}
