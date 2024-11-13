package com.pack1.quanlybangiaythethao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Database.db";
    private static final int DATABASE_VERSION = 1;
    private  static final  String USER_TABLE = "CREATE TABLE User(user_id integer primary key autoincrement," +
            "username text UNIQUE,"+
            "password text,"+
            "fname text," +
            "lname text," +
            "birth text," +
            "role text," +
            "gender text," +
            "gmail text,"+
            "numbers text,"+
            "dateadded text," +
            "avatar blob"+
            ");";
    private static final String  PRODUCT_TABLE = "CREATE TABLE Product (" +
            "product_id INTEGER primary key autoincrement ," +
            "name TEXT UNIQUE," +
            "quantity INTEGER," +
            "size TEXT," +
            "color TEXT," +
            "gender TEXT," +
            "price REAL," +
            "brand TEXT," +
            "description TEXT," +
            "dateadded TEXT," +
            "pd_image blob,"+
            "rating text"+
            ")";
    private static final String PRODUCT_IMAGE_TABLE = "CREATE TABLE Product_images (" +
            "    img_id INTEGER PRIMARY KEY autoincrement," +
            "    Image BLOB," +
            "    product_id INTEGER," +
            "    FOREIGN KEY (product_id) REFERENCES Product(product_id)  ON DELETE CASCADE " +
            ")";
    private static final String ORDER_TABLE = "CREATE TABLE User_order (" +
            "    order_id INTEGER PRIMARY KEY autoincrement," +
            "    quantity INTEGER," +
            "    total_price REAL," +
            "    status TEXT," +
            "    user_id INTEGER," +
            "    product_id INTEGER," +
            "    dateorder TEXT," +
            "    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE SET NULL," +
            "    FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE SET NULL" +
            ")";
    public static final String REVIEW_TABLE ="CREATE TABLE Review (" +
            "review_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "review_text TEXT," +
            "rating INTEGER CHECK(rating BETWEEN 1 AND 5)," +
            "reviewdate TEXT,"+
            "user_id INTEGER," +
            "product_id INTEGER," +
            "FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE ,"+
            "FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE CASCADE"+
            ")";
    public static final String SHOPPING_CART_TABLE = "CREATE TABLE Shopping_cart (" +
            "  user_id INTEGER," +
            "  product_id INTEGER," +
            "  cart_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE ,"+
            "  FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE SET NULL"+
            ");";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);//tạo
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE);
        db.execSQL(PRODUCT_TABLE);
        db.execSQL(PRODUCT_IMAGE_TABLE);
        db.execSQL(ORDER_TABLE);
        db.execSQL(REVIEW_TABLE);
        db.execSQL(SHOPPING_CART_TABLE);
    }
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.execSQL("PRAGMA foreign_keys = ON;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + "User");
        db.execSQL("DROP TABLE IF EXISTS " + "Product");
        db.execSQL("DROP TABLE IF EXISTS " + "Product_images");
        db.execSQL("DROP TABLE IF EXISTS " + "User_order");
        db.execSQL("DROP TABLE IF EXISTS " + "Review");
        db.execSQL("DROP TABLE IF EXISTS " + "Shopping_cart");
        onCreate(db);
    }

//    public void resetDatabase(SQLiteDatabase db)
//    {
//        db.execSQL("DROP TABLE IF EXISTS " + "User");
//        db.execSQL("DROP TABLE IF EXISTS " + "Product");
//        db.execSQL("DROP TABLE IF EXISTS " + "Product_images");
//        db.execSQL("DROP TABLE IF EXISTS " + "User_order");
//        onCreate(db);
//    }


}
