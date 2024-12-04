package com.pack1.admin;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pack1.quanlybangiaythethao.R;
import com.pack1.quanlybangiaythethao.User;
import com.pack1.quanlybangiaythethao.UserDao;

import java.util.ArrayList;

import custom_adapter.EmployeesListAdapter;

public class EmployeeManageActivity extends AppCompatActivity {


    Toolbar toolbar;
    ListView employeeListDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_manage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbar = findViewById(R.id.employeelisttoolbar);
        setSupportActionBar(toolbar);

        employeeListDisplay = findViewById(R.id.listEmployeesDisplay);



        loadEmployees();
    }

    private void loadEmployees()
    {   UserDao userDao = new UserDao(this);
        ArrayList<User> empList = userDao.getAllEmployees();
        EmployeesListAdapter employeesListAdapter = new EmployeesListAdapter(this,empList,this.getLayoutInflater());
        employeeListDisplay.setAdapter(employeesListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.employee_manage_actionbar,menu);
        return true;
    }

    @Override// cái nút back đi ra acti khác
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.btnadd) {
            //
        }
        if(id == R.id.search_employee)
        {
            //
        }
        return false;
    }
}


