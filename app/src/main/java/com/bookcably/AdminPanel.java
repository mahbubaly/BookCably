package com.bookcably;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;


public class AdminPanel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        Button insertBtn = findViewById(R.id.btn_insert_product);
        Button availableCar= findViewById(R.id.btn_CarInfo);
        Button viewUserBtn = findViewById(R.id.btn_Userlist);
        Button btnCarOnRent = findViewById(R.id.btn_UserBookedCars);
        Button btnHome = findViewById(R.id.btn_backHome);



        insertBtn.setOnClickListener(v->{
            Intent intent = new Intent(AdminPanel.this,InsertCarsActivity.class);
            startActivity(intent);
            Toast.makeText(AdminPanel.this, "Pressed insert", Toast.LENGTH_SHORT).show();
        });

        availableCar.setOnClickListener(v->{
            Intent intent = new Intent(AdminPanel.this,ViewCarActivity.class);
            startActivity(intent);
        });


        viewUserBtn.setOnClickListener(v->{
            Intent intent = new Intent(AdminPanel.this,UserListActivityAdminPage.class);
            startActivity(intent);
        });

        btnCarOnRent.setOnClickListener(v->{
            Intent intent = new Intent(AdminPanel.this,CarOnRentActivity.class);
            startActivity(intent);
        });
        btnHome.setOnClickListener(v->{
            Intent intent = new Intent(AdminPanel.this,MainActivity.class);
            startActivity(intent);
        });





    }
}