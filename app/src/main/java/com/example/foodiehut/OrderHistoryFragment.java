package com.example.foodiehut;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class OrderHistoryFragment extends Fragment implements OrderAdapter.OnOrderClickListener {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;
    private DBHelper dbHelper;

    public OrderHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        recyclerView = view.findViewById(R.id.orders_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DBHelper(getContext());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);

        if (userId != -1) {
            orderList = dbHelper.getOrdersByUserId(userId);
            orderAdapter = new OrderAdapter(getContext(), orderList, this);
            recyclerView.setAdapter(orderAdapter);
        }

        return view;
    }

    @Override
    public void onOrderClick(int orderId) {
        Intent intent = new Intent(getContext(), OrderDetailsActivity.class);
        intent.putExtra("order_id", orderId);
        startActivity(intent);
    }
}