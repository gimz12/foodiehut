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

    private static final String TABLE_ORDERS = "Orders";
    private static final String COLUMN_ORDER_ID = "order_id";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_ORDER_DATE = "order_date";
    private static final String COLUMN_TOTAL_PRICE = "total_price";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_DELIVERY_LOCATION = "delivery_location";
    private static final String COLUMN_PROMO_CODE = "promo_code";

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
                "order_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                "total_price REAL NOT NULL, " +
                "status TEXT NOT NULL CHECK(status IN ('pending', 'processed', 'delivered', 'cancelled')), " +
                "delivery_location TEXT, " +
                "promo_code TEXT, " +
                "FOREIGN KEY(user_id) REFERENCES Users(user_id))");

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

    public void deleteCartItem(String productName, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Cart", "productName = ? AND user_id = ?", new String[]{productName, String.valueOf(userId)});
        db.close();
    }

    public List<Order> getOrderHistoryByUserId(int userId) {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM Orders WHERE user_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int orderId = cursor.getInt(cursor.getColumnIndexOrThrow("order_id"));
                String orderDate = cursor.getString(cursor.getColumnIndexOrThrow("order_date"));
                double totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("total_price"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                String deliveryLocation = cursor.getString(cursor.getColumnIndexOrThrow("delivery_location"));
                String promoCode = cursor.getString(cursor.getColumnIndexOrThrow("promo_code"));

                Order order = new Order(orderId, userId, orderDate, totalPrice, status, deliveryLocation, promoCode);
                orderList.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orderList;
    }

    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Orders",  // Assuming your table name is "Orders"
                new String[]{"order_id", "order_date", "total_price", "status", "delivery_location", "promo_code"}, // Select the columns you want
                "user_id = ?", // Where clause
                new String[]{String.valueOf(userId)}, // Where clause arguments
                null, null, "order_date DESC"); // Order by date descending

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int orderId = cursor.getInt(cursor.getColumnIndexOrThrow("order_id"));
                String orderDate = cursor.getString(cursor.getColumnIndexOrThrow("order_date"));
                double totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("total_price"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                String deliveryLocation = cursor.getString(cursor.getColumnIndexOrThrow("delivery_location"));
                String promoCode = cursor.getString(cursor.getColumnIndexOrThrow("promo_code"));

                Order order = new Order(orderId, userId, orderDate, totalPrice, status, deliveryLocation, promoCode);
                orders.add(order);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return orders;
    }

    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> orderItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM OrderItems WHERE order_id = ?", new String[]{String.valueOf(orderId)});

        if (cursor.moveToFirst()) {
            do {
                int orderItemId = cursor.getInt(cursor.getColumnIndexOrThrow("order_item_id"));
                int itemId = cursor.getInt(cursor.getColumnIndexOrThrow("item_id"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                String customization = cursor.getString(cursor.getColumnIndexOrThrow("customization"));

                OrderItem orderItem = new OrderItem(orderItemId, orderId, itemId, quantity, customization);
                orderItemList.add(orderItem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return orderItemList;
    }


}
