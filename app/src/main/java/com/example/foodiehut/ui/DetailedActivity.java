package com.example.foodiehut.ui;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.foodiehut.DBHelper;
import com.example.foodiehut.R;
import com.example.foodiehut.ui.home.ViewAllModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {
    TextView quantity;
    int totalQuantity = 1;
    double totalPrice = 0 ;

    ImageView imageView;
    TextView price, des, Name;
    Button addToCart_btn;
    ImageView addItem, removeItem;
    Toolbar toolbar;
    int userId;

    ViewAllModel viewAllModel = null;
    SQLiteDatabase db;
    private DBHelper dbHelper;


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

        // Getting user ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("user_id", -1); // Default is -1 if not found

        if (userId != -1) {
            Toast.makeText(this, "Welcome " + userId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "User ID error", Toast.LENGTH_SHORT).show();
        }

        quantity = findViewById(R.id.quantity);
        imageView = findViewById(R.id.detailed_img);
        addItem = findViewById(R.id.add_item);
        removeItem = findViewById(R.id.remove_item);

        Name = findViewById(R.id.name_txt);
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
            Name.setText(viewAllModel.getName());

            totalPrice = viewAllModel.getPrice() * totalQuantity;
        }

        addToCart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addedtocart();
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalQuantity < 10){
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));
                }
            }
        });

        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalQuantity > 1){
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));
                }
            }
        });

        // Initialize database helper
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
    }

    private void addedtocart() {
        String saveCurrentDate, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("productName", viewAllModel.getName());
        values.put("productPrice", viewAllModel.getPrice()); // Use REAL type for price
        values.put("productDate", saveCurrentDate);
        values.put("productTime", saveCurrentTime);
        values.put("totalQuantity", totalQuantity);
        values.put("totalPrice", totalPrice);

        long newRowId = db.insert("Cart", null, values);
        if (newRowId != -1) {
            Toast.makeText(this, "Item added to cart", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error adding item to cart", Toast.LENGTH_SHORT).show();
        }
    }
}