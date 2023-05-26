package com.example.it320project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SpaceDetails extends AppCompatActivity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_details);

       Intent intent = getIntent();
       /*
        int id = intent.getIntExtra("id", 0);
        String name = intent.getStringExtra("name");
        String location = intent.getStringExtra("location");
        String category = intent.getStringExtra("category");
        double price = intent.getDoubleExtra("price", 0.0);
        int capacity = intent.getIntExtra("capacity", 0);
        String description = intent.getStringExtra("description");*/
        MyDatabaseHelper dbHelper=new  MyDatabaseHelper(this);


        int spaceId = getIntent().getIntExtra("id", 0);
        Space space = dbHelper.getSpaceById(spaceId);
        String name= space.getName();
        String location = space.getLocation();
        String category =space.getCategory();
        double price = space.getPrice();
        int capacity = space.getCapacity();
        String description = space.getDescription();

        byte[] photo = dbHelper.getPhotoData(spaceId);

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
        btn=findViewById(R.id.rentBtn);
        int id2 = intent.getIntExtra("id", 0);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyDatabaseHelper dbHelper = new MyDatabaseHelper(SpaceDetails.this);

                // Get space ID of the selected space
                int spaceId = id2;

                // Insert into rented spaces table
                boolean inserted = dbHelper.insertIntoRentedSpaces(spaceId);

                // Update space status in SpaceInfo table
                boolean updated = dbHelper.updateSpaceStatus(spaceId, 1);

                if (inserted && updated) {
                    Toast.makeText(SpaceDetails.this, "Successfully rented space!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SpaceDetails.this, RentedSpacesActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SpaceDetails.this, "Renting space failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
}}
