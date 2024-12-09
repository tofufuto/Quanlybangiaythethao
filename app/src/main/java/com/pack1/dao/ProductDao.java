package com.pack1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.graphics.Bitmap;

import java.util.ArrayList;

import com.pack1.models.Product;
import com.pack1.quanlybangiaythethao.Staticstuffs;


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


    public ArrayList<Product> getProductsKw(String kw)
    {
        String[] stringArgs = new String[]{"%"+kw+"%"};
        ArrayList<Product> resultList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from Product where name LIKE ?",stringArgs);
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
                resultList.add(product);
            } while (cursor.moveToNext());
        cursor.close();
        dbHelper.close();
        return resultList;
    }
    public Product getProductName(String Name)
    {
        String[] stringArgs = new String[]{Name};
        ArrayList<Product> resultList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from Product where name = ?",stringArgs);
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
                resultList.add(product);
            } while (cursor.moveToNext());
        cursor.close();
        dbHelper.close();
        return resultList.get(0);
    }
    public int getProductIdByName(String pName)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select product_id from Product where name = ?",new String[]{pName});
        int id = -1;
        if(cursor.moveToNext())
            id = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
        cursor.close();
        db.close();
        return id;
    }
    public int updateProductyId(int pID,Product product)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",product.getName());
        values.put("quantity",product.getQuantity());
        values.put("size",product.getSize());
        values.put("color",product.getColor());
        values.put("gender",product.getGender());
        values.put("price",product.getPrice());
        values.put("brand",product.getBrand());
        values.put("description",product.getDescription());
        //values.put("rating",product.getRating());
        values.put("pd_image",Staticstuffs.bitmapToByteArray( product.getPdImage()));
        String whereClause = "product_id = ?";
        String[] whereArgs = {"" + pID};
        int rowAffected = db.update("Product",values,whereClause,whereArgs);
        return rowAffected;
    }
    public int deleteProductFromDatabaseByID(int pID)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = "product_id = ?";
        String[] whereArgs = {""+pID};
        int rowDeleted = db.delete("Product",whereClause,whereArgs);
        return rowDeleted;
    }
}
