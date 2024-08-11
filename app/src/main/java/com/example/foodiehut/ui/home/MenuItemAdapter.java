package com.example.foodiehut.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodiehut.R;

import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {

    private List<MenuItem> menuItems;

    public MenuItemAdapter(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItem menuItem = menuItems.get(position);
        holder.nameTextView.setText(menuItem.getName());
        holder.descriptionTextView.setText(menuItem.getDescription());
        holder.priceTextView.setText(String.format("$%.2f", menuItem.getPrice()));

        if (menuItem.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(menuItem.getImage(), 0, menuItem.getImage().length);
            holder.imageView.setImageBitmap(bitmap);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_launcher_background); // Fallback image if none
        }
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView nameTextView;
        TextView descriptionTextView;
        TextView priceTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.pop_img);
            nameTextView = itemView.findViewById(R.id.pop_name);
            descriptionTextView = itemView.findViewById(R.id.pop_des);
            priceTextView = itemView.findViewById(R.id.pop_price);
        }
    }
}

