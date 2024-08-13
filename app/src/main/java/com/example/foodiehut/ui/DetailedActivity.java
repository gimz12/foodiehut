package com.example.foodiehut.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.foodiehut.R;
import com.example.foodiehut.ui.home.ViewAllModel;

public class DetailedActivity extends AppCompatActivity {

    ImageView imageView;
    TextView price,des;
    Button addToCart_btn;
    ImageView addItem,removeItem;
    Toolbar toolbar;

    ViewAllModel viewAllModel = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Object object = getIntent().getSerializableExtra("detail");
        if(object instanceof ViewAllModel){
            viewAllModel = (ViewAllModel) object;
        }


        imageView = findViewById(R.id.detailed_img);
        addItem = findViewById(R.id.add_item);
        removeItem = findViewById(R.id.remove_item);

        price = findViewById(R.id.detailed_price);
        des = findViewById(R.id.detailed_des);
        addToCart_btn = findViewById(R.id.add_to_cart);

        if(viewAllModel != null){

            if (viewAllModel.getImage() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(viewAllModel.getImage(), 0, viewAllModel.getImage().length);
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.drawable.ic_launcher_background); // Fallback image if none
            }

            price.setText(String.format("$%.2f", viewAllModel.getPrice()));
            des.setText(viewAllModel.getDescription());
        }

    }

}