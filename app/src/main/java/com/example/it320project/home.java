package com.example.it320project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
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

        logOutBtn = findViewById(R.id.logoutBtn);
        addBtn = findViewById(R.id.Addbtn);

        db = new MyDatabaseHelper(this);
        spaceList = db.getAllSpaces();
        rv = findViewById(R.id.rwMain);
        rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        spaceAdapter = new SpaceListAdapter(this, spaceList, rv);
        rv.setAdapter(spaceAdapter);


        Spinner spinner = findViewById(R.id.spinnerLoc);
        List<String> filterOptions = Arrays.asList("All", "North of Riyadh", "South of Riyadh", "West of Riyadh", "East of Riyadh");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, filterOptions);
        spinner.setAdapter(adapter);

        Spinner spinner2 = findViewById(R.id.spinnerCap);
        List<String> filterOptions2 = Arrays.asList("All", "10-20", "20-30", "30-40", "40 and more");
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, filterOptions2);
        spinner2.setAdapter(adapter2);

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

        SearchView searchView = findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                List<Space> matchingSpaces = db.searchSpaces(newText);
                spaceAdapter.setData(matchingSpaces);
                return true;
            }
        });


    }}
