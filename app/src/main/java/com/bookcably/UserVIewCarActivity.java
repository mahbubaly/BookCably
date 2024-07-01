package com.bookcably;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;

public class UserVIewCarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_car);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

       TextView userNameShow = findViewById(R.id.userNameFromMain);

        userNameShow.setText("Welcome to bookcably "+ username);


        Button btnAvailableCar = findViewById(R.id.btn_AvailableCars);
        Button btnTakeRent = findViewById(R.id.btn_take_rent);
        Button btnProfile = findViewById(R.id.btn_Your_profile);



        btnAvailableCar.setOnClickListener(v->{
             Intent availableCarIntent = new Intent(UserVIewCarActivity.this, AvailableCarActivityUserPage.class);
            startActivity(availableCarIntent);
        });


        btnProfile.setOnClickListener(v->{
            Intent profileIntent = new Intent(UserVIewCarActivity.this, UserProfilePageActivity.class);
            profileIntent.putExtra("username",username);
            startActivity(profileIntent);

        });

        btnTakeRent.setOnClickListener(v->{
            Intent takeRentIntent = new Intent(UserVIewCarActivity.this, TakingRentAcitivty.class);
            startActivity(takeRentIntent);

        });



    }
}