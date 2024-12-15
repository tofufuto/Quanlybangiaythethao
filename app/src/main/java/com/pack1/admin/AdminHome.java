package com.pack1.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pack1.quanlybangiaythethao.R;
import com.pack1.quanlybangiaythethao.Staticstuffs;

public class AdminHome extends AppCompatActivity {

    Toolbar toolbar;

    Button checkProductsBtn, employeeManageBtn,statsBtn;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

         sharedPreferences = getSharedPreferences("AUTHORITY", MODE_PRIVATE);
         editor = sharedPreferences.edit();

        toolbar = findViewById(R.id.adminActionBar);
        setSupportActionBar(toolbar);

        checkProductsBtn = findViewById(R.id.checksanpham);
        checkProductsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, AdministratorProductSelect.class);
            startActivity(intent);
        });
        employeeManageBtn = findViewById(R.id.checknhanvien);
        employeeManageBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, EmployeeManageActivity.class);
            startActivity(intent);
        });
        statsBtn = findViewById(R.id.statsbtn);
        statsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this,Stats.class);
            startActivity(intent);
        });


        /*User user = new User("skibidi","123","Ski","bidi", Staticstuffs.ConvertStringtoDate("07-08-2004"),Staticstuffs.NGUOIDUNG,Staticstuffs.MALE,"1111111113","skibididomdom@gmail.com",null);
        User user2 = new User("Shikanoko","123","Shika","noko",Staticstuffs.ConvertStringtoDate("07-08-2004"),Staticstuffs.NGUOIDUNG,Staticstuffs.FEMALE,"1111111112","lmaoxd@gmail.com",null);
        UserDao userDao = new UserDao(this);
        userDao.addUser(user);
        userDao.addUser(user2);*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_home_action_bar,menu);
        return true;
    }
    @Override// cái nút back đi ra acti khác
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logoutbtn) {
            editor.putBoolean(Staticstuffs.SP_IS_SIGNIN,false);
            editor.commit();
            onBackPressed();
        }
        return false;
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