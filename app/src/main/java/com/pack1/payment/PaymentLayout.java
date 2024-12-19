package com.pack1.payment;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.pack1.dao.ProductDao;
import com.pack1.dao.UserDao;
import com.pack1.models.Product;
import com.pack1.models.User;
import com.pack1.payment.lib.Api.CreateOrder;
import com.pack1.quanlybangiaythethao.R;

import org.json.JSONObject;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class PaymentLayout extends AppCompatActivity {
    TextView tvInfo,tvaddress,tvToTal;
    int currentUserId,productId;
    String productName;
    EditText etAddress, etQuantity;
    Button btAddress, btZalo;
    Float totalPrice;

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
        btZalo = findViewById(R.id.btzalo);
        etQuantity = findViewById(R.id.etQuantity);
        Intent intent = this.getIntent();
        UserDao userDao = new UserDao(this);
        currentUserId = Integer.parseInt(intent.getStringExtra("currentUserId"));
        productName = intent.getStringExtra("productName");


        ProductDao productDao = new ProductDao(this );
        Product product = productDao.getProductName(productName);
        productId = productDao.getProductIdByName(productName);

        User user = userDao.getUserById(Integer.parseInt(intent.getStringExtra("currentUserId")));
        tvInfo.setText("Hello: "+user.getFname()+" "+user.getLname());

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
        String totalstring = String.format("%.0f",totalPrice);
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(553, Environment.SANDBOX);
        btZalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateOrder orderApi = new CreateOrder();
                try {
                    JSONObject data = orderApi.createOrder(totalstring);
                    String code = data.getString("returncode");
                    if (code.equals("1")) {
                        String token = data.getString("zptranstoken");
                        ZaloPaySDK.getInstance().payOrder(PaymentLayout.this, token, "demozpdk://app", new PayOrderListener() {
                            @Override
                            public void onPaymentSucceeded(String s, String s1, String s2) {
                                Intent intent1 = new Intent(PaymentLayout.this,payment_receipt.class);
                                intent1.putExtra("result","Thanh toán thành công");
                               startActivity(intent1);

                            }

                            @Override
                            public void onPaymentCanceled(String s, String s1) {
                                Intent intent1 = new Intent(PaymentLayout.this,payment_receipt.class);
                                intent1.putExtra("result"," Hủy thanh toán ");
                                startActivity(intent1);
                            }

                            @Override
                            public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                                Intent intent1 = new Intent(PaymentLayout.this,payment_receipt.class);
                                intent1.putExtra("result"," Lỗi thanh toán ");
                                startActivity(intent1);


                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}