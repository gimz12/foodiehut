package com.example.foodiehut.ui.Profile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.foodiehut.DBHelper;
import com.example.foodiehut.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CAMERA_PERMISSION_CODE = 100;

    private EditText nameEditText, emailEditText, phoneEditText, addressEditText;
    private ImageView profileImageView;
    private Button updateButton, uploadPhotoButton, takePhotoButton;
    private DBHelper dbHelper;
    private int userId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
        userId = sharedPreferences.getInt("user_id", -1);

        // Initialize views
        nameEditText = view.findViewById(R.id.profilename);
        emailEditText = view.findViewById(R.id.profile_email);
        phoneEditText = view.findViewById(R.id.profilenumber);
        addressEditText = view.findViewById(R.id.profileaddress);
        profileImageView = view.findViewById(R.id.profileimage);
        updateButton = view.findViewById(R.id.update_btn);
        uploadPhotoButton = view.findViewById(R.id.upload_photo_btn);
        takePhotoButton = view.findViewById(R.id.take_photo_btn);

        dbHelper = new DBHelper(getActivity());

        // Set the click listener for the update button
        updateButton.setOnClickListener(v -> updateUserProfile());

        // Set the click listener for the upload photo button
        uploadPhotoButton.setOnClickListener(v -> openImageChooser());

        // Set the click listener for the take photo button
        takePhotoButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                requestCameraPermission();
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(getActivity(), "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    profileImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CAMERA_REQUEST_CODE && data != null) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = (Bitmap) extras.get("data");
                    profileImageView.setImageBitmap(photo);
                }
            }
        }
    }

    private void updateUserProfile() {
        // Retrieve user input
        String username = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phoneNumber = phoneEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();

        // Validate input
        if (username.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
            Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(getActivity(), "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPhoneNumber(phoneNumber)) {
            Toast.makeText(getActivity(), "Invalid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert the image to a byte array
        Bitmap bitmap = ((BitmapDrawable) profileImageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] profileImage = stream.toByteArray();

        // Update user details
        boolean isUpdated = dbHelper.updateUserDetails(userId, username, email, profileImage, address, phoneNumber);

        if (isUpdated) {
            Toast.makeText(getActivity(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Profile Update Failed", Toast.LENGTH_SHORT).show();
        }
    }

    // Check for valid email format
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Check for valid phone number (simple example)
    private boolean isValidPhoneNumber(String phoneNumber) {
        // This regex allows only digits and should be at least 10 digits long
        return phoneNumber.matches("\\d{10,}");
    }

}
