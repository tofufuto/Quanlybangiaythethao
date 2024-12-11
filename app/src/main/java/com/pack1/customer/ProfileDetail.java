package com.pack1.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pack1.dao.ProductDao;
import com.pack1.dao.UserDao;
import com.pack1.models.User;
import com.pack1.quanlybangiaythethao.R;
import com.pack1.quanlybangiaythethao.Staticstuffs;

public class ProfileDetail extends AppCompatActivity {

    private final ActivityResultLauncher<Intent> editProfileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {

                // Kiểm tra kết quả trả về từ AddProductActivity
                if (result.getResultCode() == RESULT_OK) {
                    // Reload lại dữ liệu từ SQLite
                    loadUserInfo();
                }
            }
    );


    TextView fullNameTv,genderTv,birthDateTv,dateAddedTv,userNameTv,passWordTv,gmailTv,sdtTv;
    CheckBox hidePassw;
    ImageButton editBtn;
    ImageView avatarV;
    int currentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        currentUserId = Integer.parseInt(this.getIntent().getStringExtra("currentUserId"));

        fullNameTv = findViewById(R.id.fullname);
        genderTv = findViewById(R.id.gender);
        birthDateTv = findViewById(R.id.birth);
        dateAddedTv = findViewById(R.id.dateadded);
        userNameTv = findViewById(R.id.userName);
        passWordTv = findViewById(R.id.passWord);
        gmailTv = findViewById(R.id.gmail);
        sdtTv = findViewById(R.id.sdt);
        hidePassw = findViewById(R.id.hideShow);
        editBtn = findViewById(R.id.editbtn);
        avatarV = findViewById(R.id.avatar);

        loadUserInfo();

        hidePassw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    passWordTv.setVisibility(View.INVISIBLE);
                else
                    passWordTv.setVisibility(View.VISIBLE);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ProfileEditActi.class);
                intent.putExtra("currentUserId",""+currentUserId);
                editProfileLauncher.launch(intent);
            }
        });
    }

    private void loadUserInfo()
    {
        UserDao userDao = new UserDao(this);
        User user = userDao.getUserById(currentUserId);
        String ngayTao = Staticstuffs.ConvertDatetoString( userDao.getDateAddedById(currentUserId));
        fullNameTv.setText(user.getFname() + " "+user.getLname());
        genderTv.setText(user.getGender());
        birthDateTv.setText(Staticstuffs.ConvertDatetoString( user.getBirth()));
        dateAddedTv.setText(ngayTao);
        userNameTv.setText(user.getUserName());
        passWordTv.setText(user.getPassword());
        gmailTv.setText(user.getGmail());
        sdtTv.setText(user.getNumbers());
        avatarV.setImageBitmap(user.getAvatar());
    }


}