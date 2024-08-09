package com.example.foodiehut;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public final static String DB_NAME="dishdash.db";
    public final static int DB_VERSION=1;

    public DBHelper( Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create Users Table
        db.execSQL("CREATE TABLE Users (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE, " +
                "password TEXT, " +
                "email TEXT UNIQUE, " +
                "profile_picture BLOB, " +
                "registration_date DATETIME, " +
                "is_admin BOOLEAN)");

        // Create Administrators Table
        db.execSQL("CREATE TABLE Administrators (" +
                "admin_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "name TEXT, " +
                "email TEXT UNIQUE, " +
                "phone_number TEXT, " +
                "FOREIGN KEY(user_id) REFERENCES Users(user_id))");

        // Create Orders Table
        db.execSQL("CREATE TABLE Orders (" +
                "order_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "order_date DATETIME, " +
                "status TEXT, " +
                "total_amount REAL, " +
                "delivery_address TEXT, " +
                "promotion_code TEXT, " +
                "shop_id INTEGER, " +
                "comments TEXT, " +
                "FOREIGN KEY(user_id) REFERENCES Users(user_id), " +
                "FOREIGN KEY(shop_id) REFERENCES Shops(shop_id))");

        // Create Order_Items Table
        db.execSQL("CREATE TABLE Order_Items (" +
                "order_item_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "order_id INTEGER, " +
                "menu_item_id INTEGER, " +
                "quantity INTEGER, " +
                "price REAL, " +
                "customization_details TEXT, " +
                "FOREIGN KEY(order_id) REFERENCES Orders(order_id), " +
                "FOREIGN KEY(menu_item_id) REFERENCES Menu_Items(menu_item_id))");

        // Create Menu_Items Table
        db.execSQL("CREATE TABLE Menu_Items (" +
                "menu_item_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "description TEXT, " +
                "price REAL, " +
                "category TEXT, " +
                "image BLOB)");

        // Create Promotions Table
        db.execSQL("CREATE TABLE Promotions (" +
                "promotion_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "code TEXT UNIQUE, " +
                "discount_percentage INTEGER, " +
                "start_date DATETIME, " +
                "end_date DATETIME)");

        // Create Shops Table
        db.execSQL("CREATE TABLE Shops (" +
                "shop_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "address TEXT, " +
                "latitude REAL, " +
                "longitude REAL, " +
                "rating REAL)");

        // Create Shop_Menu_Items Table
        db.execSQL("CREATE TABLE Shop_Menu_Items (" +
                "shop_menu_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "shop_id INTEGER, " +
                "menu_item_id INTEGER, " +
                "availability BOOLEAN, " +
                "FOREIGN KEY(shop_id) REFERENCES Shops(shop_id), " +
                "FOREIGN KEY(menu_item_id) REFERENCES Menu_Items(menu_item_id))");

        // Create Ratings Table
        db.execSQL("CREATE TABLE Ratings (" +
                "rating_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "menu_item_id INTEGER, " +
                "rating INTEGER, " +
                "comment TEXT, " +
                "rating_date DATETIME, " +
                "FOREIGN KEY(user_id) REFERENCES Users(user_id), " +
                "FOREIGN KEY(menu_item_id) REFERENCES Menu_Items(menu_item_id))");

        // Create User_Favorites Table
        db.execSQL("CREATE TABLE User_Favorites (" +
                "favorite_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "menu_item_id INTEGER, " +
                "FOREIGN KEY(user_id) REFERENCES Users(user_id), " +
                "FOREIGN KEY(menu_item_id) REFERENCES Menu_Items(menu_item_id))");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop existing tables
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Administrators");
        db.execSQL("DROP TABLE IF EXISTS Orders");
        db.execSQL("DROP TABLE IF EXISTS Order_Items");
        db.execSQL("DROP TABLE IF EXISTS Menu_Items");
        db.execSQL("DROP TABLE IF EXISTS Promotions");
        db.execSQL("DROP TABLE IF EXISTS Shops");
        db.execSQL("DROP TABLE IF EXISTS Shop_Menu_Items");
        db.execSQL("DROP TABLE IF EXISTS Ratings");
        db.execSQL("DROP TABLE IF EXISTS User_Favorites");

        // Create new tables
        onCreate(db);
    }
}
