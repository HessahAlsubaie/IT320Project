package com.example.it320project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class RentedSpacesActivity extends AppCompatActivity {

    private List<Space> spaces;
    FloatingActionButton homeBtn;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_rented_spaces);
            homeBtn=findViewById(R.id.backBtn);
            homeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(RentedSpacesActivity.this, home.class);
                    startActivity(intent);
                }
            });

            MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
            spaces = dbHelper.getRentedSpaces();

            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new RentedSpacesAdapter(this,this,spaces));
        }

    }
