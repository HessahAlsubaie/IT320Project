package com.example.it320project;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.Manifest;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Add extends AppCompatActivity {


    EditText spaceName, location, category, price, capacity, description;
    Button addBtn, selectPhotoButton;
    Bitmap photoBitmap;
    FloatingActionButton backBtn;

    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> selectImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        spaceName = findViewById(R.id.spaceName);
        location = findViewById(R.id.Location);
        category = findViewById(R.id.Category);
        price = findViewById(R.id.price);
        capacity = findViewById(R.id.capacity);
        description = findViewById(R.id.description);
        backBtn= findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add.this, home.class);
                startActivity(intent);
            }
        });


        addBtn = findViewById(R.id.submitInfoButton);
        selectPhotoButton = findViewById(R.id.selectPhotoButton);


        // Request permission to read external storage
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // Launch the image selection activity
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        selectImageLauncher.launch(intent);
                    } else {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                });

        // Launch the image selection activity and retrieve the selected image
        selectImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        try {
                            // Check if the selected image is a PNG format
                            InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeStream(inputStream, null, options);
                            inputStream.close();
                            if (!"image/png".equals(options.outMimeType)) {
                                Toast.makeText(this, "Please select a PNG image", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Check if the selected image is small enough
                            int maxSize = 2000; // maximum size in pixels
                            inputStream = getContentResolver().openInputStream(selectedImage);
                            options.inJustDecodeBounds = false;
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                            inputStream.close();
                            if (bitmap.getWidth() > maxSize || bitmap.getHeight() > maxSize) {
                                Toast.makeText(this, "Please select a smaller image", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            photoBitmap = bitmap;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (photoBitmap == null) {
                    Toast.makeText(Add.this, "Please select a photo", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean nameValid = validateName();
                boolean locValid = validateLoc();
                boolean catValid = validateCat();
                boolean priceValid = validatePrice();
                boolean descValid = validatedescp();
                boolean capValid= validateCapacity();

                if (!nameValid) {
                    spaceName.requestFocus();
                    return;
                }

                if (!capValid) {
                    capacity.requestFocus();
                    return;
                }

                if (!locValid) {
                    location.requestFocus();
                    return;
                }

                if (!catValid) {
                    category.requestFocus();
                    return;
                }

                if (!priceValid) {
                    price.requestFocus();
                    return;
                }

                if (!descValid) {
                    description.requestFocus();
                    return;
                }

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] photoData = stream.toByteArray();

                Space spaceModel;
                try {
                    spaceModel = new Space(-1, spaceName.getText().toString(), location.getText().toString(),
                            category.getText().toString(), Integer.parseInt(price.getText().toString()), Integer.parseInt(capacity.getText().toString()),
                            description.getText().toString(), photoData);
                } catch (Exception e) {
                    Toast.makeText(Add.this, "invalid information", Toast.LENGTH_SHORT).show();
                    spaceModel = new Space(-1, "error", "error", "error", 0, 0, "error", null);
                }
                MyDatabaseHelper myDb = new MyDatabaseHelper(Add.this);
                boolean success = myDb.addOne(spaceModel);
                if (success) {
                    Toast.makeText(Add.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Add.this, "Not Added Successfully", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(Add.this, home.class);
                startActivity(intent);
            }
        });

        selectPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the app has permission to read external storage
                if (ContextCompat.checkSelfPermission(Add.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // Launch the image selection activity
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    selectImageLauncher.launch(intent);
                } else {
                    // Request permission to read external storage
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }
        });
    }

    private boolean validateName() {
        String val = spaceName.getText().toString().trim();
        if (val.isEmpty()) {
            spaceName.setError("Field cannot be empty");
            return false;
        } else {
            spaceName.setError(null);
            return true;
        }
    }

    private boolean validateLoc() {
        String val = location.getText().toString().trim().toLowerCase();
        if (val.isEmpty()) {
            location.setError("Field cannot be empty");
            return false;
        } else if (!val.startsWith("north of riyadh") && !val.startsWith("west of riyadh")
                && !val.startsWith("east of riyadh") && !val.startsWith("south of riyadh")) {
            location.setError("Location must start with an area form Riyadh");
            return false;
        } else {
            location.setError(null);
            return true;
        }
    }

    private boolean validateCat() {
        String val = category.getText().toString().trim();
        if (val.isEmpty()) {
            category.setError("Field can not be empty. Please choose a category.");
            return false;
        } else {
            category.setError(null);
            return true;
        }
    }

    private boolean validatePrice() {
        String val = price.getText().toString().trim();
        if (val.isEmpty()) {
            price.setError("Field cannot be empty, please set the price");
            return false;
        } else {
            price.setError(null);
            return true;
        }
    }

    private boolean validatedescp() {
        String val = description.getText().toString().trim();
        if (val.isEmpty()) {
            description.setError("Please write a short description");
            return false;
        } else {
            description.setError(null);
            return true;
        }
    }
    private boolean validateCapacity() {
        String val = capacity.getText().toString().trim();
        if (val.isEmpty()) {
            capacity.setError("Please enter the potential capacity.");
            return false;
        } else {
            capacity.setError(null);
            return true;
        }
    }
}