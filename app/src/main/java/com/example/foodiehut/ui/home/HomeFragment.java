package com.example.foodiehut.ui.home;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodiehut.DBHelper;
import com.example.foodiehut.R;
import com.example.foodiehut.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    ScrollView scrollView;
    ProgressBar progressBar;

    private RecyclerView recyclerView;
    private RecyclerView categoriesRecyclerView,RecommendedRecyclerView;

    private HomeCategoryAdapter categoryAdapter;
    private MenuItemAdapter adapter;

    private List<MenuItem> menuItems;
    private List<HomeCategory> categoryList;

    private List<RecommendedItem> recommendedItemList;
    RecommenededItemAdapter recommenededItemAdapter;

    private DBHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.pop_rec);
        categoriesRecyclerView = view.findViewById(R.id.categories_rec);
        RecommendedRecyclerView = view.findViewById(R.id.recommended_rec);
        scrollView = view.findViewById(R.id.scroll_view);
        progressBar = view.findViewById(R.id.progressbar);

        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        RecommendedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        dbHelper = new DBHelper(getContext());
        loadMenuItems();
        loadCategories();
        loadRecommendedItems();
        return view;
    }

    private void loadRecommendedItems() {
        recommendedItemList= new ArrayList<>();
        // Example static data, replace with database query if needed
        recommendedItemList.add(new RecommendedItem("Pasta", "Italian Pasta","https://iili.io/dlkWDxe.md.jpg"));
        recommendedItemList.add(new RecommendedItem("Burgers", "American Burgers", "https://iili.io/dlkWZb9.md.jpg"));
        // Add more categories as needed

        progressBar.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);

        recommenededItemAdapter = new RecommenededItemAdapter(getContext(), recommendedItemList);
        RecommendedRecyclerView.setAdapter(recommenededItemAdapter);
    }

    private void loadMenuItems() {
        menuItems = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                "MenuItems",
                new String[]{"item_id", "name", "description", "price", "image", "availability"},
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            try {
                // Ensure column names are correct
                int itemIdIndex = cursor.getColumnIndex("item_id");
                int nameIndex = cursor.getColumnIndex("name");
                int descriptionIndex = cursor.getColumnIndex("description");
                int priceIndex = cursor.getColumnIndex("price");
                int imageIndex = cursor.getColumnIndex("image");
                int availabilityIndex = cursor.getColumnIndex("availability");

                while (cursor.moveToNext()) {
                    int itemId = cursor.getInt(itemIdIndex);
                    String name = cursor.getString(nameIndex);
                    String description = cursor.getString(descriptionIndex);
                    double price = cursor.getDouble(priceIndex);
                    byte[] image = cursor.getBlob(imageIndex);
                    boolean availability = cursor.getInt(availabilityIndex) > 0;

                    MenuItem menuItem = new MenuItem(itemId, name, description, price, image, availability);
                    menuItems.add(menuItem);
                }
            } finally {
                cursor.close();
            }
        }

        adapter = new MenuItemAdapter(menuItems);
        recyclerView.setAdapter(adapter);
    }

    private void loadCategories() {
        categoryList = new ArrayList<>();
        // Example static data, replace with database query if needed
        categoryList.add(new HomeCategory("Pizza", "https://iili.io/dleeOPe.md.jpg", "type1"));
        categoryList.add(new HomeCategory("Burgers", "https://iili.io/dleeNV9.md.jpg", "type2"));
        // Add more categories as needed

        categoryAdapter = new HomeCategoryAdapter(getContext(), categoryList);
        categoriesRecyclerView.setAdapter(categoryAdapter);
    }


}