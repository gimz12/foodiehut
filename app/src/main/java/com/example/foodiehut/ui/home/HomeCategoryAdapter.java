package com.example.foodiehut.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodiehut.R;

import java.util.List;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {

    Context context;
    List<HomeCategory> categoryList;

    public HomeCategoryAdapter(Context context, List<HomeCategory> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public HomeCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_cat_cards,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull HomeCategoryAdapter.ViewHolder holder, int position) {

        HomeCategory homeCategory = categoryList.get(position);
        holder.name.setText(homeCategory.getName());

        Glide.with(context)
                .load(homeCategory.getImg_url())
                .placeholder(R.drawable.ic_launcher_background) // Optional placeholder while loading
                .error(R.drawable.ic_launcher_background) // Optional error placeholder
                .into(holder.catimg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewAllActivity.class);
                intent.putExtra("category",categoryList.get(holder.getAdapterPosition()).getName());
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView catimg;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catimg = itemView.findViewById(R.id.home_cat_img);
            name = itemView.findViewById(R.id.cat_home_name);

        }
    }
}
