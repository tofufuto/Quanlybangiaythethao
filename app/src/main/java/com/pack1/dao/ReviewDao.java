package com.pack1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pack1.models.Review;
import com.pack1.quanlybangiaythethao.Staticstuffs;

import java.util.ArrayList;

public class ReviewDao {
    DatabaseHelper dbhelper;

    public ReviewDao (Context context)
    {
        dbhelper = new DatabaseHelper(context);
    }
    public long addReview(Review review)
    {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("review_text",review.getReview());
        values.put("rating",review.getRating());
        values.put("reviewdate", Staticstuffs.ConvertDatetoString(review.getReviewDate()));
        values.put("user_id",review.getUserId());
        values.put("product_id",review.getProductId());
        long rs = db.insert("Review",null,values);
        db.close();
        return rs;
    }

    public ArrayList<Review> getAllReviewByProuctId(int productId)
    {   ArrayList<Review> reviews = new ArrayList<>();
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Review where product_id = ?", new String[]{""+productId});
        if(cursor.moveToNext())
        {
            do {
                 int reviewId = cursor.getInt(cursor.getColumnIndexOrThrow("review_id"));
                 String rev = cursor.getString(cursor.getColumnIndexOrThrow("review_text"));
                 float rating = cursor.getFloat(cursor.getColumnIndexOrThrow("rating"));
                 int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                 int pId = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
                 Review review = new Review(rev,rating,pId,userId,reviewId);
                 reviews.add(review);
            }while(cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return reviews;
    }
    public boolean hasRated(int userId, int productId) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            // Truy vấn kiểm tra sự tồn tại của đánh giá
            String query = "SELECT COUNT(*) FROM Review WHERE user_id = ? AND product_id = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId), String.valueOf(productId)});

            if (cursor != null && cursor.moveToFirst()) {
                int count = cursor.getInt(0); // Lấy số lượng dòng
                return count > 0; // Trả về true nếu có ít nhất một đánh giá
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return false; // Mặc định trả về false nếu không tìm thấy
    }

}
