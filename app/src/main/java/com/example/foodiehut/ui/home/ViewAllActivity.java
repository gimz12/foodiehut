package com.example.foodiehut.ui.home;

import android.os.Bundle;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodiehut.DBHelper;
import com.example.foodiehut.R;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ViewAllAdapter adapter;
    private List<ViewAllModel> viewAllModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        recyclerView = findViewById(R.id.view_all_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewAllModelList = new ArrayList<>();
        adapter = new ViewAllAdapter(this, viewAllModelList);
        recyclerView.setAdapter(adapter);

        String category = getIntent().getStringExtra("category");
        loadMenuItems(category);
        Toast.makeText(this, category, Toast.LENGTH_SHORT).show();

    }

    private void loadMenuItems(String category) {
        SQLiteDatabase db = new DBHelper(this).getReadableDatabase();
        Cursor cursor = db.query(
                "MenuItems",
                null,
                "category = ?",
                new String[]{category},
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                boolean availability = cursor.getInt(cursor.getColumnIndexOrThrow("availability")) > 0;

                viewAllModelList.add(new ViewAllModel(name, description, category, price, image, availability));
            }
            cursor.close();
        }

        adapter.notifyDataSetChanged();
    }
}
