package com.pack1.RegistersLogin;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.pack1.admin.AdminHome;
import com.pack1.customer.HomeActivity;
import com.pack1.employee.EmployeeHome;
import com.pack1.dao.DatabaseHelper;
import com.pack1.payment.PaymentLayout;
import com.pack1.quanlybangiaythethao.R;
import com.pack1.quanlybangiaythethao.Staticstuffs;
import com.pack1.dao.UserDao;


public class LoginLayout extends AppCompatActivity {

    EditText username, password;
    CheckBox cbPass;
    Button btlogin, btregister;
    SQLiteDatabase db;
    DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_layout);





//            this.deleteDatabase("Database.db");// xóa luôn cái database
//            dbHelper = new DatabaseHelper(this);
//            db = dbHelper.getWritableDatabase();



            btlogin = findViewById(R.id.btLogin);
            btregister = findViewById(R.id.btRegister);
            username = findViewById(R.id.etUserName);
            password = findViewById(R.id.etPassword);
            dbHelper = new DatabaseHelper(this);
            //xu ly checkbox
            username.requestFocus();
            ConfigInput.blockSpaces(username);
            ConfigInput.blockSpaces(password);
            cbPass = findViewById(R.id.cbShowPassword);

        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        // Giả sử bạn muốn thực hiện đăng nhập khi Enter được nhấn
                        btlogin.performClick();  // Giả lập việc nhấn nút Đăng Nhập
                        return true;  // Đánh dấu rằng sự kiện đã được xử lý
                    }
                }
                return false;  // Nếu không phải phím Enter thì không làm gì
            }
        });


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
                     if(taikhoan.equals(Staticstuffs.ADMIN_USER_NAME) && matkhau.equals(Staticstuffs.ADMIN_PASSWORD))
                     {
                         Intent intent = new Intent(getApplicationContext(), AdminHome.class);
                         startActivity(intent);
                         clearAllInputText();
                         return;
                     }
                     if(taikhoan.equals(Staticstuffs.DEV_USER_NAME) && matkhau.equals(Staticstuffs.DEV_PASSWORD))
                     {
                         Intent intent = new Intent(getApplicationContext(), PaymentLayout.class);
                         startActivity(intent);
                         clearAllInputText();
                         return;
                     }
                     UserDao userDao = new UserDao(getApplicationContext());
                     int userID = userDao.checkUser(taikhoan, matkhau);
                     if (userID != -1) {
                         try {
                             if(userDao.getUserRole(userID).equals(Staticstuffs.NGUOIDUNG)) {
                                 Toast.makeText(LoginLayout.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                 Intent intent = new Intent(LoginLayout.this, HomeActivity.class);
                                 intent.putExtra("currentUserId", "" + userID);
                                 startActivity(intent);
                             } else if (userDao.getUserRole(userID).equals(Staticstuffs.NHANVIEN)) {
                                 Toast.makeText(LoginLayout.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                 Intent intent = new Intent(LoginLayout.this, EmployeeHome.class);
                                 intent.putExtra("currentUserId", "" + userID);
                                 startActivity(intent);
                             }
                         }catch (Exception e)
                         {
                             e.printStackTrace();
                             clearAllInputText();
                         }

                     } else {
                         Toast.makeText(LoginLayout.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                     }
                     clearAllInputText();
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

        private void clearAllInputText()
        {
            username.setText("");
            password.setText("");
        }

    }
