package com.example.foodiehut;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {

    Context context;
    List<MyCart> myCartList;
    DBHelper dbHelper;
    int userId;

    public MyCartAdapter(Context context, List<MyCart> myCartList, int userId) {
        this.context = context;
        this.myCartList = myCartList;
        this.userId = userId;
        dbHelper = new DBHelper(context); // Initialize the DBHelper
    }

    @NonNull
    @Override
    public MyCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartAdapter.ViewHolder holder, int position) {
        MyCart currentItem = myCartList.get(position);

        holder.name.setText(currentItem.getProductname());
        holder.price.setText(String.format("$%.2f", currentItem.getProductprice()));
        holder.date.setText(currentItem.getProductDate());
        holder.time.setText(currentItem.getProductTime());
        holder.quantity.setText(String.valueOf(currentItem.getTotalQuantity()));
        holder.totalprice.setText(String.format("$%.2f", currentItem.getTotoalPrice()));

        // Set click listener on delete ImageView
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete the item from the database
                dbHelper.deleteCartItem(currentItem.getProductname(), userId);

                // Remove the item from the list and notify the adapter
                myCartList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, myCartList.size());

                Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return myCartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, date, time, price, totalprice, quantity;
        Button delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            date = itemView.findViewById(R.id.current_date);
            time = itemView.findViewById(R.id.current_time);
            quantity = itemView.findViewById(R.id.total_quantity);
            totalprice = itemView.findViewById(R.id.total_price);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
