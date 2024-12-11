package com.pack1.employee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pack1.quanlybangiaythethao.R;

public class EmployeeHome extends AppCompatActivity {

    int currentUserId;
    Button profileBtn,checkOrderBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        try {
            Intent homeActiIntent = this.getIntent();
            currentUserId = Integer.parseInt(homeActiIntent.getStringExtra("currentUserId"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        profileBtn = findViewById(R.id.profilenhanvien);
        checkOrderBtn = findViewById(R.id.checkorder);

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EmployeeProfile.class);
                intent.putExtra("currentUserId",""+currentUserId);
                startActivity(intent);
            }
        });

        checkOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}