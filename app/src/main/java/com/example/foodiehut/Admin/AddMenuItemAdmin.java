package com.example.foodiehut.Admin;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AddMenuItemAdmin extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_PHOTO_REQUEST = 2;

    private EditText itemNameEditText;
    private EditText itemDescriptionEditText;
    private EditText itemPriceEditText;
    private ImageView itemImageView;
    private Button chooseImageButton;
    private Button takePhotoButton;
    private Button addItemButton;
    private TextView statusMessageTextView;
    private Spinner categorySpinner;

    private DBHelper dbHelper;
    private Bitmap selectedImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item_admin);

        itemNameEditText = findViewById(R.id.item_name);
        itemDescriptionEditText = findViewById(R.id.item_description);
        itemPriceEditText = findViewById(R.id.item_price);
        itemImageView = findViewById(R.id.item_image);
        chooseImageButton = findViewById(R.id.choose_image_button);
        takePhotoButton = findViewById(R.id.take_photo_button);
        addItemButton = findViewById(R.id.add_item_button);
        statusMessageTextView = findViewById(R.id.status_message);
        categorySpinner = findViewById(R.id.category_spinner);

        dbHelper = new DBHelper(this);

        // Populate the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMenuItem();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PHOTO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    selectedImageBitmap = BitmapFactory.decodeStream(inputStream);
                    itemImageView.setImageBitmap(selectedImageBitmap);
                } catch (Exception e) {
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == TAKE_PHOTO_REQUEST && data != null && data.getExtras() != null) {
                selectedImageBitmap = (Bitmap) data.getExtras().get("data");
                itemImageView.setImageBitmap(selectedImageBitmap);
            }
        }
    }

    private void addMenuItem() {
        String name = itemNameEditText.getText().toString().trim();
        String description = itemDescriptionEditText.getText().toString().trim();
        String priceStr = itemPriceEditText.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();

        if (name.isEmpty() || priceStr.isEmpty()) {
            statusMessageTextView.setText("Name and Price are required.");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            statusMessageTextView.setText("Invalid price format.");
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("price", price);
        values.put("availability", true);
        values.put("image", getImageAsByteArray(selectedImageBitmap));
        values.put("category", category);
        values.put("created_at", System.currentTimeMillis());

        long result = db.insert("MenuItems", null, values);

        if (result == -1) {
            statusMessageTextView.setText("Failed to add item. Try again.");
        } else {
            Toast.makeText(this, "Item added successfully!", Toast.LENGTH_SHORT).show();
            itemNameEditText.setText("");
            itemDescriptionEditText.setText("");
            itemPriceEditText.setText("");
            itemImageView.setImageResource(android.R.drawable.ic_menu_gallery); // Reset image
            statusMessageTextView.setText("");
        }
    }

    private byte[] getImageAsByteArray(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            return outputStream.toByteArray();
        } else {
            return null;
        }
    }

}
