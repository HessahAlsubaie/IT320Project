package com.example.it320project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
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
        String spaceName = intent.getStringExtra("name");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyDatabaseHelper dbHelper = new MyDatabaseHelper(SpaceDetails.this);
                int userId = dbHelper.getSpacesUserId(spaceName);
                int currentUserId = dbHelper.getCurrentUserId();
                if (userId != currentUserId) {
                    // Show rent dialog to allow the user to choose the time of the rent
                    showRentDialog(id2);
                } else {
                    Toast.makeText(SpaceDetails.this, "Owners cannot rent their spaces", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void showRentDialog(int id2) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setTitle("Rent Space");
        builder.setMessage("Select the date and time you want to rent this space.");


        View view = LayoutInflater.from(this).inflate(R.layout.rent_dialog, null);
        builder.setView(view);


        DatePicker datePicker = view.findViewById(R.id.date_picker);
        TimePicker timePicker = view.findViewById(R.id.time_picker);


        Calendar calendar = Calendar.getInstance();
        datePicker.setMinDate(calendar.getTimeInMillis());


        builder.setPositiveButton("Rent", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int dayOfMonth = datePicker.getDayOfMonth();
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();


                String rentDateTime = year + "-" + (month + 1) + "-" + dayOfMonth + " " + hour + ":" + minute + ":00";


                int spaceId = id2;

                MyDatabaseHelper dbHelper = new MyDatabaseHelper(SpaceDetails.this);
                boolean inserted = dbHelper.insertIntoRentedSpaces(spaceId, rentDateTime);


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
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do nothing
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
