package com.pack1.employee;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pack1.admin.AdministratorProductSelect;
import com.pack1.admin.ProductDetailAdmin;
import com.pack1.dao.UserDao;
import com.pack1.dao.UserOrderDao;
import com.pack1.models.User;
import com.pack1.models.UserOrder;
import com.pack1.quanlybangiaythethao.R;
import com.pack1.quanlybangiaythethao.Staticstuffs;

import org.w3c.dom.Text;

import java.util.ArrayList;

import custom_adapter.CustomerOrderAdapter;
import custom_adapter.EmployeeOrderAdapter;

public class EmployeeOrderList extends AppCompatActivity {

    ListView orderListDisplay ;
    Button findAll,pendingBtn,confirmedBtn,deliveringBtn,deliveredBtn;
    ImageButton searchBtn;
    EditText searchField;
    ArrayList<UserOrder> orderList ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_order_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        orderListDisplay = findViewById(R.id.order_list_display);
        findAll = findViewById(R.id.findAll);
        pendingBtn = findViewById(R.id.pendingBtn);
        confirmedBtn = findViewById(R.id.confirmedBtn);
        deliveringBtn = findViewById(R.id.deliveringBtn);
        deliveredBtn = findViewById(R.id.deliveredBtn);
        searchBtn = findViewById(R.id.search);
        searchField = findViewById(R.id.searchField);

        /////////////////////

        loadOrder();
        EmployeeOrderAdapter adapter = new EmployeeOrderAdapter(EmployeeOrderList.this,orderList,EmployeeOrderList.this.getLayoutInflater());
        orderListDisplay.setAdapter(adapter);
        ////////////////////

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(searchField.getText().toString().equals(""))
                {
                    loadOrder();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(searchField.getText().toString().equals(""))
                {
                    loadOrder();
                }

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!searchField.equals(""))
                    findOrderByUserPhone();
            }
        });

        findAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmployeeOrderAdapter adapter = new EmployeeOrderAdapter(EmployeeOrderList.this,filterOrderBySatus(null),EmployeeOrderList.this.getLayoutInflater());
                orderListDisplay.setAdapter(adapter);
            }
        });
        pendingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmployeeOrderAdapter adapter = new EmployeeOrderAdapter(EmployeeOrderList.this,filterOrderBySatus(Staticstuffs.ORDER_STATUS_PENDIND),EmployeeOrderList.this.getLayoutInflater());
                orderListDisplay.setAdapter(adapter);
            }
        });
        confirmedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmployeeOrderAdapter adapter = new EmployeeOrderAdapter(EmployeeOrderList.this,filterOrderBySatus(Staticstuffs.ORDER_STATUS_CONFIRMED),EmployeeOrderList.this.getLayoutInflater());
                orderListDisplay.setAdapter(adapter);
            }
        });
        deliveringBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmployeeOrderAdapter adapter = new EmployeeOrderAdapter(EmployeeOrderList.this,filterOrderBySatus(Staticstuffs.ORDER_STATUS_DELIVERING),EmployeeOrderList.this.getLayoutInflater());
                orderListDisplay.setAdapter(adapter);
            }
        });
        deliveredBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmployeeOrderAdapter adapter = new EmployeeOrderAdapter(EmployeeOrderList.this,filterOrderBySatus(Staticstuffs.ORDER_STATUS_DELIVERD),EmployeeOrderList.this.getLayoutInflater());
                orderListDisplay.setAdapter(adapter);
            }
        });

        orderListDisplay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView orderIdTV = view.findViewById(R.id.id);
               Intent intent = new Intent(EmployeeOrderList.this,  EmployeeOrderDetail.class);
               intent.putExtra("orderId",orderIdTV.getText().toString());
               startActivity(intent);
            }
        });
    }
    private void findOrderByUserPhone()
    {
        UserDao userDao = new UserDao(EmployeeOrderList.this);
        int userIds = userDao.getUserIdByPhoneNumber(searchField.getText().toString());
        ArrayList<UserOrder> userOrders = new ArrayList<UserOrder>();
        for(UserOrder u : orderList)
        {
            if (u.getUserId() == userIds)
            {
                userOrders.add(u);
            }
        }
        EmployeeOrderAdapter adapter = new EmployeeOrderAdapter(EmployeeOrderList.this,userOrders,EmployeeOrderList.this.getLayoutInflater());
        orderListDisplay.setAdapter(adapter);
    }
    private void loadOrder()
    {
            UserOrderDao userOrderDao = new UserOrderDao(this);
            orderList = userOrderDao.getAllOrders();
    }
    private ArrayList<UserOrder> filterOrderBySatus(String orderStatus)
    {
        ArrayList<UserOrder> userOrders = new ArrayList<>();
        if (orderStatus == null)
            return orderList;
        if (orderStatus.equals(Staticstuffs.ORDER_STATUS_PENDIND))
        {
            for(UserOrder u : orderList)
            {
                if(u.getStatus().equals(Staticstuffs.ORDER_STATUS_PENDIND))
                    userOrders.add(u);
            }
            return userOrders;
        }
        if (orderStatus.equals(Staticstuffs.ORDER_STATUS_CONFIRMED))
        {
            for(UserOrder u : orderList)
            {
                if(u.getStatus().equals(Staticstuffs.ORDER_STATUS_CONFIRMED))
                    userOrders.add(u);
            }
            return userOrders;
        }
        if (orderStatus.equals(Staticstuffs.ORDER_STATUS_DELIVERING))
        {
            for(UserOrder u : orderList)
            {
                if(u.getStatus().equals(Staticstuffs.ORDER_STATUS_DELIVERING))
                    userOrders.add(u);
            }
            return userOrders;
        }
        if (orderStatus.equals(Staticstuffs.ORDER_STATUS_DELIVERD))
        {
            for(UserOrder u : orderList)
            {
                if(u.getStatus().equals(Staticstuffs.ORDER_STATUS_DELIVERD))
                    userOrders.add(u);
            }
            return userOrders;
        }

        return orderList;
    }
}