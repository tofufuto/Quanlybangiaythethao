package com.pack1.quanlybangiaythethao;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddProductActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    SQLiteDatabase database;
    private final int PICK_IMAGE_REQUEST = 1;
    EditText nameInput,quantityInput,sizeInput,colorInput,brandInput,descriptionInput,priceInput;
    RadioButton rdMale,rdFemale;
    Button btnAddImageMain,btnAddProduct;
    ImageView imageView;
    Bitmap imageBitmap;
    byte[] imageByteArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    dbHelper = new DatabaseHelper(this);
    database = dbHelper.getWritableDatabase();
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

    Toolbar toolbar = findViewById(R.id.toolbaraddproduct);
    setSupportActionBar(toolbar);

        createImageSelection();



        buttonToAddProduct();

        fixNestedScrollViewofDescription();
    }

    private void buttonToAddProduct() {
        btnAddProduct.setOnClickListener(view -> {
            String gender = rdMale.isChecked() ? "Nam" : "Nữ";
            try {
                Product product = new Product(nameInput.getText().toString().trim(),
                        Integer.parseInt(quantityInput.getText().toString().trim()),
                        sizeInput.getText().toString().trim(),
                        colorInput.getText().toString().trim(),
                        gender,
                        Float.parseFloat(priceInput.getText().toString().trim()),
                        brandInput.getText().toString().trim(),
                        descriptionInput.getText().toString().trim(),
                        0f,
                        imageBitmap);
                ProductDao productDao = new ProductDao(this);
                if (productDao.addProduct(product) == -1)
                    Toast.makeText(getApplicationContext(), "Lỗi không thể thêm sản phẩm \n (!) Xem lại tên sản phẩm", Toast.LENGTH_SHORT).show();
                else
                    clearAllInputField();
            }catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), "Lỗi không thể thêm sản phẩm \n (!) Kiểm tra lại thông tin điền", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void fixNestedScrollViewofDescription()
    {
        descriptionInput.setOnTouchListener((v, event) -> {
            // Ngăn ScrollView chặn sự kiện cuộn khi người dùng chạm vào EditText
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
            }
            // Khôi phục lại khả năng cuộn cho ScrollView khi ngừng chạm
            if (event.getAction() == MotionEvent.ACTION_UP) {
                v.getParent().requestDisallowInterceptTouchEvent(false);
            }
            return false; // Trả về false để vẫn cho phép xử lý sự kiện cuộn của EditText
        });
    }
    private  void clearAllInputField()
    {
        nameInput.setText("");
        quantityInput.setText("");
        sizeInput.setText("");
        colorInput.setText("");
        brandInput.setText("");
        descriptionInput.setText("");
        priceInput.setText("");
        imageView.setImageResource(R.drawable.ic_launcher_background);
    }
    private void createImageSelection()
    {
        btnAddImageMain.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            imageBitmap = Staticstuffs.uriToBitmap(getApplicationContext(),selectedImageUri);
            imageView.setImageBitmap(imageBitmap);
            imageByteArray = Staticstuffs.bitmapToByteArray(imageBitmap);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_product_action_bar,menu);
        return true;
    }

    @Override// cái nút back đi ra acti khác
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.btnback) {
            Intent intent = new Intent(this, AdministratorProductSelect.class);
            startActivity(intent);
        }
        return false;
    }
}