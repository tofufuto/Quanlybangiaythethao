package com.pack1.admin;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pack1.quanlybangiaythethao.DatabaseHelper;
import com.pack1.quanlybangiaythethao.ProducImage;
import com.pack1.quanlybangiaythethao.Product;
import com.pack1.quanlybangiaythethao.ProductDao;
import com.pack1.quanlybangiaythethao.ProductImageDao;
import com.pack1.quanlybangiaythethao.R;
import com.pack1.quanlybangiaythethao.Staticstuffs;

import java.util.ArrayList;
import custom_adapter.GridViewImageAdapter;

public class AddProductActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    SQLiteDatabase database;
    private final int PRODUCT_IMAGE_LIMIT = 5;
    private final int PICK_IMAGE_REQUEST = 1;
    private final int PICK_MUTIPLE_IMAGES_REQUEST = 2;
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
        setContentView(R.layout.activity_add_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        gridViewImageAdapter = new GridViewImageAdapter(this,productImagesBitmap,this.getLayoutInflater());
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
        gridView = findViewById(R.id.productImagesDisplay);
        btnAddMutipleImages = findViewById(R.id.btnAddMutiImage);
        Toolbar toolbar = findViewById(R.id.toolbaraddproduct);
        setSupportActionBar(toolbar);

        createImageSelection();

        buttonToAddProduct();

        fixNestedScrollViewofDescription();

        createMutipleImageSelection();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);// set result để báo cho Administrator để load lại sản phẩm
        finish();
    }

    private void buttonToAddProduct() {
        btnAddProduct.setOnClickListener(view -> {
            {
                new AddProductToDatabaseAsync(this).execute();
            }
        });
    }

    private int AddProductToDatabase(Context context) {

        String gender = rdMale.isChecked() ? Staticstuffs.MALE : Staticstuffs.FEMALE;

        int rs;
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
            rs = (int) productDao.addProduct(product);
            return rs;
        } catch (Exception e) {
            Toast.makeText(context, "Lỗi không thể thêm sản phẩm \n (!) Kiểm tra lại thông tin điền", Toast.LENGTH_SHORT).show();
            return -1;
        }
    }


    private class AddProductToDatabaseAsync extends AsyncTask<Void,Void,Void>
    {
        int rs;
        private Context context;
        AlertDialog loadingDia;
        public AddProductToDatabaseAsync(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LayoutInflater inflater = LayoutInflater.from(context);
            AlertDialog.Builder alBuilder  = new AlertDialog.Builder(context);
            alBuilder.setTitle("Đang thêm sản phẩm vào hệ thông");
            alBuilder.setCancelable(false);
            alBuilder.setView(inflater.inflate(R.layout.dialog_loading,null));
            loadingDia = alBuilder.create();
            loadingDia.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                rs = AddProductToDatabase(context);
                if(rs != -1)
                    addProductImagesToDatabase(rs);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            loadingDia.dismiss();
            setResult(RESULT_OK);
            clearAllInputField();
            if(rs == -1)
                Toast.makeText(getApplicationContext(),"Lỗi không thêm vào được Database",Toast.LENGTH_SHORT).show();
        }
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
        productImagesBitmap.clear();
        gridViewImageAdapter.notifyDataSetChanged();
    }
    private void createImageSelection()// lấy ảnh đại diện cho product
    {
        btnAddImageMain.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
    }
    private void createMutipleImageSelection()//lấy các ảnh minh họa cho product
    {
        btnAddMutipleImages.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, PICK_MUTIPLE_IMAGES_REQUEST);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST)
            mainProductImageRequestHandler(requestCode, resultCode, data);
        else if (requestCode == PICK_MUTIPLE_IMAGES_REQUEST)
            productuctMutipleImageRequestHandler(requestCode, resultCode, data);
    }

    private void mainProductImageRequestHandler(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            imageBitmap = Staticstuffs.uriToBitmap(this,selectedImageUri);
            imageView.setImageURI(selectedImageUri);
        }
    }
    private void productuctMutipleImageRequestHandler(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_MUTIPLE_IMAGES_REQUEST && resultCode == RESULT_OK) {
            // Xử lý các ảnh được chọn
            if (data != null) {
                if (data.getClipData() != null) {
                    // Nhiều ảnh được chọn
                    int count = data.getClipData().getItemCount();
                    if(count > PRODUCT_IMAGE_LIMIT)
                    {
                        Toast.makeText(this, "Tối đa ảnh là 10", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ArrayList<Uri> imageUris = new ArrayList<>();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imageUris.add(imageUri);
                    }
                    for(int i = 0;i< count ;i++)
                    {
                        productImagesBitmap.add(Staticstuffs.uriToBitmap(this,imageUris.get(i)));
                    }
                    // Xử lý danh sách các Uri ảnh ở đây
                } else if (data.getData() != null) {
                    // Chỉ một ảnh được chọn
                    Uri imageUri = data.getData();
                    productImagesBitmap.add(Staticstuffs.uriToBitmap(this,imageUri));
                }
                gridView.setAdapter(gridViewImageAdapter);
            }
        }
    }

    private void addProductImagesToDatabase(int productId)
    {
        for(int i = 0;i < productImagesBitmap.size();i++)
        {
            ProducImage producImage = new ProducImage(productImagesBitmap.get(i),productId);
            ProductImageDao productImageDao = new ProductImageDao(this);
            productImageDao.addProductImage(producImage);
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
            onBackPressed();
        }
        return false;
    }
}