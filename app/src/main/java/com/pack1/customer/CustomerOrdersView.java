package com.pack1.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pack1.dao.UserOrderDao;
import com.pack1.models.UserOrder;
import com.pack1.quanlybangiaythethao.R;

import java.util.ArrayList;

import custom_adapter.CustomerOrderAdapter;

public class CustomerOrdersView extends AppCompatActivity {

    ListView ordersDisplay;
    TextView nofiNoOrder;
    int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_orders_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ordersDisplay = findViewById(R.id.orderslist);
        nofiNoOrder = findViewById(R.id.noorder);

        Intent intent = this.getIntent();
        currentUserId = Integer.parseInt( intent.getStringExtra("currentUserId"));



        UserOrderDao userOrderDao = new UserOrderDao(this);
        ArrayList<UserOrder> userOrdersArrayList = userOrderDao.getOrdersByUserId(currentUserId);
        if(userOrdersArrayList.isEmpty()){
            nofiNoOrder.setEnabled(true);
        }
        else {
            CustomerOrderAdapter adapter = new CustomerOrderAdapter(this, userOrdersArrayList, this.getLayoutInflater());
            ordersDisplay.setAdapter(adapter);
            nofiNoOrder.setEnabled(false);
        }
    }
}