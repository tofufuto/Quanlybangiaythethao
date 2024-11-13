package com.pack1.quanlybangiaythethao;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import custom_adapter.ProductGridViewAdapter;

public class AdministratorProductSelect extends AppCompatActivity {

    DatabaseHelper dbHelper;
    SQLiteDatabase database;
    GridView productDisplay;
    ProductGridViewAdapter adapter;
    ArrayList<Product> productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administrator_product_select);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        productDisplay = findViewById(R.id.productDisplay);
        Toolbar toolbar = findViewById(R.id.toolbarselectproduct);
        setSupportActionBar(toolbar);



        ProductDao productDao = new ProductDao(this);
        productList = productDao.getAllProduct();

        adapter = new ProductGridViewAdapter(this,productList, this.getLayoutInflater());
        productDisplay.setAdapter(adapter);



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.administrator_product_select_action_bar,menu);
        return true;
    }

    @Override// cái nút back đi ra acti khác
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.btnadd) {
            Intent intent = new Intent(this, AddProductActivity.class);
            startActivity(intent);
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}