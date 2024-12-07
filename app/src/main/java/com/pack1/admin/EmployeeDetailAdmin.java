package com.pack1.admin;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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

    SQLiteDatabase db;
    DatabaseHelper dbhelper;
    EditText usernameInput,passwordInput,fNameInput,lNameInput,gmailInput,numberInput;
    CalendarView birthPicker;
    Button addAvatar,btnAddEmployee;
    Bitmap imageBitmap;
    ImageView avatarView;
    RadioButton rdMale ,rdFemale;
    int EmployeeID;

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
        loadEmployeeFromDatabase();
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
}