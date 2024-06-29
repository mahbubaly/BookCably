package com.bookcably;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;

public class UserProfilePageActivity extends AppCompatActivity {

    private TextView tvUsername, tvEmail, tvPhone, tvPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_page);



    }
}
