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
public Product getProductName(String Name)// lấy 1 product ra bằng tên
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

    public Product getProductById(int productId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Product WHERE product_id = ?", new String[]{String.valueOf(productId)});

        if (cursor != null && cursor.moveToFirst()) {
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
            cursor.close();
            db.close();
            return product;
        }

        cursor.close();
        db.close();
        return null;
    }
    public ArrayList<Product> getProductsByGender(String gender) {
        ArrayList<Product> products = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "SELECT * FROM Product WHERE gender = ?";
        Cursor cursor = db.rawQuery(query, new String[]{gender});
        while (cursor.moveToNext()) {
            int productId = cursor.getInt(0);
            String name = cursor.getString(1);
            int quantity = cursor.getInt(2);
            String size = cursor.getString(3);
            String color = cursor.getString(4);
            byte[] pdImageBytes = cursor.getBlob(10);
            Bitmap pdImage = Staticstuffs.byteArrayToBitmap (pdImageBytes);
            float rating = Float.parseFloat(cursor.getString(11));
            float price = (float) cursor.getDouble(6);
            Product product = new Product(name, quantity, size, color, gender, price, cursor.getString(7), cursor.getString(8), rating, pdImage, productId);
            products.add(product);
        }
        cursor.close();
        db.close();
        return products;
    }
    public boolean updateProductRating(int productId, float newRating) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Mở cơ sở dữ liệu để ghi
        ContentValues values = new ContentValues();
        values.put("rating", String.valueOf(newRating)); // Chuyển rating thành chuỗi

        // Cập nhật dòng dữ liệu có `product_id` tương ứng
        int rowsAffected = db.update("Product", values, "product_id = ?", new String[]{String.valueOf(productId)});

        // Đóng cơ sở dữ liệu sau khi thực hiện xong
        db.close();

        // Trả về true nếu cập nhật thành công (có ít nhất 1 dòng bị ảnh hưởng)
        return rowsAffected > 0;
    }


}
