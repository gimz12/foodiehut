package com.example.foodiehut.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodiehut.DBHelper;
import com.example.foodiehut.LoginUser;
import com.example.foodiehut.R;

public class AdminHomeActivity extends AppCompatActivity {

    private TextView tvTotalOrders, tvMostPurchasedItem, tvTotalRevenue;
    private Button btnAddMenuItems, btnManageMenuItems, btnManageOrders, btnGetUserDetails, btnLogout;
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
        btnLogout = findViewById(R.id.btn_logout);

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

        btnLogout.setOnClickListener(view -> showLogoutConfirmationDialog());
    }

    private void displayDashboardData() {
        int totalOrders = dbHelper.getTotalOrders();
        String mostPurchasedItem = dbHelper.getMostPurchasedItem();
        double totalRevenue = dbHelper.getTotalRevenue();

        tvTotalOrders.setText("Total Orders: " + totalOrders);
        tvMostPurchasedItem.setText("Most Purchased Item: " + mostPurchasedItem);
        tvTotalRevenue.setText("Total Revenue: " + totalRevenue);
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Redirect to login activity and finish current activity
                        Intent intent = new Intent(AdminHomeActivity.this, LoginUser.class);
                        startActivity(intent);
                        finish(); // Close the current activity
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }
}
