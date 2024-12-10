package com.pack1.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pack1.dao.ProductDao;
import com.pack1.dao.ShoppingCartDao;
import com.pack1.models.Product;
import com.pack1.quanlybangiaythethao.R;

import java.util.ArrayList;

import custom_adapter.ProductGridViewAdapter;

public class ShoppingCartActivity extends AppCompatActivity {

    private int currentUserId; // Chúng ta sẽ nhận currentUserId dưới dạng int
    private GridView gridView;
    private TextView emptyCartTextView;
    private ArrayList<Product> productList;
    private ShoppingCartDao shoppingCartDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        // Nhận currentUserId từ Intent dưới dạng int
        Intent intent = getIntent();
        currentUserId = intent.getIntExtra("currentUserId", -1); // -1 là giá trị mặc định nếu không nhận được giá trị hợp lệ

        // Kiểm tra nếu currentUserId không hợp lệ
        if (currentUserId == -1) {
            Toast.makeText(this, "Thông tin người dùng không hợp lệ!", Toast.LENGTH_SHORT).show();
            finish(); // Nếu không hợp lệ, đóng Activity này
            return;
        }

        // Khởi tạo ShoppingCartDao để truy xuất thông tin giỏ hàng
        shoppingCartDao = new ShoppingCartDao(this);

        // Lấy danh sách sản phẩm trong giỏ hàng của người dùng
        loadProductsInCart();

        // Thiết lập GridView và TextView thông báo giỏ hàng trống
        gridView = findViewById(R.id.gridViewCart);
        emptyCartTextView = findViewById(R.id.emptyCartTextView);

        // Kiểm tra xem giỏ hàng có sản phẩm không, nếu không, thông báo cho người dùng
        if (productList.isEmpty()) {
            gridView.setVisibility(View.GONE);
            emptyCartTextView.setVisibility(View.VISIBLE);  // Hiển thị thông báo giỏ hàng trống
        } else {
            gridView.setVisibility(View.VISIBLE);
            emptyCartTextView.setVisibility(View.GONE);  // Ẩn thông báo khi giỏ hàng có sản phẩm
        }

        // Tạo và thiết lập Adapter cho GridView
        ProductGridViewAdapter adapter = new ProductGridViewAdapter(this, productList, getLayoutInflater());
        gridView.setAdapter(adapter);

        // Xử lý sự kiện khi người dùng nhấn vào một sản phẩm trong giỏ hàng
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy tên sản phẩm từ View
                TextView productNameTextView = view.findViewById(R.id.pname);
                String productName = productNameTextView.getText().toString();

                // Chuyển đến màn hình chi tiết sản phẩm
                Intent detailIntent = new Intent(ShoppingCartActivity.this, CustomerProductDetail.class);
                detailIntent.putExtra("productName", productName);
                detailIntent.putExtra("currentUserId", String.valueOf(currentUserId)); // Truyền currentUserId dưới dạng int
                startActivity(detailIntent);
            }
        });
    }

    // Lấy danh sách sản phẩm trong giỏ hàng của người dùng
    private void loadProductsInCart() {
        ProductDao productDao = new ProductDao(this);

        // Lấy tất cả các ID sản phẩm trong giỏ hàng của currentUserId
        ArrayList<Integer> productIds = shoppingCartDao.getProductIdsByUserId(currentUserId);

        productList = new ArrayList<>();
        for (int productId : productIds) {
            // Lấy thông tin sản phẩm từ ID
            Product product = productDao.getProductById(productId);
            if (product != null) {
                productList.add(product); // Thêm sản phẩm vào danh sách nếu có
            }
        }
    }
}
