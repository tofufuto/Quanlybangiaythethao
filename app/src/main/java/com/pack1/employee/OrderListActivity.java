package com.pack1.employee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.pack1.dao.UserOrderDao;
import com.pack1.models.UserOrder;
import com.pack1.quanlybangiaythethao.R;


import java.util.ArrayList;
import java.util.Date;

import custom_adapter.OrderAdapter;

public class OrderListActivity extends AppCompatActivity {

    private ListView listViewOrders;
    private ArrayList<UserOrder> userOrders;
    private UserOrderDao userOrderDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        // Khởi tạo Dao
        userOrderDao = new UserOrderDao(this);

        // Lấy currentUserId từ Intent
        int currentUserId = getIntent().getIntExtra("currentUserId", -1);

        // Lấy danh sách đơn hàng theo currentUserId
        userOrders = userOrderDao.getOrdersByUserId(currentUserId);

        // Tạo danh sách sản phẩm để hiển thị (hoặc có thể lấy thêm thông tin sản phẩm ở đây)
        ArrayList<String> orderDescriptions = new ArrayList<>();
        for (UserOrder order : userOrders) {
            orderDescriptions.add("Đơn hàng #" + order.getOrderId() + " - Trạng thái: " + order.getStatus());
        }

        // Khởi tạo OrderAdapter để hiển thị dữ liệu
        OrderAdapter orderAdapter = new OrderAdapter(this, userOrders);
        listViewOrders = findViewById(R.id.listViewOrders);
        listViewOrders.setAdapter(orderAdapter);


        // sự kiện chọn đơn hàng
        listViewOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {

                Intent intent = new Intent(OrderListActivity.this, OrderDetailActivity.class);
                UserOrder selectedOrder = userOrders.get(position);
                int orderId = selectedOrder.getOrderId();
                int productId = selectedOrder.getProductId();  // Lấy productId từ selectedOrder
                int userId = selectedOrder.getUserId();  // Lấy userId từ selectedOrder
                String address = selectedOrder.getShipAdress();  // Lấy address từ selectedOrder
                Date orderDate = selectedOrder.getDateOrder();  // Lấy orderDate từ selectedOrder
                String productImageUrl = selectedOrder.getImageUrl();  // Lấy productImageUrl từ selectedOrder
                double totalAmount = selectedOrder.getTotalPrice();  // Lấy tổng tiền từ selectedOrder
                // Truyền dữ liệu
                intent.putExtra("order_id", orderId);
                intent.putExtra("product_id", productId);
                intent.putExtra("user_id", userId);
                intent.putExtra("address", address);
                intent.putExtra("order_date", orderDate);
                intent.putExtra("product_image_url", productImageUrl);
                intent.putExtra("total amount",totalAmount);
                // Khởi chạy Activity
                startActivity(intent);

            }
        });
    }
}
