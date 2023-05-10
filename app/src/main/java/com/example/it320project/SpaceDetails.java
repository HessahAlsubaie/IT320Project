package com.example.it320project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Locale;

public class SpaceDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_details);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        String name = intent.getStringExtra("name");
        String location = intent.getStringExtra("location");
        String category = intent.getStringExtra("category");
        double price = intent.getDoubleExtra("price", 0.0);
        int capacity = intent.getIntExtra("capacity", 0);
        String description = intent.getStringExtra("description");


        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        byte[] photo = dbHelper.getPhotoData(id);

        // Display the space details in the activity layout
        TextView nameTextView = findViewById(R.id.nameTextView);
        nameTextView.setText(name);

        TextView locationTextView = findViewById(R.id.locationTextView);
        locationTextView.setText(location);

        TextView categoryTextView = findViewById(R.id.categoryTextView);
        categoryTextView.setText(category);

        TextView priceTextView = findViewById(R.id.priceTextView);
        priceTextView.setText(String.format(Locale.getDefault(), "%.2f", price));

        TextView capacityTextView = findViewById(R.id.capacityTextView);
        capacityTextView.setText(String.valueOf(capacity));

        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(description);

        ImageView photoImageView = findViewById(R.id.photoImageView);
        if (photo != null && photo.length > 0) {
            Bitmap photoBitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
            if (photoBitmap != null) {
                photoImageView.setImageBitmap(photoBitmap);

            }
        }
    }
}
