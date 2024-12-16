package com.pack1.customer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.pack1.quanlybangiaythethao.Staticstuffs;

import java.util.ArrayList;
import java.util.Collections;

import custom_adapter.ProductGridViewAdapter;

public class HomeActivity extends AppCompatActivity {

    Toolbar bottomToolbar;
    Toolbar toolbar;
    int currentUserId;
    ArrayList<Product> productList;
    //TextView currUserIdTextView;
    GridView productDisplay;

    Button allPdBtn,maleBtn,femaleBtn,sortBtn;

    ImageButton logoutBtn;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    boolean isincrease = false;

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

        allPdBtn = findViewById(R.id.allProductbtn);
        maleBtn = findViewById(R.id.maleProduct);
        femaleBtn = findViewById(R.id.femaleProduct);
        logoutBtn = findViewById(R.id.logoutbtn);
        sortBtn = findViewById(R.id.sortPrice);

        sharedPreferences = getSharedPreferences("AUTHORITY", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        loadProductFromDatabase(null);

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
        allPdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadProductFromDatabase(null);
            }
        });

        maleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadProductFromDatabase(Staticstuffs.MALE);
            }
        });

        femaleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadProductFromDatabase(Staticstuffs.FEMALE);
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean(Staticstuffs.SP_IS_SIGNIN,false);
                editor.commit();
                onBackPressed();
            }
        });
        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isincrease) {
                    sortProductsByPriceAscending(productList);
                }
                else
                    sortProductsByPriceDescending(productList);
                isincrease = !isincrease;
                loadProductToGridView();

            }
        });

    }
    public void sortProductsByPriceAscending(ArrayList<Product> products) {
        Collections.sort(products, (p1, p2) -> Float.compare(p1.getPrice(), p2.getPrice()));
    }
    public void sortProductsByPriceDescending(ArrayList<Product> products) {
        Collections.sort(products, (p1, p2) -> Float.compare(p2.getPrice(), p1.getPrice()));
    }

    private void setupBottomToolbar(Toolbar bottomToolbar) {
        ImageButton orderBtn = bottomToolbar.findViewById(R.id.orderBtn);
        ImageButton homeBtn = bottomToolbar.findViewById(R.id.homeBtn);
        ImageButton profileBtn = bottomToolbar.findViewById(R.id.profileButton);

        homeBtn.setOnClickListener(v -> {

            loadProductFromDatabase(null);
        });

        orderBtn.setOnClickListener(v -> {

            Intent intent = new Intent(getApplicationContext(), CustomerOrdersView.class);
            intent.putExtra("currentUserId",""+currentUserId);
            startActivity(intent);
        });

        profileBtn.setOnClickListener(v -> {

//            Toast.makeText(this, "Profile Clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),ProfileDetail.class);
            intent.putExtra("currentUserId",""+currentUserId);
            startActivity(intent);
        });
    }

    private void loadProductFromDatabase(String gender) {
        if(gender == null)
        {
            ProductDao productDao = new ProductDao(this);
            productList = productDao.getAllProduct();

            loadProductToGridView();
        }else if (gender.equals(Staticstuffs.MALE))
        {
            ProductDao productDao = new ProductDao(getApplicationContext());
            productList = productDao.getProductsByGender(Staticstuffs.MALE);

            loadProductToGridView();
        } else if (gender.equals(Staticstuffs.FEMALE)) {
            ProductDao productDao = new ProductDao(getApplicationContext());
            productList = productDao.getProductsByGender(Staticstuffs.FEMALE);

            loadProductToGridView();
        }
    }

    private void loadProductToGridView() {
        ProductGridViewAdapter adapter = new ProductGridViewAdapter(this, productList, this.getLayoutInflater());
        productDisplay.setAdapter(adapter);
    }

    private void loadProductFromDatabaseWithKW(String kw)
    {
        ProductDao productDao = new ProductDao(this);

        productList = productDao.getProductsKw(kw);


        loadProductToGridView();
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
                    loadProductFromDatabase(null);
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
            intent.putExtra("currentUserId", currentUserId); // Truyền currentUserId
            startActivity(intent); // Bắt đầu Activity mới
        }
        return false; // Trả về giá trị của phương thức cha
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(sharedPreferences.getBoolean(Staticstuffs.SP_IS_SIGNIN,false)) {
            finishAffinity(); // Kết thúc toàn bộ activity trong task
            System.exit(0); // Đảm bảo quá trình bị kill (tùy chọn)
        }
    }
}