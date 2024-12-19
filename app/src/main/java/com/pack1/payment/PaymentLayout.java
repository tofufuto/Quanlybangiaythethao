package com.pack1.payment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.pack1.dao.ProductDao;
import com.pack1.dao.UserDao;
import com.pack1.dao.UserOrderDao;
import com.pack1.models.Product;
import com.pack1.models.User;
import com.pack1.models.UserOrder;
import com.pack1.quanlybangiaythethao.R;
import com.pack1.quanlybangiaythethao.Staticstuffs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class PaymentLayout extends AppCompatActivity {
    TextView tvInfo,tvaddress,tvToTal;
    int currentUserId,productId;
    String productName;
    EditText etAddress, etQuantity;
    Button btAddress, btZalo;
    Float totalPrice;
    ImageView imgProduct;
    TextView tvNameProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_layout);
        tvaddress = findViewById(R.id.tvAddress);
        etAddress = findViewById(R.id.etAddress);
        btAddress = findViewById(R.id.btnOk);
        tvInfo = findViewById(R.id.tvInfoUser);
        tvToTal = findViewById(R.id.tvtotal);
        btZalo = findViewById(R.id.btmomo);
        etQuantity = findViewById(R.id.etQuantity);
        Intent intent = this.getIntent();
        UserDao userDao = new UserDao(this);
        currentUserId = Integer.parseInt(intent.getStringExtra("currentUserId"));
        productName = intent.getStringExtra("productName");
        imgProduct = findViewById(R.id.imgProduct);
        tvNameProduct = findViewById(R.id.tvNameProduct);


        ProductDao productDao = new ProductDao(this );
        UserOrderDao userOrderDao = new UserOrderDao(this);
        Product product = productDao.getProductName(productName);
        productId = productDao.getProductIdByName(productName);

        User user = userDao.getUserById(Integer.parseInt(intent.getStringExtra("currentUserId")));
        tvInfo.setText("Hello: "+user.getFname()+" "+user.getLname());

        tvNameProduct.setText(productName);
        imgProduct.setImageBitmap(product.getPdImage());
        tvaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etAddress.setVisibility(View.VISIBLE);
                btAddress.setVisibility(View.VISIBLE);
            }
        });
        btAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvaddress.setText("Địa chỉ của bạn: "+etAddress.getText().toString());
                etAddress.setVisibility(View.GONE);
                btAddress.setVisibility(View.GONE);
            }
        });
        etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String quantityStr = s.toString();
                if (!quantityStr.isEmpty()) {
                    int quantity = Integer.parseInt(quantityStr);
                    totalPrice = quantity * product.getPrice();
                    tvToTal.setText("Tổng tiền: " +Float.toString(totalPrice));
                } else {
                    tvToTal.setText("Tổng tiền: 0");
                }

            }
        });
        btZalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("momo://"));
                intent.setPackage("com.momo.android");
                Log.d("MyLog", "Intent: " + String.valueOf(intent1));
                if (intent1 != null) {
                    // Nếu ứng dụng Momo tồn tại, mở ứng dụng Momo
                    String sl= etQuantity.getText().toString();
                    float tonggia = totalPrice;
                    String status= Staticstuffs.ORDER_STATUS_PENDIND;
                    int id= user.getUserId();
                    int idprod = product.getProduct_id();
                    String diachi= etAddress.getText().toString();
                    UserOrder userOrder = new UserOrder(Integer.parseInt(sl),tonggia,status,idprod,id,diachi);

                    userOrderDao.addUserOrder(userOrder);
                    startActivity(intent1);
                } else {
                    // Nếu không có ứng dụng Momo, hiển thị thông báo
                    Toast.makeText(getApplicationContext(), "Ứng dụng Momo không có trên thiết bị của bạn", Toast.LENGTH_SHORT).show();
                }

            }
        });


//        String sl= etQuantity.getText().toString();
//        float tonggia = totalPrice;
//        String status= Staticstuffs.ORDER_STATUS_PENDIND;
//        int id= user.getUserId();
//        int idprod = product.getProduct_id();
//        String diachi= etAddress.getText().toString();
//        UserOrder userOrder = new UserOrder(Integer.parseInt(sl),tonggia,status,idprod,id,diachi);
//
//        userOrderDao.addUserOrder(userOrder);
    }
}


