package com.pack1.quanlybangiaythethao;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.Nullable;

import java.util.Calendar;
import java.util.Date;

public class RegisterLayout extends AppCompatActivity {
    EditText birthEditText, etRegPassWord, etEmail, etRegUserName, etRegFName, etRegLName,etConfirmPassword, etRegNumbers;
    Button btBack, btRegSubmit, btChooseImg;
    ImageView imgAvatar;
    Date date;
    CheckBox maleCheckBox, femaleCheckBox;
    TableRow tableRow;
    TextView tvUpperCase, tvSpecialChar, tvNumber,tvEmail,tvConfirm;
    boolean flagPassword,flagEmail, flagConfirmPassword;
    DatabaseHelper dbHelper;
    Bitmap bitmapAvatar;
    static final int PICK_IMAGE_REQUEST = 1;
    private ActivityResultLauncher<Intent> galleryLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_layout);


        // tham chieu gia tri
        tableRow = findViewById(R.id.tbrCheckbox);
        maleCheckBox = findViewById(R.id.checkbox_male);
        femaleCheckBox = findViewById(R.id.checkbox_female);
        birthEditText = findViewById(R.id.etRegbirth);
        btBack = findViewById(R.id.btBack);
        btRegSubmit = findViewById(R.id.btRegSumbit);
        etRegPassWord = findViewById(R.id.etRegPassword);
        etRegUserName = findViewById(R.id.etRegUserName);
        etRegFName = findViewById(R.id.etfname);
        etRegLName = findViewById(R.id.etlname);
        tvUpperCase = findViewById(R.id.tvUpperCase);
        etRegNumbers = findViewById(R.id.etRegNumbers);
        tvSpecialChar = findViewById(R.id.tvSpecialChar);
        tvEmail = findViewById(R.id.textEmail);
        tvNumber = findViewById(R.id.tvNumber);
        etEmail = findViewById(R.id.etRegEmail);
        imgAvatar = findViewById(R.id.imgAvatar);
        btChooseImg = findViewById(R.id.btnChooseImage);


        //check password = confirm password
//       etConfirmPassword.addTextChangedListener(new TextWatcher() {
//           @Override
//           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//               tvConfirm.setText("Chưa khớp");
//               tvConfirm.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_dark));
//           }
//
//           @Override
//           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//               if(etRegPassWord.getText().toString().equals(etConfirmPassword.getText().toString()));
//               tvConfirm.setText("Khớp mật khẩu");
//               tvConfirm.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_green_dark));
//           }
//
//           @Override
//           public void afterTextChanged(Editable editable) {
//
//           }
//       });

        //button back lai giao dien dang nhap
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //checkbox hien matkhau
        CheckBox cbshowPass = findViewById(R.id.cbShowPassword);
        cbshowPass.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Hiện mật khẩu
                etRegPassWord.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                // Ẩn mật khẩu
                etRegPassWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });


        //calendar birth
        birthEditText.setOnClickListener(view -> showDatePicker());
        maleCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                femaleCheckBox.setChecked(false); // Bỏ chọn Female nếu chọn Male
            }
        });
        femaleCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                maleCheckBox.setChecked(false); // Bỏ chọn Male nếu chọn Female
            }
        });

        //check password
        etRegPassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    flagPassword = false;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = charSequence.toString();
                if (password.isEmpty()) {
                    tvUpperCase.setVisibility(View.GONE);
                    tvNumber.setVisibility(View.GONE);
                    tvSpecialChar.setVisibility(View.GONE);
                }
                if (!password.isEmpty()) {
                    tvUpperCase.setVisibility(View.VISIBLE);
                    tvNumber.setVisibility(View.VISIBLE);
                    tvSpecialChar.setVisibility(View.VISIBLE);
                }
                    // Check for uppercase letter
                    if (password.matches(".*[A-Z].*")) {
                        tvUpperCase.setTextColor(ContextCompat.getColor(getApplicationContext(),android.R.color.holo_green_dark));
                    } else {
                        tvUpperCase.setTextColor(ContextCompat.getColor(getApplicationContext(),android.R.color.holo_red_dark));
                    }

                    // Check for number
                    if (password.matches(".*\\d.*")) {
                        tvNumber.setTextColor(ContextCompat.getColor(getApplicationContext(),android.R.color.holo_green_dark));
                    } else {
                        tvNumber.setTextColor(ContextCompat.getColor(getApplicationContext(),android.R.color.holo_red_dark));
                    }

                    // Check for special character
                    if (password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
                        tvSpecialChar.setTextColor(ContextCompat.getColor(getApplicationContext(),android.R.color.holo_green_dark));
                    } else {
                        tvSpecialChar.setTextColor(ContextCompat.getColor(getApplicationContext(),android.R.color.holo_red_dark));
                    }
                }

            @Override
            public void afterTextChanged(Editable editable) {
                flagPassword = true;
            }
        });

        //check emails
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String e = charSequence.toString().trim();
                if(!e.isEmpty()){
                    tvEmail.setVisibility(View.VISIBLE);
                }
                else {
                    tvEmail.setVisibility(View.GONE);
                }
                if(!isValidEmail(e)){
                    tvEmail.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_dark));
                }
                else {
                    tvEmail.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_green_dark));
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //xu ly bitmap avatar
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            imgAvatar.setImageURI(selectedImageUri);
                            bitmapAvatar = Staticstuffs.uriToBitmap(this, selectedImageUri);
                        }
                    }
                }
        );
        //xu ly nut dang ky
        btRegSubmit.setOnClickListener(view -> {

        try{
            String tk = etRegUserName.getText().toString();
            String mk = etRegPassWord.getText().toString();
            String fn = etRegFName.getText().toString();
            String ln = etRegLName.getText().toString();
            date = Staticstuffs.ConvertStringtoDate(birthEditText.getText().toString());
            String email = etEmail.getText().toString();
            String role = Staticstuffs.NGUOIDUNG;
            String sdt = etRegNumbers.getText().toString();
            String gt = maleCheckBox.isChecked()?Staticstuffs.NAM:Staticstuffs.NU;
            Bitmap avt = bitmapAvatar;
            UserDao userDao = new UserDao(this);
            User u = new User(tk,mk,fn,ln,date,role,gt,sdt,email,avt);

            int rs =(int)userDao.addUser(u);
        }catch (Exception ex){
            Toast.makeText(this,"Loi",Toast.LENGTH_SHORT).show();
        }

        });


        //chon hinh anh avatar
            btChooseImg.setOnClickListener(view -> openGallery());

    }

    @SuppressWarnings("deprecation")
    private void openGallery()// lấy ảnh đại diện cho product
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Hien ra lịch chọn cho birth
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                RegisterLayout.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Đặt ngày đã chọn vào EditText
                    String selectedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    birthEditText.setText(selectedDate);
                },
                year, month, day);

        // giới hạn ngày, kh cho chọn tương lai
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com$";
        return email.matches(emailPattern);
    }

}


