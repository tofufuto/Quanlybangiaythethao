package com.pack1.RegistersLogin;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
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
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.pack1.dao.DatabaseHelper;
import com.pack1.quanlybangiaythethao.R;
import com.pack1.quanlybangiaythethao.Staticstuffs;
import com.pack1.models.User;
import com.pack1.dao.UserDao;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class RegisterLayout extends AppCompatActivity {
    EditText birthEditText, etRegPassWord, etEmail, etRegUserName, etRegFName, etRegLName, etRegNumbers;
    Button btBack, btRegSubmit, btChooseImg;
    ImageView imgAvatar;
    Date date;
    CheckBox maleCheckBox, femaleCheckBox;
    TableRow tableRow;
    TextView tvUpperCase, tvSpecialChar, tvNumber, tvEmail;
    AtomicBoolean isFlagPassword = new AtomicBoolean(false);
    AtomicBoolean isFlagNumbers = new AtomicBoolean(false);

    AtomicBoolean isFlagEmail = new AtomicBoolean(false);
    DatabaseHelper dbHelper;
    Bitmap bitmapAvatar;
    static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri = null;
    private String[] fieldNames;
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
        etRegUserName.requestFocus();


        //chan khoang trang
        ConfigInput.blockSpaces(etRegUserName);
        ConfigInput.blockSpaces(etRegPassWord);
        ConfigInput.blockSpaces(etRegFName);
        ConfigInput.blockSpaces(etRegLName);
        ConfigInput.blockSpaces(etEmail);

        // chan so cho fname va lname
        ConfigInput.blockNumbers(etRegFName);
        ConfigInput.blockNumbers(etRegLName);
        //gioi han sdt
        ConfigInput.limitNumbers(etRegNumbers, isFlagNumbers, getApplicationContext());

        //button back lai giao dien dang nhap
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //enter button login
        etEmail.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        // Giả sử bạn muốn thực hiện đăng nhập khi Enter được nhấn
                        btRegSubmit.performClick();  // Giả lập việc nhấn nút Đăng Nhập
                        return true;  // Đánh dấu rằng sự kiện đã được xử lý
                    }
                }
                return false;  // Nếu không phải phím Enter thì không làm gì
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
        ConfigInput.isValidPassword(etRegPassWord, isFlagPassword, tvUpperCase, tvNumber, tvSpecialChar, getApplicationContext());


        //check emails
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String e = charSequence.toString().trim();
                if (!e.isEmpty()) {
                    tvEmail.setVisibility(View.VISIBLE);
                } else {
                    tvEmail.setVisibility(View.GONE);
                }
                if (!isValidEmail(e)) {
                    tvEmail.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_dark));
                } else {
                    tvEmail.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_green_dark));
                    isFlagEmail.set(true);
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


            String tk = etRegUserName.getText().toString();
            String mk = etRegPassWord.getText().toString();
            String fn = etRegFName.getText().toString();
            String ln = etRegLName.getText().toString();
            date = Staticstuffs.ConvertStringtoDate(birthEditText.getText().toString());
            String ngaysinh = birthEditText.getText().toString();
            String email = etEmail.getText().toString();
            String role = Staticstuffs.NGUOIDUNG;
            String sdt = etRegNumbers.getText().toString();
            String gt = maleCheckBox.isChecked() ? Staticstuffs.MALE : Staticstuffs.FEMALE;
            Bitmap avt = bitmapAvatar;
            UserDao userDao = new UserDao(this);
            EditText [] editTexts ={etRegUserName,etRegPassWord,etRegFName,etRegLName,birthEditText,
                    etRegNumbers, etEmail };
            fieldNames = new String[]{
                    "Tài Khoản",
                    "Mật khẩu",
                    "Tên",
                    "Họ",
                    "SĐT",
                    "Ngày Sinh",
                    "Email",
            };

            boolean allFieldsFilled = true;
            // kiem tra xem du lieu nhap vao co null khong
            String[] strings = {tk, mk, fn,ln,ngaysinh,email,sdt,gt};
            boolean allNonNull = Arrays.stream(strings).allMatch(s -> s != null && !s.isEmpty());
            if(allNonNull){
                //check tk va email trong db
                String checkResult = userDao.checkReg(tk, email);
                if (!"OK".equals(checkResult)) {
                    Toast.makeText(this, checkResult, Toast.LENGTH_SHORT).show(); // Hiển thị thông báo lỗi
                } else {
                    if(isFlagPassword.get()&&isFlagNumbers.get()) {
                        User u = new User(tk, mk, fn, ln, date, role, gt, sdt, email, avt);
                        int rs = (int) userDao.addUser(u);
                           Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterLayout.this, LoginLayout.class);
                        startActivity(intent);

                    }
                    else {
                        if(isFlagPassword.get()==false)
                            Toast.makeText(this, "Mật khẩu chưa hợp lệ!", Toast.LENGTH_SHORT).show();
                        if(isFlagNumbers.get()==false)
                            Toast.makeText(this, "SĐT chưa hợp lệ!", Toast.LENGTH_SHORT).show();
                        if(isFlagEmail.get()==false)
                            Toast.makeText(this, "Email chưa hợp lệ!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else{
                for (int i = 0; i < editTexts.length; i++) {
                    EditText editText = editTexts[i];
                    String input = editText.getText().toString().trim();
                    if (TextUtils.isEmpty(input)) {
                        // Nếu ô trống, focus vào ô đó và hiển thị thông báo
                        editText.requestFocus();
                        Toast.makeText(this, "Vui lòng nhập vào ô " + fieldNames[i], Toast.LENGTH_SHORT).show();
                        break;
                    }

                }

            }

        });


        //chon hinh anh avatar
        btChooseImg.setOnClickListener(view -> openGallery());

    }

    @SuppressWarnings("deprecation")
    private void openGallery()// lấy ảnh đại diện cho product
    {
        imgAvatar.setImageResource(R.drawable.user); //avt mac dinh
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data.getData() != null) {
            Uri selectedImageUri = data.getData(); // Lấy URI của ảnh được chọn
            if (selectedImageUri != null) {
                imgAvatar.setImageURI(selectedImageUri); // Hiển thị ảnh trong ImageView
                bitmapAvatar = Staticstuffs.uriToBitmap(this, selectedImageUri);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(selectedImageUri==null){
            imgAvatar.setImageResource(R.drawable.user);
        }
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
                    String selectedDate = "" + selectedDay + "-" + (selectedMonth + 1) + "-" + selectedYear;
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

    public class UsernameInputFilter implements InputFilter {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            // Chỉ cho phép các ký tự không dấu và không phải ký tự đặc biệt
            String blockCharacterSet = "[^a-zA-Z0-9]"; // Cho phép chỉ chữ và số

            for (int i = start; i < end; i++) {
                if (source.subSequence(i, i + 1).toString().matches(blockCharacterSet)) {
                    return ""; // Chặn ký tự không hợp lệ
                }
            }
            return null; // Ký tự hợp lệ
        }
    }

}


