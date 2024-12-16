package com.pack1.employee;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.pack1.quanlybangiaythethao.R;
import com.pack1.quanlybangiaythethao.Staticstuffs;
import com.pack1.dao.UserOrderDao;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderId, tvProductId, tvUserId, tvAddress, tvOrderDate;
    private ImageView ivProductImage;
    private RadioGroup rgOrderStatus;
    private Button btnUpdateStatus;
    private UserOrderDao userOrderDao; // Khai báo UserOrderDao

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // Khởi tạo UserOrderDao
        userOrderDao = new UserOrderDao(this);

        // Khởi tạo các view
        tvOrderId = findViewById(R.id.tvOrderId);
        tvProductId = findViewById(R.id.tvProductId);
        tvUserId = findViewById(R.id.tvUserId);
        tvAddress = findViewById(R.id.tvAddress);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        ivProductImage = findViewById(R.id.ivProductImage);
        rgOrderStatus = findViewById(R.id.rgOrderStatus);
        btnUpdateStatus = findViewById(R.id.btnUpdateStatus);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        int orderId = intent.getIntExtra("order_id", -1);  // Lấy ID đơn hàng
        int productId = intent.getIntExtra("product_id", -1);  // Lấy ID sản phẩm
        int userId = intent.getIntExtra("user_id", -1);  // Lấy ID người dùng
        String address = intent.getStringExtra("address");  // Lấy địa chỉ giao hàng
        String orderDate = intent.getStringExtra("order_date");  // Lấy ngày đặt hàng
        String productImageUrl = intent.getStringExtra("product_image_url");  // URL hình ảnh sản phẩm

        // Hiển thị các thông tin đơn hàng
        tvOrderId.setText("ID Đơn Hàng: " + orderId);
        tvProductId.setText("ID Sản Phẩm: " + productId);
        tvUserId.setText("ID Người Dùng: " + userId);
        tvAddress.setText("Địa chỉ: " + address);
        tvOrderDate.setText("Ngày đặt: " + orderDate);

        // Kiểm tra nếu có hình ảnh sản phẩm
        Bitmap productImage = Staticstuffs.convertImageUrlToBitmap(productImageUrl);
        if (productImage != null) {
            ivProductImage.setImageBitmap(productImage);
        } else {
            // Nếu không có hình ảnh, sử dụng hình ảnh mặc định
            ivProductImage.setImageResource(R.drawable.pt1);
        }

        // Cập nhật trạng thái đơn hàng
        btnUpdateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = rgOrderStatus.getCheckedRadioButtonId();
                String status = "";

                // Sử dụng if-else
                if (selectedId == R.id.rbPending) {
                    status = Staticstuffs.ORDER_STATUS_PENDIND;
                } else if (selectedId == R.id.rbConfirmed) {
                    status = Staticstuffs.ORDER_STATUS_CONFIRMED;
                } else if (selectedId == R.id.rbDelivering) {
                    status = Staticstuffs.ORDER_STATUS_DELIVERING;
                } else if (selectedId == R.id.rbDelivered) {
                    status = Staticstuffs.ORDER_STATUS_DELIVERD;
                }

                if (!status.isEmpty()) {
                    // Cập nhật trạng thái đơn hàng trong cơ sở dữ liệu bằng phương thức updateOrderStatus từ UserOrderDao
                    boolean isUpdated = userOrderDao.updateOrderStatus(orderId, status);

                    // Hiển thị thông báo kết quả cập nhật
                    if (isUpdated) {
                        Toast.makeText(OrderDetailActivity.this, "Trạng thái đã được cập nhật", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(OrderDetailActivity.this, "Cập nhật trạng thái thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
