package com.pack1.customer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pack1.dao.ProductDao;
import com.pack1.models.Product;
import com.pack1.quanlybangiaythethao.R;

import java.util.ArrayList;

import custom_adapter.ProductGridViewAdapter;

public class HomeActivity extends AppCompatActivity {

    Toolbar bottomToolbar;
    Toolbar toolbar;
    int currentUserId;
    ArrayList<Product> productList;
    TextView currUserIdTextView;
    GridView productDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        try {
            Intent homeActiIntent = this.getIntent();
            currentUserId = Integer.parseInt(homeActiIntent.getStringExtra("currentUserId"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        toolbar = findViewById(R.id.toolbarcustomerhomer);
        setSupportActionBar(toolbar);
        ////
        bottomToolbar = findViewById(R.id.bottom_toolbar);
        setupBottomToolbar(bottomToolbar);

        productDisplay = findViewById(R.id.productDisplay);


        loadProductFromDatabase();

        productDisplay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // ProductDao productDao = new ProductDao(getApplicationContext());
                TextView pName = view.findViewById(R.id.pname);
                Intent intent = new Intent(getApplicationContext(),CustomerProductDetail.class);
                intent.putExtra("productName",pName.getText().toString());
                intent.putExtra("currentUserId",""+currentUserId);
                startActivity(intent);
            }
        });


    }
    private void setupBottomToolbar(Toolbar bottomToolbar) {
        ImageButton orderBtn = bottomToolbar.findViewById(R.id.orderBtn);
        ImageButton homeBtn = bottomToolbar.findViewById(R.id.homeBtn);
        ImageButton profileBtn = bottomToolbar.findViewById(R.id.profileButton);

        homeBtn.setOnClickListener(v -> {
            // Xử lý sự kiện nút Home
            Toast.makeText(this, "Home Clicked", Toast.LENGTH_SHORT).show();
        });

        orderBtn.setOnClickListener(v -> {
            // Xử lý sự kiện nút Favorite
            Toast.makeText(this, "Order clicked", Toast.LENGTH_SHORT).show();
        });

        profileBtn.setOnClickListener(v -> {
            // Xử lý sự kiện nút Profile
            Toast.makeText(this, "Profile Clicked", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadProductFromDatabase() {
        ProductDao productDao = new ProductDao(this);
        productList = productDao.getAllProduct();

        ProductGridViewAdapter adapter = new ProductGridViewAdapter(this,productList, this.getLayoutInflater());
        productDisplay.setAdapter(adapter);
    }
    private void loadProductFromDatabaseWithKW(String kw)
    {
        ProductDao productDao = new ProductDao(this);

        productList = productDao.getProductsKw(kw);


        ProductGridViewAdapter adapter = new ProductGridViewAdapter(this,productList,this.getLayoutInflater());
        productDisplay.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.customer_home_action_bar,menu);

        MenuItem searchItem = menu.findItem(R.id.search_productbtn);
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
    @Override// cái nút back đi ra acti khác
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id ==R.id.cartBtn){
            //pass
            // Khi người dùng nhấn vào nút giỏ hàng, chuyển đến ShoppingCartActivity
            Intent intent = new Intent(this, ShoppingCartActivity.class);
            int currentUserId = 1;
            intent.putExtra("currentUserId", currentUserId); // Truyền currentUserId
            startActivity(intent); // Bắt đầu Activity mới
        }
        return false; // Trả về giá trị của phương thức cha
    }
}