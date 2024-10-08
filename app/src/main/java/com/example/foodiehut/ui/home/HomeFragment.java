package com.example.foodiehut.ui.home;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodiehut.DBHelper;
import com.example.foodiehut.R;

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
        recommendedItemList.add(new RecommendedItem("Pasta", "Delicious Italian pasta with rich tomato sauce and cheese. Perfect for a hearty meal.", "https://iili.io/dlkWDxe.md.jpg"));
        recommendedItemList.add(new RecommendedItem("Burgers", "Juicy American burgers made with 100% beef patties and fresh ingredients.", "https://iili.io/dleeOPe.md.jpg"));
        recommendedItemList.add(new RecommendedItem("Salad", "Fresh mixed greens with a variety of vegetables and a light vinaigrette.", "https://iili.io/d11SmLG.jpg"));
        recommendedItemList.add(new RecommendedItem("Soup", "Warm and comforting soup made with seasonal vegetables and spices.", "https://iili.io/d11UD8B.jpg"));
        recommendedItemList.add(new RecommendedItem("Rice", "Fluffy steamed rice, a perfect side dish for any meal.", "https://iili.io/d11UywF.jpg"));
        recommendedItemList.add(new RecommendedItem("Submarine", "Hearty submarine sandwich with meats, cheeses, and fresh veggies.", "https://iili.io/d11UZ3Q.jpg"));
        recommendedItemList.add(new RecommendedItem("Pizza", "Cheesy pizza with a crispy crust and a variety of toppings.", "https://iili.io/dleeNV9.md.jpg")); // Example for completeness
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
        categoryList.add(new HomeCategory("Pizza", "https://iili.io/dleeNV9.md.jpg", "type1"));
        categoryList.add(new HomeCategory("Burger", "https://iili.io/dleeOPe.md.jpg", "type2"));
        categoryList.add(new HomeCategory("Pasta", "https://iili.io/dlkWDxe.md.jpg", "type3"));
        categoryList.add(new HomeCategory("Salad", "https://iili.io/d11SmLG.jpg", "type4"));
        categoryList.add(new HomeCategory("Soup", "https://iili.io/d11UD8B.jpg", "type5"));
        categoryList.add(new HomeCategory("Rice", "https://iili.io/d11UywF.jpg", "type6"));
        categoryList.add(new HomeCategory("Submarine", "https://iili.io/d11UZ3Q.jpg", "type7"));
        // Add more categories as needed

        categoryAdapter = new HomeCategoryAdapter(getContext(), categoryList);
        categoriesRecyclerView.setAdapter(categoryAdapter);
    }


}