package com.pack1.quanlybangiaythethao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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
        values.put("reviewdate",Staticstuffs.ConvertDatetoString(review.getReviewDate()));
        values.put("user_id",review.getUserId());
        values.put("product_id",review.getProductId());
        long rs = db.insert("Review",null,values);
        return rs;
    }
//    public Review findReview(int userId)
//    {
//
//    }
}
