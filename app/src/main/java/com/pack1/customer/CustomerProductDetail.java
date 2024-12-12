package com.pack1.customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pack1.dao.ProductDao;
import com.pack1.dao.ProductImageDao;
import com.pack1.dao.ReviewDao;
import com.pack1.dao.ShoppingCartDao;
import com.pack1.models.ProducImage;
import com.pack1.models.Product;
import com.pack1.models.Review;
import com.pack1.models.ShoppingCart;
import com.pack1.quanlybangiaythethao.R;

import java.util.ArrayList;

import custom_adapter.ReviewDisplayAdapter;

public class CustomerProductDetail extends AppCompatActivity {

    int currentUserId,productId;
    String productName;
    TextView pdName,pdRating,pdGender,pdSize,pdColor,pdDescription,pdBrand,pdPrice;
    LinearLayout linearLayout;
    ListView reviewDisplay;
    ArrayList<ProducImage> producImages;
    Button buyBtn;
    ImageButton addToCartBtn;
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
        buyBtn = findViewById(R.id.buy);
        pdPrice = findViewById(R.id.price);
        reviewDisplay = findViewById(R.id.reviews);
        pdName = findViewById(R.id.pName);
        pdRating = findViewById(R.id.rating_c);
        pdBrand = findViewById(R.id.brand);
        pdGender = findViewById(R.id.gender);
        pdSize = findViewById(R.id.size);
        pdColor = findViewById(R.id.color);
        pdDescription = findViewById(R.id.description);
        addToCartBtn = findViewById(R.id.addCartBtn);

        //lấy currentuserid với productId dc bấm vào
        Intent ProductDetailIntent = this.getIntent();
        currentUserId = Integer.parseInt(ProductDetailIntent.getStringExtra("currentUserId"));
        productName = ProductDetailIntent.getStringExtra("productName");

        ProductDao productDao = new ProductDao(this );
        productId = productDao.getProductIdByName(productName);

        Log.d("MY LOG",productName);

        linearLayout = findViewById(R.id.pILinearLayout);

        // Thêm nhiều ImageView vào LinearLayout
        loadProductImages();
        loadProduct();
        loadReviews();

        buyBtn.setOnClickListener(new View.OnClickListener() {// nút mua cái là cái này thế acti m làm vào activity_mua_và_thanh_toán
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(),activity_mua_và_thanh_toán);
//                intent.putExtra("currentUserId",""+currentUserId);
//                intent.putExtra("productName",productName.toString());
//                startActivity(intent);
            }
        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingCart shoppingCart = new ShoppingCart(currentUserId,productId);
                ShoppingCartDao shoppingCartDao = new ShoppingCartDao(getApplicationContext());
                if (shoppingCartDao.isShoppingCartExist(shoppingCart))
                    {
                        Toast.makeText(getApplicationContext(),"Đã mark sản phẩm này rồi",Toast.LENGTH_SHORT).show();
                        return;
                    }
                long rs = shoppingCartDao.addShoppingCart(shoppingCart);
                if(rs != -1)
                    Toast.makeText(getApplicationContext(),"Đã thêm "+productName+" vào giỏ",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(),"Lỗi thêm thất bại",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProductImages() {
        ProductDao productDao = new ProductDao(this);
        ProductImageDao productImageDao = new ProductImageDao(this);
        producImages = productImageDao.getAllProductImagesFromID(productDao.getProductIdByName(productName));
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
    private float ratingCalculator(ArrayList<Review> reviews){
        if(reviews == null|| reviews.isEmpty())
            return 0f;
        float rs = 0f;
        for(int i = 0;i< reviews.size();i++)
        {
            rs += reviews.get(i).getRating();
        }
        return (rs/reviews.size());
    }

    private void loadProduct()
    {
        try {
            ReviewDao reviewDao = new ReviewDao(this);
            ProductDao productDao = new ProductDao(this);
            Product product = productDao.getProductName(productName);
            pdName.setText(product.getName());
            pdRating.setText("" + ratingCalculator(reviewDao.getAllReviewByProuctId(productDao.getProductIdByName(productName))));
            pdGender.setText(product.getGender());
            pdSize.setText(product.getSize());
            pdColor.setText(product.getColor());
            pdDescription.setText(product.getDescription());
            pdBrand.setText(product.getBrand());
            pdPrice.setText(""+ product.getPrice() +" VNĐ");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void loadReviews(){
        try{
            ReviewDao reviewDao = new ReviewDao(this);
            ProductDao productDao = new ProductDao(this);
            ReviewDisplayAdapter reviewDisplayAdapter = new ReviewDisplayAdapter(this,reviewDao.getAllReviewByProuctId(productDao.getProductIdByName(productName)),this.getLayoutInflater());
            reviewDisplay.setAdapter(reviewDisplayAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}