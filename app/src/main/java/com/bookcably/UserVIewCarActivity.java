package com.bookcably;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class UserVIewCarActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private String username; // Class-level variable to store username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_car);

        TextView userNameShow = findViewById(R.id.userNameFromMain);
        Button btnAvailableCar = findViewById(R.id.btn_AvailableCars);
        Button btnTakeRent = findViewById(R.id.btn_take_rent);
        Button btnProfile = findViewById(R.id.btn_Your_profile);
        Button btnLogOut = findViewById(R.id.btn_logOut);


        firestore = FirebaseFirestore.getInstance();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String email = user.getEmail();

            if (email != null) {
                firestore.collection("users")
                        .document(email)
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                Map<String, Object> userData = documentSnapshot.getData();
                                if (userData != null) {
                                    username = (String) userData.get("username");
                                    userNameShow.setText("Welcome to Bookcably, " + username);
                                }
                            }
                        })
                        .addOnFailureListener(e -> Log.e("FirestoreError", "Error fetching user data", e));
            }
        } else {
            userNameShow.setText("Welcome to Bookcably");
            Log.e("AuthError", "User is not authenticated");
        }

        // Set button listeners
        btnAvailableCar.setOnClickListener(v -> {
            Intent availableCarIntent = new Intent(UserVIewCarActivity.this, AvailableCarActivityUserPage.class);
            startActivity(availableCarIntent);
        });

        btnProfile.setOnClickListener(v -> {
            Intent profileIntent = new Intent(UserVIewCarActivity.this, UserProfilePageActivity.class);
            if (username != null) {
                profileIntent.putExtra("username", username);
            }
            startActivity(profileIntent);
        });

        btnTakeRent.setOnClickListener(v -> {
            Intent takeRentIntent = new Intent(UserVIewCarActivity.this, TakingRentAcitivty.class);
            startActivity(takeRentIntent);
        });

        btnLogOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent logoutIntent = new Intent(UserVIewCarActivity.this, MainActivity.class);
            startActivity(logoutIntent);
            finish();
        });
    }
}
