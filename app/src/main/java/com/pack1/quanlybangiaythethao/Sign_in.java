package com.pack1.quanlybangiaythethao;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.Date;

//mấy hàm biến xài chung d(ể ở cái lớp Staticstuffs
//thao tác với database xài lớp DatabaseHelper với mấy cái class Dao
//mấy hàm xóa sữa sẽ viết thêm ở mấy cái lớp Dao
//mấy cái id khóa chính của bảng là integer tự tăng nên khi add dô bảng thì đừng add id của khóa chính
public class Sign_in extends AppCompatActivity {



    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DatabaseAddingTester();
    }

    private void DatabaseAddingTester() {
        this.deleteDatabase("Database.db");// xóa luôn cái database
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();


//        User user1 = new User("huy2004","123456789","Huy","Xuan",Staticstuffs.ConvertStringtoDate("07-08-2004"),"admin","nam","1111111111","lmao420@gmail.com",null);
//        UserDao userDao1 = new UserDao(this.getApplicationContext());
//        userDao1.addUser(user1);
//
//        Product product1 = new Product("giày nike",30,"38.5","yellow","nam",1000000,"nike","giày xịn xò blah blah blah",4.3f,null);
//        ProductDao productDao1 = new ProductDao(this);
//        productDao1.addProduct(product1);
//
//        ProductImage producImage1 = new ProductImage(null,1);
//        ProductImageDao productImageDao = new ProductImageDao(this);
//        productImageDao.addProductImage(producImage1);
//
//        UserOrder userOrder1 = new UserOrder(2,2000000,"đang xử lý",1,1);
//        UserOrderDao userOrderDao1 = new UserOrderDao(this);
//        userOrderDao1.addUserOrder(userOrder1);
//
//        Review review1 = new Review("Giày xịn vkl mang sướng",5,1,1);
//        ReviewDao reviewDao1 = new ReviewDao(this);
//        reviewDao1.addReview(review1);
//
//        ShoppingCart shoppingCart1 = new ShoppingCart(1,1);
//        ShoppingCartDao shoppingCartDao1 = new ShoppingCartDao(this);
//        shoppingCartDao1.addShoppingCart(shoppingCart1);
    }
}