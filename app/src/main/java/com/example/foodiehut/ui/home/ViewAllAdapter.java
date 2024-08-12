package com.example.foodiehut.ui.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodiehut.R;

import java.util.List;

public class ViewAllAdapter extends RecyclerView.Adapter<ViewAllAdapter.ViewHolder> {

    Context context;
    List<ViewAllModel> viewAllModelList;

    public ViewAllAdapter(Context context, List<ViewAllModel> viewAllModelList) {
        this.context = context;
        this.viewAllModelList = viewAllModelList;
    }

    @NonNull
    @Override
    public ViewAllAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_all_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAllAdapter.ViewHolder holder, int position) {

        ViewAllModel viewAllModel = viewAllModelList.get(position);
        holder.name.setText(viewAllModelList.get(position).getName());
        holder.des.setText(viewAllModelList.get(position).getDescription());
        holder.price.setText(String.format("$%.2f", viewAllModelList.get(position).getPrice()));

        if (viewAllModel.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(viewAllModel.getImage(), 0, viewAllModel.getImage().length);
            holder.imageView.setImageBitmap(bitmap);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_launcher_background); // Fallback image if none
        }

    }

    @Override
    public int getItemCount() {
        return viewAllModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name,des,price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.view_img);
            name = itemView.findViewById(R.id.view_name);
            des = itemView.findViewById(R.id.view_des);
            price = itemView.findViewById(R.id.view_price);
        }
    }
}
