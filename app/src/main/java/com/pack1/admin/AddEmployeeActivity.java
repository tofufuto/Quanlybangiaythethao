package com.pack1.admin;

import android.app.DatePickerDialog;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pack1.RegistersLogin.RegisterLayout;
import com.pack1.dao.DatabaseHelper;
import com.pack1.quanlybangiaythethao.R;
import com.pack1.quanlybangiaythethao.Staticstuffs;
import com.pack1.models.User;
import com.pack1.dao.UserDao;

import java.util.Calendar;

public class AddEmployeeActivity extends AppCompatActivity {


    final static int PICK_IMAGE_REQUEST = 1;

    SQLiteDatabase db;
    DatabaseHelper dbhelper;
    EditText usernameInput,passwordInput,fNameInput,lNameInput,gmailInput,numberInput;
    TextView birthPicker;
    Button addAvatar,btnAddEmployee;
    Bitmap imageBitmap;
    ImageView avatarView;
    RadioButton rdMale ,rdFemale;

    String selectedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_employee);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbaraddemployee);
        setSupportActionBar(toolbar);

        addAvatar = findViewById(R.id.setAvatarbtn);
        usernameInput = findViewById(R.id.usernameIp);
        passwordInput = findViewById(R.id.passwordIp);
        fNameInput = findViewById(R.id.fnameInput);
        lNameInput = findViewById(R.id.lnameInput);
        gmailInput = findViewById(R.id.gmailInput);
        numberInput = findViewById(R.id.numbersInput);
        avatarView = findViewById(R.id.avatarView);
        rdMale = findViewById(R.id.rdMale);
        rdFemale = findViewById(R.id.rdFemale);
        btnAddEmployee = findViewById(R.id.btnaddemp);
        birthPicker =findViewById(R.id.birthdaychoose);

        dbhelper = new DatabaseHelper(this);
        db = dbhelper.getWritableDatabase();

        btnAddEmployee.setOnClickListener(view -> {
            new AddUserToDatabaseAsync(this).execute();
        });




            createImageSelection();


        birthPicker.setOnClickListener(view -> showDatePicker());

    }
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Đặt ngày đã chọn vào EditText
                    selectedDate = "" + selectedDay +"-"+(selectedMonth + 1)+"-"+selectedYear;
                    birthPicker.setText(selectedDate);
                },
                year, month, day);

        // giới hạn ngày, kh cho chọn tương lai
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    private class AddUserToDatabaseAsync extends AsyncTask<Void,Void,Void>
    {
        int rs;
        private Context context;
        AlertDialog loadingDia;
        public AddUserToDatabaseAsync(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LayoutInflater inflater = LayoutInflater.from(context);
            AlertDialog.Builder alBuilder  = new AlertDialog.Builder(context);
            alBuilder.setTitle("Đang thêm tài khoảng nhân viên mới vào hệ thông");
            alBuilder.setCancelable(false);
            alBuilder.setView(inflater.inflate(R.layout.dialog_loading,null));
            loadingDia = alBuilder.create();
            loadingDia.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                rs = addNewEmployeeToDatabase(context);
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
            if(rs == -1)
                Toast.makeText(getApplicationContext(),"Lỗi không thêm vào được Database",Toast.LENGTH_SHORT).show();
            else
                clearAllInputField();
        }
    }

    private int addNewEmployeeToDatabase(Context context){

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
        int rs = (int) userDao.addUser(employee);
        return  rs;
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST)
            employeeAvatarRequestHandler(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_product_action_bar,menu);
        return true;
    }

    @Override// cái nút back đi ra acti khác
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.btnback) {
            onBackPressed();
            setResult(RESULT_OK);
        }
        return false;
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
}