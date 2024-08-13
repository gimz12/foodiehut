package com.example.foodiehut;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MyCartFragment extends Fragment {

    RecyclerView recyclerView;
    MyCartAdapter myCartAdapter;
    List<MyCart> myCartList;

    private DBHelper dbHelper;
    private int userId;

    public MyCartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);

        // Set Layout Manager for RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize DBHelper
        dbHelper = new DBHelper(getContext());

        // Get user ID from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
        userId = sharedPreferences.getInt("user_id", -1);

        if (userId != -1) {
            // Fetch cart items for the user
            myCartList = dbHelper.getCartItemsByUserId(userId);

            // Log to check if items are being fetched
            Log.d("MyCartFragment", "Number of items in cart: " + myCartList.size());

            // Set up the adapter
            myCartAdapter = new MyCartAdapter(getContext(), myCartList);
            recyclerView.setAdapter(myCartAdapter);

            // Set the total price in the TextView
            double totalPrice = calculateTotalPrice(myCartList);
            TextView totalPriceTextView = view.findViewById(R.id.textView2);
            totalPriceTextView.setText(String.format("Total Price: $%.2f", totalPrice));
        }

        return view;
    }

    private double calculateTotalPrice(List<MyCart> cartItems) {
        double total = 0;
        for (MyCart item : cartItems) {
            total += item.getTotoalPrice();
        }
        return total;
    }
}
