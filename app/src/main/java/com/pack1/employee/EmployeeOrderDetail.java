package com.pack1.employee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pack1.dao.ProductDao;
import com.pack1.dao.UserDao;
import com.pack1.dao.UserOrderDao;
import com.pack1.models.Product;
import com.pack1.models.User;
import com.pack1.models.UserOrder;
import com.pack1.quanlybangiaythethao.R;
import com.pack1.quanlybangiaythethao.Staticstuffs;

public class EmployeeOrderDetail extends AppCompatActivity {

    RadioButton pendind,confirmed,delivering,delivered;
    Button update;
    TextView productName,name,sdt;
    TextView status;
    ImageView imageView;
    int orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_order_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        name = findViewById(R.id.name);
        sdt = findViewById(R.id.sdt);
        imageView = findViewById(R.id.productimage);
        pendind = findViewById(R.id.pending);
        confirmed = findViewById(R.id.confirmed);
        delivering = findViewById(R.id.delivering);
        delivered = findViewById(R.id.delivered);
        update = findViewById(R.id.button);
        status = findViewById(R.id.status);
        productName = findViewById(R.id.pname);
        Intent intent = this.getIntent();
        orderId = Integer.parseInt( intent.getStringExtra("orderId"));
        UserOrderDao userOrderDao = new UserOrderDao(this);
        UserOrder userOrder = userOrderDao.getOrderById(orderId);
        ProductDao productDao = new ProductDao(this);
        Product product = productDao.getProductById(userOrder.getProductId());
        UserDao userDao = new UserDao(this);
        User user = userDao.getUserById(userOrder.getUserId());

        imageView.setImageBitmap(product.getPdImage());
        productName.setText(product.getName());

        status.setText(userOrder.getStatus().toString());

        name.setText("Tên khách hàng : "+ user.getFname()+" "+user.getLname());
        sdt.setText("SĐT : "+user.getNumbers());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sta = Staticstuffs.ORDER_STATUS_PENDIND;
                if(pendind.isChecked())
                     sta = Staticstuffs.ORDER_STATUS_PENDIND;
                if (confirmed.isChecked())
                    sta = Staticstuffs.ORDER_STATUS_CONFIRMED;
                if (delivering.isChecked())
                    sta = Staticstuffs.ORDER_STATUS_DELIVERING;
                if (delivered.isChecked())
                    sta = Staticstuffs.ORDER_STATUS_DELIVERD;
                userOrderDao.updateOrderStatus(orderId,sta);
                onBackPressed();
            }
        });

    }
}