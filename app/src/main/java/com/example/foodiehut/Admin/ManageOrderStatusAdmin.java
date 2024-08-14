package com.example.foodiehut.Admin;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodiehut.DBHelper;
import com.example.foodiehut.R;

public class ManageOrderStatusAdmin extends AppCompatActivity {

    private TextView orderIdTextView;
    private TextView statusTextView;
    private TextView deliveryLocationTextView;
    private TextView orderDateTextView;
    private Button acceptOrderButton;
    private Button cancelOrderButton;
    private Button confirmDeliveryButton;

    private DBHelper dbHelper;
    private int orderId;
    private String currentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_order_status_admin);

        orderIdTextView = findViewById(R.id.order_id);
        statusTextView = findViewById(R.id.status);
        deliveryLocationTextView = findViewById(R.id.delivery_location);
        orderDateTextView = findViewById(R.id.order_date);
        acceptOrderButton = findViewById(R.id.accept_order_button);
        cancelOrderButton = findViewById(R.id.cancel_order_button);
        confirmDeliveryButton = findViewById(R.id.confirm_delivery_button);

        dbHelper = new DBHelper(this);

        // Get the order ID from the Intent
        Intent intent = getIntent();
        orderId = intent.getIntExtra("order_id", -1);

        if (orderId != -1) {
            loadOrderDetails();
        }

        acceptOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateOrderStatus("processed");
            }
        });

        cancelOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateOrderStatus("cancelled");
            }
        });

        confirmDeliveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateOrderStatus("delivered");
            }
        });
    }

    private void loadOrderDetails() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM Orders WHERE order_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(orderId)});

        if (cursor.moveToFirst()) {
            currentStatus = cursor.getString(cursor.getColumnIndex("status"));
            double totalPrice = cursor.getDouble(cursor.getColumnIndex("total_price"));
            String orderDate = cursor.getString(cursor.getColumnIndex("order_date"));
            String deliveryLocation = cursor.getString(cursor.getColumnIndex("delivery_location"));

            orderIdTextView.setText(String.format("Order ID: %d", orderId));
            statusTextView.setText(String.format("Status: %s", currentStatus));
            deliveryLocationTextView.setText(String.format("Delivery Location: %s", deliveryLocation));
            orderDateTextView.setText(String.format("Order Date: %s", orderDate));

            updateButtonVisibility();
        }

        cursor.close();
    }

    private void updateOrderStatus(String newStatus) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "UPDATE Orders SET status = ? WHERE order_id = ?";
        db.execSQL(query, new String[]{newStatus, String.valueOf(orderId)});
        currentStatus = newStatus;
        statusTextView.setText(String.format("Status: %s", currentStatus));
        updateButtonVisibility();
        Toast.makeText(this, "Order status updated to " + newStatus, Toast.LENGTH_SHORT).show();
    }

    private void updateButtonVisibility() {
        acceptOrderButton.setVisibility(View.GONE);
        cancelOrderButton.setVisibility(View.GONE);
        confirmDeliveryButton.setVisibility(View.GONE);

        switch (currentStatus) {
            case "pending":
                acceptOrderButton.setVisibility(View.VISIBLE);
                cancelOrderButton.setVisibility(View.VISIBLE);
                break;
            case "processed":
                confirmDeliveryButton.setVisibility(View.VISIBLE);
                break;
        }
    }
}
