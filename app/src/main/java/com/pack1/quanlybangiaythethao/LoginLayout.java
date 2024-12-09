package com.pack1.quanlybangiaythethao;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class LoginLayout extends AppCompatActivity {

    EditText username, password;
    CheckBox cbPass;
    Button btlogin, btregister;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_layout);


            btlogin = findViewById(R.id.btLogin);
            btregister = findViewById(R.id.btRegister);
            username = findViewById(R.id.etUserName);
            password = findViewById(R.id.etPassword);
            dbHelper = new DatabaseHelper(this);
            //xu ly checkbox
            cbPass = findViewById(R.id.cbShowPassword);

        cbPass.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Hiện mật khẩu
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                // Ẩn mật khẩu
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
            // xử lý nút login
             btlogin.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     String taikhoan = username.getText().toString();
                     String matkhau = password.getText().toString();
                     if (UserDao.checkUser(taikhoan, matkhau)) {
                         Toast.makeText(LoginLayout.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                         startActivity(new Intent(LoginLayout.this, HomeActivity.class));
                     } else {
                         Toast.makeText(LoginLayout.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                     }
                 }
             });




            // xử lý nút register
            btregister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(LoginLayout.this, RegisterLayout.class);
                    startActivity(intent);
                }
            });

        };
    }
