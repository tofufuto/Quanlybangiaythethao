package com.pack1.dao;

import static com.pack1.quanlybangiaythethao.Staticstuffs.ORDER_STATUS_DELIVERD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pack1.quanlybangiaythethao.Staticstuffs;
import com.pack1.models.UserOrder;

import java.util.ArrayList;
import java.util.List;

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
    public ArrayList<UserOrder> getOrdersByUserId(int userId) {
        ArrayList<UserOrder> orders = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM User_order WHERE user_id = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

            if (cursor.moveToFirst()) {
                do {
                    int orderId = cursor.getInt(cursor.getColumnIndexOrThrow("order_id"));
                    int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                    float totalPrice = cursor.getFloat(cursor.getColumnIndexOrThrow("total_price"));
                    String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                    int userID = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                    int productId = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
                    String shipAddress = cursor.getString(cursor.getColumnIndexOrThrow("ship_address"));

                    UserOrder order = new UserOrder(quantity, totalPrice, status, productId, userID, shipAddress, orderId);
                    orders.add(order);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return orders;
    }
    public String getOrderDateById(int orderId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String orderDate = null;
        Cursor cursor = null;

        try {
            String query = "SELECT dateorder FROM User_order WHERE order_id = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(orderId)});
            if (cursor.moveToFirst()) {
                orderDate = cursor.getString(cursor.getColumnIndexOrThrow("dateorder"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return orderDate;
    }
    public ArrayList<UserOrder> getOrdersByMonthAndYear(int month, int year) {
        ArrayList<UserOrder> orders = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Câu truy vấn sử dụng substr và điều kiện lọc trạng thái
        String rawQuery = "SELECT * FROM User_order " +
                "WHERE substr(dateorder, 4, 2) = ? " +
                "AND substr(dateorder, 7, 4) = ? " +
                "AND status = ?";

        // Tham số thay thế
        String[] whereArgs = {
                String.format("%02d", month),  // Định dạng tháng thành 2 chữ số (01, 02,..., 12)
                String.valueOf(year),          // Năm dưới dạng chuỗi
                ORDER_STATUS_DELIVERD          // Lọc theo trạng thái đơn hàng
        };

        // Thực thi truy vấn
        Cursor cursor = db.rawQuery(rawQuery, whereArgs);

        // Xử lý kết quả trả về
        if (cursor.moveToFirst()) {
            do {
                UserOrder order = new UserOrder(
                        cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                        cursor.getFloat(cursor.getColumnIndexOrThrow("total_price")),
                        cursor.getString(cursor.getColumnIndexOrThrow("status")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("product_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("ship_address")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("order_id"))
                );
                orders.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close(); // Đóng Cursor
        db.close();     // Đóng cơ sở dữ liệu
        return orders;
    }
    public ArrayList<UserOrder> getAllOrders() {
        ArrayList<UserOrder> orders = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Truy vấn tất cả các cột từ bảng User_order
        String query = "SELECT * FROM User_order";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // Khởi tạo đối tượng UserOrder từ dữ liệu trong Cursor
                UserOrder order = new UserOrder(
                        cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                        cursor.getFloat(cursor.getColumnIndexOrThrow("total_price")),
                        cursor.getString(cursor.getColumnIndexOrThrow("status")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("product_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("ship_address")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("order_id"))
                );

                // Thêm vào danh sách
                orders.add(order);
            } while (cursor.moveToNext());
        }

        // Đóng Cursor và Database
        cursor.close();
        db.close();

        return orders;
    }
    public UserOrder getOrderById(int orderId) {
        UserOrder userOrder = null; // Biến để lưu thông tin đơn hàng
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Câu truy vấn
        String query = "SELECT * FROM User_order WHERE order_id = ?";
        String[] selectionArgs = {String.valueOf(orderId)};

        // Thực thi truy vấn
        Cursor cursor = db.rawQuery(query, selectionArgs);

        // Kiểm tra kết quả
        if (cursor.moveToFirst()) {
            userOrder = new UserOrder(
                    cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                    cursor.getFloat(cursor.getColumnIndexOrThrow("total_price")),
                    cursor.getString(cursor.getColumnIndexOrThrow("status")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("product_id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("ship_address")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("order_id"))
            );
        }

        // Đóng Cursor và cơ sở dữ liệu
        cursor.close();
        db.close();

        return userOrder; // Trả về thông tin đơn hàng
    }
    public boolean updateOrderStatus(int orderId, String newStatus) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Đặt giá trị mới cho status
        values.put("status", newStatus);

        // Điều kiện cập nhật
        String whereClause = "order_id = ?";
        String[] whereArgs = {String.valueOf(orderId)};

        // Cập nhật cơ sở dữ liệu
        int rowsAffected = db.update("User_order", values, whereClause, whereArgs);

        // Đóng kết nối
        db.close();

        // Kiểm tra kết quả
        return rowsAffected > 0; // Trả về true nếu có ít nhất một dòng được cập nhật
    }

}
