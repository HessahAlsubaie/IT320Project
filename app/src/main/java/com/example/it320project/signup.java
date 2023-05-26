package com.example.it320project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class signup extends AppCompatActivity {

    Button signBtn;
    EditText username, password, email,phoneNumber;

    MyDatabaseHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        signBtn = findViewById(R.id.signUbBtn);
        DB = new MyDatabaseHelper(this);


        //check info before submitting
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String eml = email.getText().toString();
                String phone = phoneNumber.getText().toString();


                if (user.equals("") || pass.equals("") || eml.equals("") || phone.equals("")){
                    Toast.makeText(signup.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();}
                else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(eml).matches()) {
                    Toast.makeText(signup.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                } else if (!pass.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
                    Toast.makeText(signup.this, "Password must contain at least one uppercase letter, one lowercase letter, and one special character, and be at least 8 characters long", Toast.LENGTH_SHORT).show();
                }
                else{



                    Boolean checkuser = DB.checkUsername(user);
                    if (checkuser == false) {
                        Boolean insert = DB.insertData(user, pass, eml, phone);
                        if (insert == true) {
                            Toast.makeText(signup.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), home.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(signup.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(signup.this, "User already exists! please sign in", Toast.LENGTH_SHORT).show();
                    }

                }}
        });




    }}
