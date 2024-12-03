package com.pack1.quanlybangiaythethao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import java.util.ArrayList;

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
    public ArrayList<ProducImage> getAllProductImagesFromID(int id)
    {
        ArrayList<ProducImage> producImagesArray = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Product_images where product_id = ?",new String[]{""+id});
        if(cursor.moveToNext())
            do{
                Bitmap Image = Staticstuffs.byteArrayToBitmap(cursor.getBlob(cursor.getColumnIndexOrThrow("Image")));
                int pID = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
                int imgId = cursor.getInt(cursor.getColumnIndexOrThrow("img_id"));
                ProducImage producImage = new ProducImage(Image,pID,imgId);
                producImagesArray.add(producImage);
            }while(cursor.moveToNext());
        cursor.close();
        db.close();
        return producImagesArray;
    }

    public int deleteAllProductImageByProductId(int id)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = "product_id = ?";
        String[] whereArgs = {""+id};
        int rowDeleted = db.delete("Product_images" ,whereClause,whereArgs);
        return rowDeleted;
    }
}
