package com.example.foodiehut;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public final static String DB_NAME="foodiehut.db";
    public final static int DB_VERSION=1;

    public DBHelper( Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE Admins (" +
                "admin_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "email TEXT UNIQUE NOT NULL, " +
                "profile_image BLOB, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)");

        db.execSQL("CREATE TABLE Users (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "email TEXT UNIQUE NOT NULL, " +
                "profile_image BLOB, " +
                "address TEXT, " +
                "phone_number TEXT, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)");

        db.execSQL("CREATE TABLE MenuItems (" +
                "    item_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    name TEXT NOT NULL," +
                "    description TEXT," +
                "    price REAL NOT NULL," +
                "    availability BOOLEAN NOT NULL DEFAULT 1," +
                "    image BLOB," +
                "    category TEXT NOT NULL CHECK(category IN ('Pasta', 'Pizza', 'Salad', 'Soup', 'Burger', 'Submarine', 'Rice'))," +
                "    created_at DATETIME DEFAULT CURRENT_TIMESTAMP)");

        db.execSQL("CREATE TABLE Orders (" +
                "order_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "admin_id INTEGER, " +
                "order_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                "total_price REAL NOT NULL, " +
                "status TEXT NOT NULL CHECK(status IN ('pending', 'processed', 'delivered', 'cancelled')), " +
                "delivery_location TEXT, " +
                "promo_code TEXT, " +
                "FOREIGN KEY(user_id) REFERENCES Users(user_id), " +
                "FOREIGN KEY(admin_id) REFERENCES Admins(admin_id))");

        db.execSQL("CREATE TABLE OrderItems (" +
                "order_item_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "order_id INTEGER NOT NULL, " +
                "item_id INTEGER NOT NULL, " +
                "quantity INTEGER NOT NULL, " +
                "customization TEXT, " +
                "FOREIGN KEY(order_id) REFERENCES Orders(order_id), " +
                "FOREIGN KEY(item_id) REFERENCES MenuItems(item_id))");

        db.execSQL("CREATE TABLE Ratings (" +
                "rating_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "item_id INTEGER NOT NULL, " +
                "rating INTEGER NOT NULL CHECK(rating BETWEEN 1 AND 5), " +
                "comment TEXT, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(user_id) REFERENCES Users(user_id), " +
                "FOREIGN KEY(item_id) REFERENCES MenuItems(item_id))");

        db.execSQL("CREATE TABLE Promotions (" +
                "promo_code TEXT PRIMARY KEY, " +
                "discount INTEGER NOT NULL CHECK(discount BETWEEN 10 AND 30), " +
                "description TEXT)");

        db.execSQL("CREATE TABLE Notifications (" +
                "notification_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "message TEXT NOT NULL, " +
                "sent_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(user_id) REFERENCES Users(user_id))");

        db.execSQL("CREATE TABLE Analytics (" +
                "analytics_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "month TEXT NOT NULL, " +
                "highest_demand_item_id INTEGER NOT NULL, " +
                "highest_rating_item_id INTEGER NOT NULL, " +
                "total_orders INTEGER NOT NULL, " +
                "total_revenue REAL NOT NULL, " +
                "FOREIGN KEY(highest_demand_item_id) REFERENCES MenuItems(item_id), " +
                "FOREIGN KEY(highest_rating_item_id) REFERENCES MenuItems(item_id))");

        //Cart Table
        db.execSQL("CREATE TABLE Cart (" +
                "cart_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "productName TEXT NOT NULL, " +
                "productPrice REAL NOT NULL, " +
                "productDate TEXT NOT NULL, " +
                "productTime TEXT NOT NULL, " +
                "totalQuantity INTEGER NOT NULL, " +
                "totalPrice REAL NOT NULL, " +
                "FOREIGN KEY(user_id) REFERENCES Users(user_id))");


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop existing tables
        db.execSQL("DROP TABLE IF EXISTS Admins");
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS MenuItems");
        db.execSQL("DROP TABLE IF EXISTS Orders");
        db.execSQL("DROP TABLE IF EXISTS OrderItems");
        db.execSQL("DROP TABLE IF EXISTS Ratings");
        db.execSQL("DROP TABLE IF EXISTS Promotions");
        db.execSQL("DROP TABLE IF EXISTS Notifications");
        db.execSQL("DROP TABLE IF EXISTS Analytics");

        // Create new tables
        onCreate(db);
    }

    public List<MyCart> getCartItemsByUserId(int userId) {
        List<MyCart> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Cart WHERE user_id = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            // Check the indices of each column
            int productNameIndex = cursor.getColumnIndex("productName");
            int productPriceIndex = cursor.getColumnIndex("productPrice");
            int productDateIndex = cursor.getColumnIndex("productDate");
            int productTimeIndex = cursor.getColumnIndex("productTime");
            int totalQuantityIndex = cursor.getColumnIndex("totalQuantity");
            int totalPriceIndex = cursor.getColumnIndex("totalPrice");

            do {
                // Ensure column indices are valid before accessing them
                if (productNameIndex != -1 && productPriceIndex != -1 && productDateIndex != -1 &&
                        productTimeIndex != -1 && totalQuantityIndex != -1 && totalPriceIndex != -1) {

                    String productName = cursor.getString(productNameIndex);
                    double productPrice = cursor.getDouble(productPriceIndex);
                    String productDate = cursor.getString(productDateIndex);
                    String productTime = cursor.getString(productTimeIndex);
                    int totalQuantity = cursor.getInt(totalQuantityIndex);
                    double totalPrice = cursor.getDouble(totalPriceIndex);

                    MyCart item = new MyCart(productName, productPrice, productDate, productTime, totalQuantity, totalPrice);
                    cartItems.add(item);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartItems;
    }
}
