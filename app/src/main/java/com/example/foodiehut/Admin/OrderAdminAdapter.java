package com.example.foodiehut.Admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodiehut.R;

import java.util.ArrayList;

public class OrderAdminAdapter extends RecyclerView.Adapter<OrderAdminAdapter.OrderViewHolder> {

    private ArrayList<OrderAdmin> ordersList;
    private OnOrderClickListener onOrderClickListener;

    public OrderAdminAdapter(ArrayList<OrderAdmin> ordersList, OnOrderClickListener onOrderClickListener) {
        this.ordersList = ordersList;
        this.onOrderClickListener = onOrderClickListener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_admin_card, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderAdmin order = ordersList.get(position);
        holder.orderIdTextView.setText(String.valueOf(order.getOrderId()));
        holder.totalPriceTextView.setText(String.valueOf(order.getTotalPrice()));
        holder.statusTextView.setText(order.getStatus());
        holder.orderDateTextView.setText(order.getOrderDate());
        holder.deliveryLocationTextView.setText(order.getDeliveryLocation());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOrderClickListener.onOrderClick(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public interface OnOrderClickListener {
        void onOrderClick(OrderAdmin orderAdmin);
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView orderIdTextView;
        TextView totalPriceTextView;
        TextView statusTextView;
        TextView orderDateTextView;
        TextView deliveryLocationTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.order_id);
            totalPriceTextView = itemView.findViewById(R.id.total_price);
            statusTextView = itemView.findViewById(R.id.status);
            orderDateTextView = itemView.findViewById(R.id.order_date);
            deliveryLocationTextView = itemView.findViewById(R.id.delivery_location);
        }
    }
}

