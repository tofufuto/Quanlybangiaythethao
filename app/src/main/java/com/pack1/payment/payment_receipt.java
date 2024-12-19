package com.pack1.payment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pack1.quanlybangiaythethao.R;

public class payment_receipt extends AppCompatActivity {
    TextView tvThongBao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_receipt);
        tvThongBao = findViewById(R.id.tvNotification);
        Intent intent = getIntent();
        tvThongBao.setText(intent.getStringExtra("result"));
    }
}