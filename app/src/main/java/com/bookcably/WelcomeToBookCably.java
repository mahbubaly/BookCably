package com.bookcably;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeToBookCably extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_to_book_cably);

        Button btnGetStarted = findViewById(R.id.btn_getStarted);

        // Get the currently signed-in user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userEmail = user.getEmail(); // Get the email of the current user

            // Check if the user is the admin
            if ("admin@gmail.com".equals(userEmail)) {
                Intent intent = new Intent(WelcomeToBookCably.this, AdminPanel.class);
                startActivity(intent);
                finish();
            } else {
                // Redirect normal users to the UserViewCarActivity
                Intent intent = new Intent(WelcomeToBookCably.this, UserVIewCarActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            // No user is signed in; show the "Get Started" button
            btnGetStarted.setOnClickListener(v -> {
                Intent intent = new Intent(WelcomeToBookCably.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        }
    }
}
