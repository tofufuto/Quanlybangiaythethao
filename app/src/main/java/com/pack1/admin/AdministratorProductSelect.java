package com.pack1.admin;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pack1.dao.DatabaseHelper;
import com.pack1.models.Product;
import com.pack1.dao.ProductDao;
import com.pack1.quanlybangiaythethao.R;

import java.util.ArrayList;

import custom_adapter.ProductGridViewAdapter;

public class AdministratorProductSelect extends AppCompatActivity {

    DatabaseHelper dbHelper;
    SQLiteDatabase database;
    GridView productDisplay;
    ProductGridViewAdapter adapter;
    ArrayList<Product> productList;
    //private final int GALLERY_REQUEST_CODE = 999;
    //mỗi lần nhập xong sản phẩm là form  AddProduct sẽ trả ra result OK và load lại sản phâ từ database


    private final ActivityResultLauncher<Intent> addProductLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {

                // Kiểm tra kết quả trả về từ AddProductActivity
                if (result.getResultCode() == RESULT_OK) {
                    // Reload lại dữ liệu từ SQLite
                    loadProductFromDatabase();
                }
            }
    );

    private final ActivityResultLauncher<Intent> productDetailLaucher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result ->{
                if (result.getResultCode() == RESULT_OK) {
                    // Reload lại dữ liệu từ SQLite
                    loadProductFromDatabase();
                }
            }
    );

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


        loadProductFromDatabase();

        productDisplay.setOnItemClickListener(new AdapterView.OnItemClickListener() {// click vào 1 giày để sửa thông tin
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(AdministratorProductSelect.this, ProductDetailAdmin.class);
                TextView tv = view.findViewById(R.id.pname);
                String name = tv.getText().toString();
                intent.putExtra("clickedProductName",name);
                productDetailLaucher.launch(intent);
            }
        });




    }

    private void loadProductFromDatabase() {
        ProductDao productDao = new ProductDao(this);
        productList = productDao.getAllProduct();

        adapter = new ProductGridViewAdapter(this,productList, this.getLayoutInflater());
        productDisplay.setAdapter(adapter);
    }
    private void loadProductFromDatabaseWithKW(String kw)
    {
        ProductDao productDao = new ProductDao(this);

        productList = productDao.getProductsKw(kw);


        adapter = new ProductGridViewAdapter(this,productList,this.getLayoutInflater());
        productDisplay.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.administrator_product_select_action_bar,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search_product_select_view);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadProductFromDatabaseWithKW(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty())
                    loadProductFromDatabase();
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.btnadd) {
            Intent intent = new Intent(this, AddProductActivity.class);
            addProductLauncher.launch(intent);
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}