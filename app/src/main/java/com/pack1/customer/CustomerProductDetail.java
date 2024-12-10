package com.pack1.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pack1.dao.ProductImageDao;
import com.pack1.models.ProducImage;
import com.pack1.quanlybangiaythethao.R;

import java.util.ArrayList;

public class CustomerProductDetail extends AppCompatActivity {

    int currentUserId;
    int productId;
    LinearLayout linearLayout;
    ArrayList<ProducImage> producImages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_product_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //lấy currentuserid với productId dc bấm vào
        Intent ProductDetailIntent = this.getIntent();
        currentUserId = Integer.parseInt(ProductDetailIntent.getStringExtra("currentUserId"));
        productId = Integer.parseInt(ProductDetailIntent.getStringExtra("productId"));


        linearLayout = findViewById(R.id.pILinearLayout);

        // Thêm nhiều ImageView vào LinearLayout
        ProductImageDao productImageDao = new ProductImageDao(this);
        producImages = productImageDao.getAllProductImagesFromID(productId);
        for(int i =0;i < producImages.size();i++) {

            ImageView imageView = new ImageView(this);

            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    1000, // Chiều rộng (px)
                    1000  // Chiều cao (px)
            ));
            imageView.setPadding(2, 10, 2, 10);
            imageView.setImageBitmap(producImages.get(i).getImage()); // ảnh
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            // Thêm ImageView vào LinearLayout
            linearLayout.addView(imageView);

        }
    }
}