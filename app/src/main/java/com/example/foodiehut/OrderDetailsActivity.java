package com.example.foodiehut;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderDetailsAdapter orderDetailsAdapter;
    private List<OrderItem> orderItemList;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        recyclerView = findViewById(R.id.recycler_view_order_details);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBHelper(this);

        int orderId = getIntent().getIntExtra("order_id", -1);
        if (orderId != -1) {
            orderItemList = dbHelper.getOrderItemsByOrderId(orderId);
            orderDetailsAdapter = new OrderDetailsAdapter(this, orderItemList);
            recyclerView.setAdapter(orderDetailsAdapter);
        }
    }
}