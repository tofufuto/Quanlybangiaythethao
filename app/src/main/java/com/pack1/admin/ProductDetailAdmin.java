package com.pack1.admin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
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

import com.pack1.quanlybangiaythethao.ProducImage;
import com.pack1.quanlybangiaythethao.Product;
import com.pack1.quanlybangiaythethao.ProductDao;
import com.pack1.quanlybangiaythethao.ProductImageDao;
import com.pack1.quanlybangiaythethao.R;
import com.pack1.quanlybangiaythethao.ReviewDao;
import com.pack1.quanlybangiaythethao.Staticstuffs;

import java.util.ArrayList;

import custom_adapter.GridViewImageAdapter;
import custom_adapter.ReviewDisplayAdapter;

public class ProductDetailAdmin extends AppCompatActivity {

    Toolbar toolbar;
    Intent intent;

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
    ListView reviewListdisplay ;
    int thisProductID;
    String clickedProductName;

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
        reviewListdisplay = findViewById(R.id.comment_detail_display);

        intent = getIntent();
        toolbar = findViewById(R.id.toolbarproductdetail);
        setSupportActionBar(toolbar);
        clickedProductName = intent.getStringExtra("clickedProductName");

        thisProductID = new ProductDao(this).getProductIdByName(clickedProductName);

        loadProductFromDatabase(clickedProductName);

        createImageSelection();
        createMutipleImageSelection();

        updateProductToDatabase();

    }

    private void updateProductToDatabase() {
        btnAddProduct.setOnClickListener(view -> {
            {
                new UpdateProductToDatabaseAsync(this).execute();
//                int rs = UpdateProductToDatabase(clickedProductName,this);
//                if(rs == 1)
//                    updateProductImagesToDatabase(this);
            }
        });
    }

    private void loadProductFromDatabase(String pName)
    {
        ProductDao productDao = new ProductDao(this);
        Product product = productDao.getProductName(pName);
        ProductImageDao productImageDao = new ProductImageDao(this);
        ReviewDao reviewDao = new ReviewDao(this);
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

        //pImageBitmap = product.getPdImage();

        productImagesBitmap = Staticstuffs.productImageListToBitmapList(productImageDao.getAllProductImagesFromID(thisProductID));
        GridViewImageAdapter adapter = new GridViewImageAdapter(this,productImagesBitmap,this.getLayoutInflater());
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        ReviewDisplayAdapter reviewDisplayAdapter = new ReviewDisplayAdapter(this,reviewDao.getAllReviewByProuctId(thisProductID),this.getLayoutInflater());
        reviewListdisplay.setAdapter(reviewDisplayAdapter);

    }
    private class UpdateProductToDatabaseAsync extends AsyncTask<Void,Void,Void>
    {
        private Context context;
        AlertDialog loadingDia;
        public UpdateProductToDatabaseAsync(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LayoutInflater inflater = LayoutInflater.from(context);
            AlertDialog.Builder alBuilder  = new AlertDialog.Builder(context);
            alBuilder.setTitle("Đang sửa sản phẩm trong hệ thông");
            alBuilder.setCancelable(false);
            alBuilder.setView(inflater.inflate(R.layout.dialog_loading,null));
            loadingDia = alBuilder.create();
            loadingDia.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                int rs = UpdateProductToDatabase(clickedProductName,context);
                if(rs == 1)
                    updateProductImagesToDatabase(context);
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
        }
    }

    private int UpdateProductToDatabase(String pName,Context context){
        try {
            String gender = rdMale.isChecked() ? Staticstuffs.MALE : Staticstuffs.FEMALE;
            Product product = new Product(nameInput.getText().toString(), Integer.parseInt(quantityInput.getText().toString()),
                    sizeInput.getText().toString(),
                    colorInput.getText().toString(),
                    gender,
                    Float.parseFloat(priceInput.getText().toString()),
                    brandInput.getText().toString(),
                    descriptionInput.getText().toString(),
                    0f,
                    imageBitmap);
            ProductDao productDao = new ProductDao(context);
            productDao.updateProductyId(thisProductID, product);
            ProductImageDao productImageDao = new ProductImageDao(context);
            productImageDao.deleteAllProductImageByProductId(thisProductID);
            return 1;
        }catch (Exception e)
        {
            Toast.makeText(this,"!!!",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return  -1;
    }

    private void updateProductImagesToDatabase(Context context) {
        ProductImageDao productImageDao = new ProductImageDao(context);
        for(int i = 0;i < productImagesBitmap.size();i++)
        {
            ProducImage producImage = new ProducImage(productImagesBitmap.get(i),thisProductID);
            productImageDao.addProductImage(producImage);
        }
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
                productImagesBitmap.clear();
                if (data.getClipData() != null) {
                    // Nhiều ảnh được chọn
                    int count = data.getClipData().getItemCount();
                    if(count > PRODUCT_IMAGE_LIMIT)
                    {
                        Toast.makeText(this, "Tối đa ảnh là 5", Toast.LENGTH_SHORT).show();
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
                GridViewImageAdapter adapter = new GridViewImageAdapter(this,productImagesBitmap,this.getLayoutInflater());
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }
    private void deleteProduct(){
        ProductDao productDao = new ProductDao(this);
        if(productDao.deleteProductFromDatabaseByID(thisProductID) == 0)
            Toast.makeText(this,"Không xóa được",Toast.LENGTH_SHORT).show();
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
        if(id ==R.id.delbtn){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Xác nhận xóa"); // Tiêu đề của dialog.
            builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?"); // Nội dung dialog.

// Nút Yes
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Xử lý khi người dùng nhấn Yes
                    deleteProduct();
                    setResult(RESULT_OK);
                    onBackPressed();
                }
            });

// Nút No
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Xử lý khi người dùng nhấn No
                    dialog.dismiss(); // Đóng dialog
                }
            });

// Tạo và hiển thị AlertDialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return false;
    }

}