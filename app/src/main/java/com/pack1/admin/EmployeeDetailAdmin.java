package com.pack1.admin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pack1.quanlybangiaythethao.DatabaseHelper;
import com.pack1.quanlybangiaythethao.R;
import com.pack1.quanlybangiaythethao.Staticstuffs;
import com.pack1.quanlybangiaythethao.User;
import com.pack1.quanlybangiaythethao.UserDao;

import java.util.Calendar;
import java.util.Date;

public class EmployeeDetailAdmin extends AppCompatActivity {

    final static int PICK_IMAGE_REQUEST = 1;

    SQLiteDatabase db;
    DatabaseHelper dbhelper;
    EditText usernameInput,passwordInput,fNameInput,lNameInput,gmailInput,numberInput;
    CalendarView birthPicker;
    Button addAvatar,btnAddEmployee;
    Bitmap imageBitmap;
    ImageView avatarView;
    RadioButton rdMale ,rdFemale;
    int EmployeeID;

    String  selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_detail_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbardetailemployee);
        setSupportActionBar(toolbar);

        addAvatar = findViewById(R.id.setAvatarbtn);
        usernameInput = findViewById(R.id.usernameIp);
        passwordInput = findViewById(R.id.passwordIp);
        fNameInput = findViewById(R.id.fnameInput);
        lNameInput = findViewById(R.id.lnameInput);
        gmailInput = findViewById(R.id.gmailInput);
        numberInput = findViewById(R.id.numbersInput);
        birthPicker = findViewById(R.id.calendarView);
        avatarView = findViewById(R.id.avatarView);
        rdMale = findViewById(R.id.rdMale);
        rdFemale = findViewById(R.id.rdFemale);
        btnAddEmployee = findViewById(R.id.btnaddemp);

        dbhelper = new DatabaseHelper(this);
        db = dbhelper.getWritableDatabase();

        birthPicker.setMaxDate(System.currentTimeMillis());

        EmployeeID = Integer.parseInt(getIntent().getStringExtra("EmployeeID"));
        //Toast.makeText(this,""+EmployeeID,Toast.LENGTH_SHORT).show();

        birthPicker.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                selectedDate = day+"-"+(month+1)+"-"+year;
            }
        });

        loadEmployeeFromDatabase();



        createImageSelection();

        btnAddEmployee.setOnClickListener(view -> {
            new updateNhanVienToDatabaseAsync(this).execute();
        });

    }
    private class updateNhanVienToDatabaseAsync extends AsyncTask<Void,Void,Void>
    {
        int rs;
        private Context context;
        AlertDialog loadingDia;
        public updateNhanVienToDatabaseAsync(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LayoutInflater inflater = LayoutInflater.from(context);
            AlertDialog.Builder alBuilder  = new AlertDialog.Builder(context);
            alBuilder.setTitle("Đang chỉnh sửa tài khoảng nhân viên mới trong hệ thông");
            alBuilder.setCancelable(false);
            alBuilder.setView(inflater.inflate(R.layout.dialog_loading,null));
            loadingDia = alBuilder.create();
            loadingDia.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                rs = updateUser(context);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            loadingDia.dismiss();
            setResult(RESULT_OK);
            //clearAllInputField();
            if(rs == 0)
                Toast.makeText(getApplicationContext(),"Lỗi không chỉnh sửa được",Toast.LENGTH_SHORT).show();
            else {
                clearAllInputField();
                onBackPressed();
            }
        }
    }

    private int updateUser(Context context)// return 0 if can't update
    {
        String gender = rdMale.isChecked() ? Staticstuffs.MALE:Staticstuffs.FEMALE;

        UserDao userDao = new UserDao(context);
        User employee = new User(usernameInput.getText().toString(),
                passwordInput.getText().toString(),
                fNameInput.getText().toString(),
                lNameInput.getText().toString(),
                Staticstuffs.ConvertStringtoDate(selectedDate),
                Staticstuffs.NHANVIEN,
                gender,
                numberInput.getText().toString(),
                gmailInput.getText().toString(),
                imageBitmap);
        int raffacted = userDao.updateUserById(EmployeeID,employee);
        return raffacted;
    }

    private void createImageSelection()// lấy ảnh đại diện cho product
    {
        addAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
    }

    private void employeeAvatarRequestHandler(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            imageBitmap = Staticstuffs.uriToBitmap(this,selectedImageUri);
            avatarView.setImageURI(selectedImageUri);
        }
    }

    private void loadEmployeeFromDatabase()
    {
        UserDao userDao = new UserDao(this);
        User employee = userDao.getUserById(EmployeeID);
        usernameInput.setText(employee.getUserName());
        passwordInput.setText(employee.getPassword());
        fNameInput.setText(employee.getFname());
        lNameInput.setText(employee.getLname());
        Date birthdate = employee.getBirth();


        long timestamp = birthdate.getTime();
        birthPicker.setDate(timestamp, true, true);

        gmailInput.setText(employee.getGmail());
        numberInput.setText(employee.getNumbers());

        if(employee.getGender().equals(Staticstuffs.MALE))
            rdMale.setChecked(true);
        else {
            rdMale.setChecked(false);
            rdFemale.setChecked(true);
        }
        imageBitmap = employee.getAvatar();
        avatarView.setImageBitmap(imageBitmap);



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST)
            employeeAvatarRequestHandler(requestCode, resultCode, data);
    }
    private void clearAllInputField()
    {
        usernameInput.setText("");
        passwordInput.setText("");
        fNameInput.setText("");
        lNameInput.setText("");
        gmailInput.setText("");
        numberInput.setText("");
        avatarView.setImageResource(R.drawable.ic_launcher_background);
        imageBitmap = null;
        rdMale.setChecked(true);
        rdFemale.setChecked(false);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.employee_detail_action_bar,menu);
        return true;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btnback) {
            onBackPressed();
        }
        if(id == R.id.delbtn)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Xác nhận xóa"); // Tiêu đề của dialog.
            builder.setMessage("Bạn có chắc chắn muốn xóa tài khoảng này?"); // Nội dung dialog.

// Nút Yes
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Xử lý khi người dùng nhấn Yes
                    UserDao userDao = new UserDao(getApplicationContext());
                    if(userDao.deleteUserById(EmployeeID) == 0)
                        Toast.makeText(getApplicationContext(),"Không xóa được ! ",Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    onBackPressed();
                }
            });

// Nút No
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Xử lý khi người dùng nhấn No
                    dialog.dismiss(); // Đóng dialog
                }
            });

// Tạo và hiển thị AlertDialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return false;
    }
}