package com.example.foodiehut.Admin;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodiehut.DBHelper;
import com.example.foodiehut.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ManageMenuItemAdmin extends AppCompatActivity {

    private EditText itemIdEditText;
    private EditText itemNameEditText;
    private EditText itemDescriptionEditText;
    private EditText itemPriceEditText;
    private Spinner categorySpinner;
    private ImageView itemImageView;
    private Button loadItemButton;
    private Button updateItemButton;
    private Button deleteItemButton;
    private TextView statusMessageTextView;

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_menu_item_admin);

        itemIdEditText = findViewById(R.id.item_id);
        itemNameEditText = findViewById(R.id.item_name);
        itemDescriptionEditText = findViewById(R.id.item_description);
        itemPriceEditText = findViewById(R.id.item_price);
        categorySpinner = findViewById(R.id.category_spinner);
        itemImageView = findViewById(R.id.item_image);
        loadItemButton = findViewById(R.id.load_item_button);
        updateItemButton = findViewById(R.id.update_item_button);
        deleteItemButton = findViewById(R.id.delete_item_button);
        statusMessageTextView = findViewById(R.id.status_message);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        loadItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMenuItem();
            }
        });

        updateItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMenuItem();
            }
        });

        deleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMenuItem();
            }
        });
    }

    private void loadMenuItem() {
        String itemIdStr = itemIdEditText.getText().toString().trim();
        if (itemIdStr.isEmpty()) {
            statusMessageTextView.setText("Item ID is required.");
            return;
        }

        long itemId = Long.parseLong(itemIdStr);
        Cursor cursor = db.query("MenuItems", null, "item_id = ?", new String[]{String.valueOf(itemId)}, null, null, null);

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String category = cursor.getString(cursor.getColumnIndex("category"));
            byte[] imageBlob = cursor.getBlob(cursor.getColumnIndex("image"));
            boolean availability = cursor.getInt(cursor.getColumnIndex("availability")) > 0;

            itemNameEditText.setText(name);
            itemDescriptionEditText.setText(description);
            itemPriceEditText.setText(String.valueOf(price));
            categorySpinner.setSelection(((ArrayAdapter) categorySpinner.getAdapter()).getPosition(category));

            if (imageBlob != null) {
                InputStream inputStream = new ByteArrayInputStream(imageBlob);
                itemImageView.setImageBitmap(BitmapFactory.decodeStream(inputStream));
            }

            statusMessageTextView.setText(availability ? "Available" : "Not Available");
        } else {
            statusMessageTextView.setText("Item not found.");
        }
        cursor.close();
    }

    private void updateMenuItem() {
        String itemIdStr = itemIdEditText.getText().toString().trim();
        String name = itemNameEditText.getText().toString().trim();
        String description = itemDescriptionEditText.getText().toString().trim();
        String priceStr = itemPriceEditText.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();
        boolean availability = statusMessageTextView.getText().toString().equals("Available");

        if (itemIdStr.isEmpty() || name.isEmpty() || priceStr.isEmpty()) {
            statusMessageTextView.setText("Item ID, Name, and Price are required.");
            return;
        }

        long itemId = Long.parseLong(itemIdStr);
        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            statusMessageTextView.setText("Invalid price format.");
            return;
        }

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("price", price);
        values.put("availability", availability);
        values.put("category", category);

        int rowsUpdated = db.update("MenuItems", values, "item_id = ?", new String[]{String.valueOf(itemId)});

        if (rowsUpdated > 0) {
            Toast.makeText(this, "Item updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            statusMessageTextView.setText("Failed to update item.");
        }
    }

    private void deleteMenuItem() {
        String itemIdStr = itemIdEditText.getText().toString().trim();
        if (itemIdStr.isEmpty()) {
            statusMessageTextView.setText("Item ID is required.");
            return;
        }

        long itemId = Long.parseLong(itemIdStr);
        int rowsDeleted = db.delete("MenuItems", "item_id = ?", new String[]{String.valueOf(itemId)});

        if (rowsDeleted > 0) {
            Toast.makeText(this, "Item deleted successfully!", Toast.LENGTH_SHORT).show();
            itemNameEditText.setText("");
            itemDescriptionEditText.setText("");
            itemPriceEditText.setText("");
            categorySpinner.setSelection(0);
            itemImageView.setImageResource(android.R.drawable.ic_menu_gallery);
            statusMessageTextView.setText("");
        } else {
            statusMessageTextView.setText("Failed to delete item.");
        }
    }
}
