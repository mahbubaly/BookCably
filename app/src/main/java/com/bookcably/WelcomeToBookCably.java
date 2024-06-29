package com.bookcably;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;

public class WelcomeToBookCably extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_to_book_cably);
        Button btnGetStarted = findViewById(R.id.btn_getStarted);

        btnGetStarted.setOnClickListener(v->{
            Intent intent = new Intent(WelcomeToBookCably.this,MainActivity.class);
            startActivity(intent);
        });

    }
}