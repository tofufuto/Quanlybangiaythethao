package com.pack1.employee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pack1.quanlybangiaythethao.R;
import com.pack1.quanlybangiaythethao.Staticstuffs;

public class EmployeeHome extends AppCompatActivity {

    int currentUserId;
    Button profileBtn,checkOrderBtn;
    ImageButton logoutBtn;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

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


        sharedPreferences = getSharedPreferences("AUTHORITY", MODE_PRIVATE);
        editor = sharedPreferences.edit();

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
        logoutBtn = findViewById(R.id.logoutbtn);

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
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean(Staticstuffs.SP_IS_SIGNIN,false);
                editor.commit();
                onBackPressed();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(sharedPreferences.getBoolean(Staticstuffs.SP_IS_SIGNIN,false)) {
            finishAffinity(); // Kết thúc toàn bộ activity trong task
            System.exit(0); // Đảm bảo quá trình bị kill (tùy chọn)
        }
    }
}