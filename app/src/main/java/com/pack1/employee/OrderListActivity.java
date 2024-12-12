package com.pack1.employee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.pack1.dao.UserOrderDao;
import com.pack1.models.UserOrder;
import com.pack1.quanlybangiaythethao.R;
import custom_adapter.UserOrderAdapter;
import java.util.List;

public class OrderListActivity extends AppCompatActivity {

    private ListView lvOrders;
    private List<UserOrder> userOrderList;
    private UserOrderAdapter userOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        lvOrders = findViewById(R.id.lvOrders);

        // Lấy danh sách đơn hàng từ cơ sở dữ liệu
        UserOrderDao userOrderDao = new UserOrderDao(OrderListActivity.this);

        try {
            userOrderList = userOrderDao.getAllUserOrders(); // Lấy danh sách đơn hàng
            if (userOrderList == null || userOrderList.isEmpty()) {
                // Hiển thị thông báo khi không có đơn hàng
                Toast.makeText(this, "Không có đơn hàng nào", Toast.LENGTH_SHORT).show();
                // Hiển thị thông báo rõ ràng trên giao diện
                findViewById(R.id.tvNoOrdersMessage).setVisibility(View.VISIBLE); // Nếu bạn có TextView với ID tvNoOrdersMessage trong layout
            } else {
                // Thiết lập adapter cho ListView khi có dữ liệu
                userOrderAdapter = new UserOrderAdapter(OrderListActivity.this, userOrderList);
                lvOrders.setAdapter(userOrderAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Hiển thị thông báo lỗi khi có sự cố trong việc lấy dữ liệu
            Toast.makeText(this, "Lỗi khi tải dữ liệu đơn hàng", Toast.LENGTH_SHORT).show();
        }

        lvOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Khi chọn đơn hàng, mở màn hình chỉnh sửa trạng thái
                UserOrder selectedOrder = userOrderList.get(position);

                // Truyền thông tin đơn hàng sang Activity chỉnh sửa
                Intent intent = new Intent(OrderListActivity.this, EditOrderStatusActivity.class);
                intent.putExtra("orderId", selectedOrder.getOrderId());  // Truyền ID đơn hàng vào
                startActivity(intent);
            }
        });
    }
}
