package com.example.foodiehut;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;
    private OnOrderClickListener onOrderClickListener;

    // Define an interface for click events
    public interface OnOrderClickListener {
        void onOrderClick(int orderId);
    }

    public OrderAdapter(Context context, List<Order> orderList, OnOrderClickListener onOrderClickListener) {
        this.context = context;
        this.orderList = orderList;
        this.onOrderClickListener = onOrderClickListener;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.orderId.setText("Order ID: " + order.getOrderId());
        holder.orderDate.setText("Date: " + order.getOrderDate());
        holder.totalPrice.setText("Total Price: $" + order.getTotalPrice());
        holder.status.setText("Status: " + order.getStatus());
        holder.deliveryLocation.setText("Delivery Location: " + order.getDeliveryLocation());

        // Set an OnClickListener for the item view
        holder.itemView.setOnClickListener(v -> {
            if (onOrderClickListener != null) {
                onOrderClickListener.onOrderClick(order.getOrderId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, orderDate, totalPrice, status, deliveryLocation;

        public OrderViewHolder(View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.order_id);
            orderDate = itemView.findViewById(R.id.order_date);
            totalPrice = itemView.findViewById(R.id.total_price);
            status = itemView.findViewById(R.id.status);
            deliveryLocation = itemView.findViewById(R.id.delivery_location);
        }
    }
}

