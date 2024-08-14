package com.example.foodiehut;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {

    private Context context;
    private List<OrderItem> orderItemList;

    public OrderDetailsAdapter(Context context, List<OrderItem> orderItemList) {
        this.context = context;
        this.orderItemList = orderItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItem orderItem = orderItemList.get(position);
        holder.itemNameTextView.setText("Item: " + orderItem.getItemId());
        holder.quantityTextView.setText("Quantity: " + orderItem.getQuantity());
        holder.customizationTextView.setText("Customization: " + orderItem.getCustomization());
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView, quantityTextView, customizationTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.item_name_text_view);
            quantityTextView = itemView.findViewById(R.id.quantity_text_view);
            customizationTextView = itemView.findViewById(R.id.customization_text_view);
        }
    }
}
