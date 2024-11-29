package com.pack1.quanlybangiaythethao;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import custom_adapter.GridViewImageAdapter;

public class ProductDetailAdmin extends AppCompatActivity {

    Toolbar toolbar;
    Intent intent;


    EditText nameInput,quantityInput,sizeInput,colorInput,brandInput,descriptionInput,priceInput;
    RadioButton rdMale,rdFemale;
    Button btnAddImageMain,btnAddProduct;
    ImageView imageView;
    Bitmap imageBitmap;
    ArrayList<Bitmap> productImagesBitmap = new ArrayList<>();
    GridView gridView;
    Button btnAddMutipleImages;
    GridViewImageAdapter gridViewImageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gridViewImageAdapter = new GridViewImageAdapter(this,productImagesBitmap,this.getLayoutInflater());
        priceInput = findViewById(R.id.priceip);
        nameInput = findViewById(R.id.nameip);
        quantityInput = findViewById(R.id.quantityip);
        sizeInput = findViewById(R.id.sizeip);
        colorInput = findViewById(R.id.colorip);
        brandInput = findViewById(R.id.brandip);
        descriptionInput = findViewById(R.id.descrip);
        rdMale = findViewById(R.id.rdmale);
        rdFemale = findViewById(R.id.rdfemale);
        btnAddImageMain = findViewById(R.id.btnAddImageMain);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        imageView = findViewById(R.id.imageViewMain);
        gridView = findViewById(R.id.productImagesDisplay);
        btnAddMutipleImages = findViewById(R.id.btnAddMutiImage);


        intent = getIntent();
        toolbar = findViewById(R.id.toolbarproductdetail);
        setSupportActionBar(toolbar);
        String clickedProductName = intent.getStringExtra("clickedProductName");

        loadProductFromDatabase(clickedProductName);

    }
    private void loadProductFromDatabase(String pName)
    {
        ProductDao productDao = new ProductDao(this);
        Product product = productDao.getProductName(pName);
        ProductImageDao productImageDao = new ProductImageDao(this);
        nameInput.setText(product.getName());
        priceInput.setText(""+product.getPrice());
        quantityInput.setText(""+product.getQuantity());
        sizeInput.setText(""+product.getSize());
        colorInput.setText(product.getColor());
        brandInput.setText(product.getBrand());
        descriptionInput.setText(product.getDescription());
        if(product.getGender().equals(Staticstuffs.MALE))
            rdMale.setChecked(true);
        else
            rdFemale.setChecked(true);
        imageView.setImageBitmap(product.getPdImage());

        ArrayList<Bitmap> Images = Staticstuffs.productImageListToBitmapList(productImageDao.getAllProductImagesFromID(product.getProduct_id()));
        GridViewImageAdapter adapter = new GridViewImageAdapter(this,Images,this.getLayoutInflater());
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_detail_action_bar,menu);
        return true;
    }
    @Override// cái nút back đi ra acti khác
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.btnback) {
            onBackPressed();
        }
        return false;
    }

}