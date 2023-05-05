package com.example.it320project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity {

    Button logOutBtn;
    FloatingActionButton addBtn;

    MyDatabaseHelper db;
    RecyclerView rv;
    SpaceListAdapter spaceAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<Space> spaceList = new ArrayList<>();
    Button viewBtn;
    Button deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logOutBtn= findViewById(R.id.logoutBtn);
        addBtn= findViewById(R.id.Addbtn);

        db = new MyDatabaseHelper(this);
        spaceList = db.getAllSpaces();
        rv= findViewById(R.id.rwMain);
        rv.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        spaceAdapter = new SpaceListAdapter(this, spaceList,rv);
        rv.setAdapter(spaceAdapter);


        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, MainActivity.class);
                Toast.makeText(home.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(home.this, Add.class);
                startActivity(intent);
            }
        });


    }

    private void showDetails(Space space) {
        // Create an intent to start the details activity
        Intent intent = new Intent(home.this, SpaceDetails.class);

        // Pass the item data to the details activity
        intent.putExtra("name", space.getName());
        intent.putExtra("location", space.getLocation());
        intent.putExtra("category", space.getCategory());

        // Start the details activity
        startActivity(intent);
    }
}