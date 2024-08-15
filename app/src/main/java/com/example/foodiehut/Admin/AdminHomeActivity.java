package com.example.foodiehut.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodiehut.DBHelper;
import com.example.foodiehut.R;

public class AdminHomeActivity extends AppCompatActivity {

    private TextView tvTotalOrders, tvMostPurchasedItem, tvTotalRevenue;
    private Button btnAddMenuItems, btnManageMenuItems, btnManageOrders, btnGetUserDetails;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        // Initialize views
        tvTotalOrders = findViewById(R.id.tv_total_orders);
        tvMostPurchasedItem = findViewById(R.id.tv_most_purchased_item);
        tvTotalRevenue = findViewById(R.id.tv_total_revenue);
        btnAddMenuItems = findViewById(R.id.btn_add_menu_items);
        btnManageMenuItems = findViewById(R.id.btn_manage_menu_items);
        btnManageOrders = findViewById(R.id.btn_manage_orders);
        btnGetUserDetails = findViewById(R.id.btn_get_user_details);

        // Initialize database helper
        dbHelper = new DBHelper(this);

        // Retrieve and display dashboard data
        displayDashboardData();


        // Set button click listeners
        btnAddMenuItems.setOnClickListener(view -> {
            Intent intent = new Intent(AdminHomeActivity.this, AddMenuItemAdmin.class);
            startActivity(intent);
        });

        btnManageMenuItems.setOnClickListener(view -> {
            Intent intent = new Intent(AdminHomeActivity.this, ManageMenuItemAdmin.class);
            startActivity(intent);
        });

        btnManageOrders.setOnClickListener(view -> {
            Intent intent = new Intent(AdminHomeActivity.this, ManageOrdersAdmin.class);
            startActivity(intent);
        });

        btnGetUserDetails.setOnClickListener(view -> {
            Intent intent = new Intent(AdminHomeActivity.this, GetUserDetailsActivity.class);
            startActivity(intent);
        });
    }

    private void displayDashboardData() {
        int totalOrders = dbHelper.getTotalOrders();
        String mostPurchasedItem = dbHelper.getMostPurchasedItem();
        double totalRevenue = dbHelper.getTotalRevenue();

        tvTotalOrders.setText("Total Orders: " + totalOrders);
        tvMostPurchasedItem.setText("Most Purchased Item: " + mostPurchasedItem);
        tvTotalRevenue.setText("Total Revenue: " + totalRevenue);
    }
}
