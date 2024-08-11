package com.example.foodiehut.ui.home;

import android.content.Context;
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

public class RecommenededItemAdapter extends RecyclerView.Adapter<RecommenededItemAdapter.ViewHolder> {

    Context context;
    List<RecommendedItem> list;

    public RecommenededItemAdapter(Context context, List<RecommendedItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecommenededItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recommened_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecommenededItemAdapter.ViewHolder holder, int position) {
        RecommendedItem recommendedItem = list.get(position);
        holder.name.setText(recommendedItem.getName());
        holder.description.setText(recommendedItem.getDescription());

        Glide.with(context)
                .load(recommendedItem.getImg_url())
                .placeholder(R.drawable.ic_launcher_background) // Optional placeholder while loading
                .error(R.drawable.ic_launcher_background) // Optional error placeholder
                .into(holder.img_url);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_url;
        TextView name,description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_url=itemView.findViewById(R.id.rec_img);
            name=itemView.findViewById(R.id.rec_name);
            description=itemView.findViewById(R.id.rec_dec);
        }
    }
}
