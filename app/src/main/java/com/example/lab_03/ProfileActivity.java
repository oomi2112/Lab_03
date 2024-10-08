package com.example.lab_03;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.provider.MediaStore;
import android.content.SharedPreferences;

public class ProfileActivity extends AppCompatActivity {
    private ImageView imgView;
    private EditText nameEditText;
    private EditText emailEditText;
    private ActivityResultLauncher<Intent> myPictureTakerLauncher;
    public static final String TAG = "PROFILE_ACTIVITY";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgView = findViewById(R.id.imageView);
        nameEditText = findViewById(R.id.et_name);
        emailEditText = findViewById(R.id.et_profile_email);

        // Get the email passed from MainActivity
        String email = getIntent().getStringExtra("EMAIL");
        emailEditText.setText(email);

        // Check for camera permissions
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
        } else {
            // Permission already granted, set up camera launcher
            setupCameraLauncher();
        }

        // Setup camera intent launcher
        myPictureTakerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Bitmap imgBitmap = (Bitmap) data.getExtras().get("data");
                            imgView.setImageBitmap(imgBitmap);
                        }
                    } else if (result.getResultCode() == RESULT_CANCELED) {
                        Log.i(TAG, "User refused to capture a picture.");
                    }
                }
        );

        // Set onClickListener to ImageButton to trigger camera
        ImageButton imageButton = findViewById(R.id.btn_capture_picture);
        imageButton.setOnClickListener(view -> dispatchTakePictureIntent());

        // Log lifecycle method
        Log.e(TAG, "In function: onCreate");

        // Load and save the email to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("EMAIL", email);
        editor.apply();
    }

    // Launch camera intent
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            myPictureTakerLauncher.launch(takePictureIntent);
        }
    }

    // Set up the camera launcher after permission is granted
    private void setupCameraLauncher() {
        myPictureTakerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Bitmap imgBitmap = (Bitmap) data.getExtras().get("data");
                            imgView.setImageBitmap(imgBitmap);
                        }
                    } else if (result.getResultCode() == RESULT_CANCELED) {
                        Log.i(TAG, "User refused to capture a picture.");
                    }
                }
        );
    }

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, set up camera launcher
                setupCameraLauncher();
                Toast.makeText(this, "Camera permission granted.", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(this, "Camera permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Log lifecycle events
    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "In function: onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "In function: onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "In function: onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "In function: onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "In function: onDestroy");
    }
}
