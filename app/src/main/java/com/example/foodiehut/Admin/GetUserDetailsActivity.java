package com.example.foodiehut.Admin;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodiehut.Admin.UserAdapter;
import com.example.foodiehut.Admin.User;
import com.example.foodiehut.DBHelper;
import com.example.foodiehut.R;

import java.util.ArrayList;
import java.util.List;

public class GetUserDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUsers;
    private UserAdapter userAdapter;
    private List<User> userList;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_details);

        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBHelper(this);
        userList = new ArrayList<>();

        loadUserData();

        userAdapter = new UserAdapter(this, userList);
        recyclerViewUsers.setAdapter(userAdapter);
    }

    private void loadUserData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT user_id, username, email, address, phone_number, profile_image FROM Users", null);

        if (cursor.moveToFirst()) {
            do {
                int userId = cursor.getInt(0);
                String username = cursor.getString(1);
                String email = cursor.getString(2);
                String address = cursor.getString(3);
                String phoneNumber = cursor.getString(4);
                byte[] profileImage = cursor.getBlob(5);  // Retrieve the image data as byte array

                User user = new User(userId, username, email, address, phoneNumber, profileImage);
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
