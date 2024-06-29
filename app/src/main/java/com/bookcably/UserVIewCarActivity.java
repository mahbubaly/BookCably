package com.bookcably;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;

public class UserVIewCarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_car);
        Button btnAvailableCar = findViewById(R.id.btn_AvailableCars);
        Button btnTakeRent = findViewById(R.id.btn_take_rent);
        Button btnProfile = findViewById(R.id.btn_Your_profile);

        btnAvailableCar.setOnClickListener(v->{
            Intent intent = new Intent(UserVIewCarActivity.this, AvailableCarActivityUserPage.class);
            startActivity(intent);
        });


        btnProfile.setOnClickListener(v->{
            Intent intent = new Intent(UserVIewCarActivity.this, UserProfilePageActivity.class);
            startActivity(intent);

        });

        btnTakeRent.setOnClickListener(v->{
            Intent intent = new Intent(UserVIewCarActivity.this, TakingRentAcitivty.class);
            startActivity(intent);

        });



    }
}