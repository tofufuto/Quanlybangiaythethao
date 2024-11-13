package com.pack1.quanlybangiaythethao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductDao {
    private final DatabaseHelper dbHelper;

    public ProductDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long addProduct(Product product) //-1 nếu không thêm dc
    {
        SQLiteDatabase db;
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", product.getName());
        values.put("quantity", product.getQuantity());
        values.put("size", product.getSize());
        values.put("color", product.getColor());
        values.put("gender", product.getGender());
        values.put("price", product.getPrice());
        values.put("brand", product.getBrand());
        values.put("description", product.getDescription());
        values.put("dateadded", Staticstuffs.ConvertDatetoString(product.getDateAdded()));
        values.put("pd_image", Staticstuffs.bitmapToByteArray(product.getPdImage()));
        values.put("rating", product.getRating());
        long rs = db.insert("Product", null, values);
        db.close();
        return rs;
    }

    public ArrayList<Product> getAllProduct() {
        ArrayList<Product> productList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Product", null);
        if (cursor != null && cursor.moveToNext())
            do {
                int productId = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                String size = cursor.getString(cursor.getColumnIndexOrThrow("size"));
                String color = cursor.getString(cursor.getColumnIndexOrThrow("color"));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
                float price = cursor.getFloat(cursor.getColumnIndexOrThrow("price"));
                String brand = cursor.getString(cursor.getColumnIndexOrThrow("brand"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                float rating = cursor.getFloat(cursor.getColumnIndexOrThrow("rating"));
                Bitmap pdImage = Staticstuffs.byteArrayToBitmap(cursor.getBlob(cursor.getColumnIndexOrThrow("pd_image")));

                Product product = new Product(name, quantity, size, color, gender, price, brand, description, rating, pdImage, productId);
                productList.add(product);
            } while (cursor.moveToNext());
        cursor.close();
        dbHelper.close();
        return productList;
    }
}
