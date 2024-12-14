package com.pack1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pack1.quanlybangiaythethao.Staticstuffs;
import com.pack1.models.UserOrder;

public class UserOrderDao {
    DatabaseHelper dbHelper;
    public UserOrderDao (Context context)
    {
        dbHelper = new DatabaseHelper(context);
    }
    public long  addUserOrder (UserOrder userOrder)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantity",userOrder.getQuantity());
        values.put("total_price",userOrder.getTotalPrice());
        values.put("status",userOrder.getStatus());
        values.put("dateorder", Staticstuffs.ConvertDatetoString( userOrder.getDateOrder()));
        values.put("user_id",userOrder.getUserId());
        values.put("product_id",userOrder.getProductId());
        values.put("ship_address",userOrder.getShipAdress());
        long rs = db.insert("User_order",null,values);
        db.close();
        return rs;
    }
    public boolean isOrderExist(int userId, int productId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            // Truy vấn kiểm tra sự tồn tại của order
            String query = "SELECT COUNT(*) FROM User_order WHERE user_id = ? AND product_id = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId), String.valueOf(productId)});

            if (cursor != null && cursor.moveToFirst()) {
                int count = cursor.getInt(0); // Lấy kết quả đầu tiên (số lượng dòng)
                return count > 0; // Trả về true nếu có ít nhất 1 bản ghi
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
