package com.example.foodiehut;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class PlacedOrders extends AppCompatActivity {
    private DBHelper dbHelper;
    private List<MyCart> myCartList;
    private double totalPrice;
    private SQLiteDatabase db;
    private TextView orderNumberTextView, totalPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placed_orders);

        // Initialize TextViews
        orderNumberTextView = findViewById(R.id.order_number);
        totalPriceTextView = findViewById(R.id.total_price);

        // Initialize DBHelper
        dbHelper = new DBHelper(this);

        // Retrieve cart items and total price from intent
        Intent intent = getIntent();
        totalPrice = intent.getDoubleExtra("total_price", 0.0);
        myCartList = (List<MyCart>) intent.getSerializableExtra("itemlist");

        if (myCartList == null || myCartList.isEmpty()) {
            Toast.makeText(this, "No items to place an order for", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if no items
            return;
        }

        // Open the database for writing
        db = dbHelper.getWritableDatabase();

        // Place the order and retrieve order ID
        long orderId = placeOrder();

        // Display order ID and total price
        if (orderId != -1) {
            orderNumberTextView.setText(String.valueOf(orderId));
            totalPriceTextView.setText(String.format("Total Price: $%.2f", totalPrice));
        }
    }

    private long placeOrder() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);
        String address = sharedPreferences.getString("user_address", "");

        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return -1;
        }

        long orderId = -1;
        try {
            // Insert into Orders table
            ContentValues orderValues = new ContentValues();
            orderValues.put("user_id", userId);
            orderValues.put("total_price", totalPrice);
            orderValues.put("delivery_location", address);
            orderValues.put("status", "pending");

            orderId = db.insert("Orders", null, orderValues);

            if (orderId != -1) {
                // Insert into OrderItems table
                for (MyCart item : myCartList) {
                    ContentValues orderItemValues = new ContentValues();
                    orderItemValues.put("order_id", orderId);
                    orderItemValues.put("item_id", getItemIdByName(item.getProductname())); // Fetch item_id
                    orderItemValues.put("quantity", item.getTotalQuantity());

                    db.insert("OrderItems", null, orderItemValues);
                }

                deleteCartItems(userId);
                Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error placing order", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        return orderId;
    }

    private int getItemIdByName(String name) {
        Cursor cursor = null;
        int itemId = -1;

        try {
            String[] projection = {"item_id"};
            String selection = "name = ?";
            String[] selectionArgs = {name};

            cursor = db.query(
                    "MenuItems",
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                itemId = cursor.getInt(cursor.getColumnIndexOrThrow("item_id"));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return itemId;
    }

    private void deleteCartItems(int userId) {
        try {
            db.delete("Cart", "user_id = ?", new String[]{String.valueOf(userId)});
        } catch (Exception e) {
            Toast.makeText(this, "An error occurred while deleting cart items: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}