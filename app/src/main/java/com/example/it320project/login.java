package com.example.it320project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity {

    Button submitLog, signupBtn;
    EditText username1, password1;
    MyDatabaseHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        submitLog= findViewById(R.id.loginBtn);
        signupBtn=findViewById(R.id.signUpInlogin);
        username1=findViewById(R.id.username1);
        password1=findViewById(R.id.password1);
        DB= new MyDatabaseHelper(this);
        submitLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username1.getText().toString();
                String pass = password1.getText().toString();
                SharedPreferences preferences = getSharedPreferences("my_prefs", MODE_PRIVATE);

                if(user.equals("")||pass.equals(""))
                    Toast.makeText(login.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    Boolean check = DB.checkUsernamePassword(user, pass);
                    if (check){
                        Toast.makeText(login.this, "Sign in successfully", Toast.LENGTH_SHORT).show();
                        preferences.edit().putString("username", user).apply();
                        DB.setCurrentUserId(user);
                        Toast.makeText(login.this, "Sign in successfully " + user, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),home.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(login.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(login.this, signup.class);
                startActivity(intent);
            }
        });

    }
}