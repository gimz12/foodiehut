package com.example.foodiehut.ui.Category;

import android.content.Context;
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
import com.example.foodiehut.ui.home.HomeCategory;
import com.example.foodiehut.ui.home.HomeCategoryAdapter;

import java.util.List;

public class NavCategoryAdapter extends RecyclerView.Adapter<NavCategoryAdapter.ViewHolder> {

    Context context;
    List<NavCategory> navCategoryList;

    public NavCategoryAdapter(Context context, List<NavCategory> navCategoryList) {
        this.context = context;
        this.navCategoryList = navCategoryList;
    }

    @NonNull
    @Override
    public NavCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NavCategoryAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_vat_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NavCategoryAdapter.ViewHolder holder, int position) {
        NavCategory navCategory = navCategoryList.get(position);
        holder.name.setText(navCategory.getName());
        holder.des.setText(navCategory.getDes());

        Glide.with(context)
                .load(navCategory.getImg_url())
                .placeholder(R.drawable.ic_launcher_background) // Optional placeholder while loading
                .error(R.drawable.ic_launcher_background) // Optional error placeholder
                .into(holder.catimg);
    }

    @Override
    public int getItemCount() {
        return navCategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView catimg;
        TextView name;
        TextView des;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catimg = itemView.findViewById(R.id.cat_nav_img);
            name = itemView.findViewById(R.id.nav_cat_name);
            des= itemView.findViewById(R.id.nav_cat_des);

        }
    }
}
