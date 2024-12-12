package com.pack1.employee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.pack1.dao.UserOrderDao;
import com.pack1.models.UserOrder;
import com.pack1.quanlybangiaythethao.R;

public class EditOrderStatusActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName;
    private TextView productQuantity;
    private TextView customerName;
    private RadioButton rbProcessing, rbShipped, rbDelivered;
    private int orderId;
    private UserOrder selectedOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order_status);

        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productQuantity = findViewById(R.id.productQuantity);
        customerName = findViewById(R.id.customerName);
        rbProcessing = findViewById(R.id.rbProcessing);
        rbShipped = findViewById(R.id.rbShipped);
        rbDelivered = findViewById(R.id.rbDelivered);

        orderId = getIntent().getIntExtra("orderId", -1);

        // Lấy thông tin đơn hàng từ cơ sở dữ liệu
        UserOrderDao userOrderDao = new UserOrderDao(EditOrderStatusActivity.this);
        selectedOrder = userOrderDao.getUserOrderById(orderId);

        if (selectedOrder != null) {
            productImage.setImageBitmap(selectedOrder.getProductImage());  // Cập nhật hình ảnh sản phẩm
            productName.setText(selectedOrder.getProductName());
            productQuantity.setText(String.valueOf(selectedOrder.getQuantity()));
            customerName.setText(selectedOrder.getCustomerName());

            // Thiết lập trạng thái đơn hàng
            switch (selectedOrder.getStatus()) {
                case "Processing":
                    rbProcessing.setChecked(true);
                    break;
                case "Shipped":
                    rbShipped.setChecked(true);
                    break;
                case "Delivered":
                    rbDelivered.setChecked(true);
                    break;
            }
        }

        // Xử lý nút xác nhận
        findViewById(R.id.btnConfirmStatus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = "";
                if (rbProcessing.isChecked()) {
                    status = "Processing";
                } else if (rbShipped.isChecked()) {
                    status = "Shipped";
                } else if (rbDelivered.isChecked()) {
                    status = "Delivered";
                }

                if (!status.isEmpty()) {
                    selectedOrder.setStatus(status);

                    // Cập nhật trạng thái trong cơ sở dữ liệu
                    UserOrderDao userOrderDao = new UserOrderDao(EditOrderStatusActivity.this);
                    boolean isUpdated = userOrderDao.updateUserOrderStatus(selectedOrder);
                    if (isUpdated) {
                        Toast.makeText(EditOrderStatusActivity.this, "Trạng thái đơn hàng đã được cập nhật", Toast.LENGTH_SHORT).show();
                        finish();  // Quay lại màn hình trước
                    } else {
                        Toast.makeText(EditOrderStatusActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
