package com.pack1.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

    public boolean updateUserOrderStatus(UserOrder userOrder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", userOrder.getStatus());

        int rowsUpdated = db.update("User_order", values, "order_id = ?", new String[]{String.valueOf(userOrder.getOrderId())});
        db.close();
        return rowsUpdated > 0;
    }

    // Thêm phương thức getUserOrderById
    public UserOrder getUserOrderById(int orderId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "User_order",  // Tên bảng
                new String[] {
                        "order_id", "quantity", "total_price", "status", "dateorder",
                        "user_id", "product_id", "ship_address", "product_name", "product_image", "customer_name"
                },
                "order_id = ?",  // Điều kiện tìm kiếm
                new String[] { String.valueOf(orderId) },
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            // Lấy thông tin từ cursor
            int productId = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
            String productName = cursor.getString(cursor.getColumnIndexOrThrow("product_name"));
            Bitmap productImage = getBitmapFromCursor(cursor);  // Hàm lấy hình ảnh từ BLOB
            String customerName = cursor.getString(cursor.getColumnIndexOrThrow("customer_name"));

            UserOrder userOrder = new UserOrder(
                    cursor.getInt(cursor.getColumnIndexOrThrow("order_id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                    cursor.getFloat(cursor.getColumnIndexOrThrow("total_price")),
                    cursor.getString(cursor.getColumnIndexOrThrow("status")),
                    new Date(cursor.getLong(cursor.getColumnIndexOrThrow("dateorder"))),  // Chuyển đổi long thành Date
                    cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                    productId,
                    cursor.getString(cursor.getColumnIndexOrThrow("ship_address")),
                    productName,
                    productImage,
                    customerName
            );

            cursor.close();
            db.close();
            return userOrder;
        } else {
            db.close();
            return null;
        }
    }

    // Phương thức để lấy hình ảnh từ CSDL (BLOB)
    private Bitmap getBitmapFromCursor(Cursor cursor) {
        byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("product_image"));
        if (imageBytes != null) {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } else {
            return null;
        }
    }

    //
    public List<UserOrder> getAllUserOrders() {
        List<UserOrder> userOrders = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM User_order", null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int orderId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                float totalPrice = cursor.getFloat(cursor.getColumnIndexOrThrow("total_price"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                String dateOrderString = cursor.getString(cursor.getColumnIndexOrThrow("dateorder"));
                Date dateOrder = Staticstuffs.ConvertStringtoDate(dateOrderString);
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                int productId = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
                String shipAddress = cursor.getString(cursor.getColumnIndexOrThrow("ship_address"));
                String productName = cursor.getString(cursor.getColumnIndexOrThrow("product_name"));
                String customerName = cursor.getString(cursor.getColumnIndexOrThrow("customer_name"));
                // Nếu có hình ảnh, chuyển đổi từ byte[] sang Bitmap
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("image_column_name"));
                Bitmap productImage = Staticstuffs.byteArrayToBitmap(imageBytes);

                // Tạo đối tượng UserOrder và thêm vào danh sách
                UserOrder userOrder = new UserOrder(orderId, quantity, totalPrice, status, dateOrder, userId, productId, shipAddress, productName, productImage, customerName);
                userOrders.add(userOrder);
            }
            cursor.close();
        }
        db.close();
        return userOrders;
    }

}
