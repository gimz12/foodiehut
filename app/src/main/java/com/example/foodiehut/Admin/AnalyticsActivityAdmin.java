package com.example.foodiehut.Admin;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodiehut.DBHelper;
import com.example.foodiehut.R;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsActivityAdmin extends AppCompatActivity {

    private Spinner monthSpinner;
    private ListView demandListView;
    private TextView demandTextView;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics_admin);

        monthSpinner = findViewById(R.id.month_spinner);
        demandListView = findViewById(R.id.demand_list_view);
        demandTextView = findViewById(R.id.demand_text_view);

        dbHelper = new DBHelper(this);

        // Setup month spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.months_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter);

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String month = (String) parent.getItemAtPosition(position);
                loadAnalyticsData(month);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void loadAnalyticsData(String month) {
        Cursor demandCursor = dbHelper.getMonthlyFoodDemand(month);
        // Use a suitable adapter for displaying the demand data in the ListView
        // Example adapter for simplicity
        ArrayAdapter<String> demandAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, getDemandData(demandCursor));
        demandListView.setAdapter(demandAdapter);

    }

    private List<String> getDemandData(Cursor cursor) {
        List<String> data = new ArrayList<>();
        while (cursor.moveToNext()) {
            String category = cursor.getString(cursor.getColumnIndex("category"));
            int count = cursor.getInt(cursor.getColumnIndex("order_count"));
            data.add(category + ": " + count);
        }
        cursor.close();
        return data;
    }


}
