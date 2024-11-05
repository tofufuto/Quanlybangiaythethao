package com.pack1.quanlybangiaythethao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ProductImageDao {
    DatabaseHelper dbHelper ;

    public ProductImageDao(Context context)
    {
        dbHelper = new DatabaseHelper(context);
    }
    public long addProductImage(ProducImage producImage)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Image",Staticstuffs.bitmapToByteArray(producImage.getImage()));
        values.put("product_id",producImage.getProductId());
        long rs = db.insert("Product_images",null,values);
        db.close();
        return rs;// -1 nếu ko thêm dc
    }
}
