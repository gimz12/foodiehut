package com.example.foodiehut.Admin;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodiehut.DBHelper;
import com.example.foodiehut.R;

import java.util.ArrayList;

public class ManageOrdersAdmin extends AppCompatActivity {

    private RecyclerView ordersRecyclerView;
    private OrderAdminAdapter ordersAdapter;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_orders_admin);

        ordersRecyclerView = findViewById(R.id.orders_recycler_view);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBHelper(this);
        loadOrders();
    }

    private void loadOrders() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Orders", null);
        ArrayList<OrderAdmin> ordersList = new ArrayList<>();

        while (cursor.moveToNext()) {
            int orderId = cursor.getInt(cursor.getColumnIndex("order_id"));
            double totalPrice = cursor.getDouble(cursor.getColumnIndex("total_price"));
            String status = cursor.getString(cursor.getColumnIndex("status"));
            String orderDate = cursor.getString(cursor.getColumnIndex("order_date"));
            String deliveryLocation = cursor.getString(cursor.getColumnIndex("delivery_location"));
            ordersList.add(new OrderAdmin(orderId, totalPrice, status, orderDate, deliveryLocation));
        }
        cursor.close();

        ordersAdapter = new OrderAdminAdapter(ordersList, new OrderAdminAdapter.OnOrderClickListener() {
            @Override
            public void onOrderClick(OrderAdmin order) {
                Intent intent = new Intent(ManageOrdersAdmin.this, ManageOrderStatusAdmin.class);
                intent.putExtra("order_id", order.getOrderId());
                startActivity(intent);
            }
        });
        ordersRecyclerView.setAdapter(ordersAdapter);
    }
}
