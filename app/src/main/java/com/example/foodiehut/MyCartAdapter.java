package com.example.foodiehut;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {

    Context context;
    List<MyCart> myCartList;

    public MyCartAdapter(Context context, List<MyCart> myCartList) {
        this.context = context;
        this.myCartList = myCartList;
    }

    @NonNull
    @Override
    public MyCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartAdapter.ViewHolder holder, int position) {

        holder.name.setText(myCartList.get(position).getProductname());
        holder.price.setText(String.format("$%.2f", myCartList.get(position).getProductprice()));
        holder.date.setText(myCartList.get(position).getProductDate());
        holder.time.setText(myCartList.get(position).getProductTime());
        holder.quantity.setText(String.valueOf(myCartList.get(position).getTotalQuantity()));
        holder.totalprice.setText(String.format("$%.2f", myCartList.get(position).getTotoalPrice()));


    }

    @Override
    public int getItemCount() {
        return myCartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,date,time,price,totalprice,quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            date = itemView.findViewById(R.id.current_date);
            time = itemView.findViewById(R.id.current_time);
            quantity = itemView.findViewById(R.id.total_quantity);
            totalprice = itemView.findViewById(R.id.total_price);

        }
    }
}
