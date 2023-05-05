package com.example.it320project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Add extends AppCompatActivity {

    EditText spaceName, location, category, price, capacity, description;
    Button addBtn, selectPhotoButton;
    Bitmap photoBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        spaceName=findViewById(R.id.spaceName);
        location=findViewById(R.id.Location);
        category=findViewById(R.id.Category);
        price=findViewById(R.id.price);
        capacity=findViewById(R.id.capacity);
        description=findViewById(R.id.description);

        addBtn=findViewById(R.id.submitInfoButton);
        selectPhotoButton=findViewById(R.id.selectPhotoButton);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (photoBitmap == null) {
                    Toast.makeText(Add.this, "Please select a photo", Toast.LENGTH_SHORT).show();
                    return;
                }

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] photoData = stream.toByteArray();

                Space spaceModel;
                try {
                    spaceModel = new Space(-1,spaceName.getText().toString(), location.getText().toString(),
                            category.getText().toString(), Integer.parseInt(price.getText().toString()), Integer.parseInt(capacity.getText().toString()),
                            description.getText().toString(),photoData);
                } catch (Exception e) {
                    Toast.makeText(Add.this, "invalid information", Toast.LENGTH_SHORT).show();
                    spaceModel = new Space(-1,"error", "error", "error", 0, 0, "error",null);
                }
                MyDatabaseHelper myDb = new MyDatabaseHelper(Add.this);
                boolean success = myDb.addOne(spaceModel);
                if(success)
                {Toast.makeText(Add.this, "Added Successfully", Toast.LENGTH_SHORT).show();}
                else {Toast.makeText(Add.this, "Not Added Successfully", Toast.LENGTH_SHORT).show();}
                Intent intent= new Intent( Add.this, home.class);
                startActivity(intent);
            }
        });

        selectPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1 && data != null) {
            Uri selectedImage = data.getData();
            try {
                photoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
