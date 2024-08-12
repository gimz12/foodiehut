package com.example.foodiehut;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodiehut.ui.Category.NavCategory;
import com.example.foodiehut.ui.Category.NavCategoryAdapter;
import com.example.foodiehut.ui.home.HomeCategoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {
    private RecyclerView NavCatRecyclerView;

    private NavCategoryAdapter navCategoryAdapter;

    private List<NavCategory> navCategoryList;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        NavCatRecyclerView = view.findViewById(R.id.cat_rec);

        NavCatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        loadNavCatItems();


        return view;
    }

    private void loadNavCatItems() {
        navCategoryList = new ArrayList<>();

        navCategoryList.add(new NavCategory("Pasta","https://iili.io/dlkWDxe.md.jpg", "Italian Pasta","type2"));
        navCategoryList.add(new NavCategory("Burgers","https://iili.io/d0I70Qt.md.jpg", "American Burgers", "type4"));
        navCategoryList.add(new NavCategory("Salads","https://iili.io/d0IAIBs.md.jpg", "Discover a refreshing variety of salads, " +
                "from light and crisp greens to hearty and nutritious bowls.","type2"));
        navCategoryList.add(new NavCategory("Soups","https://iili.io/d0IAxQn.md.jpg", "American Burgers", "type4"));

        navCategoryAdapter = new NavCategoryAdapter(getContext(),navCategoryList);
        NavCatRecyclerView.setAdapter(navCategoryAdapter);
    }


}