package com.example.foodiehut;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private com.example.foodiehut.DBHelper DBHelper;
    EditText usernameEditText, passwordEditText, confirmPasswordEditText, emailEditText;
    Button signup;
    TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        emailEditText = findViewById(R.id.email);
        loginLink = findViewById(R.id.login_link);
        signup = findViewById(R.id.signup_button);

        DBHelper = new DBHelper(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginUser.class);
                startActivity(intent);
            }
        });
    }

    public void signup() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ensure that email is in the correct format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);  // Store plain text password (consider hashing for security)
        values.put("email", email);
        values.put("profile_image", (byte[]) null); // Assuming no profile image is uploaded at signup
        values.put("address", (String) null);  // Setting address to null
        values.put("phone_number", (String) null);  // Setting phone number to null
        values.put("created_at", System.currentTimeMillis());

        long result = db.insert("Users", null, values);

        if (result == -1) {
            Toast.makeText(this, "Signup failed. Please try again.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginUser.class);
            startActivity(intent);
        }
    }



}
