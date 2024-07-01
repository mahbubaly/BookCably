package com.bookcably;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;

public class UserProfilePageActivity extends AppCompatActivity {

    private TextView tvUsername, tvEmail, tvPhone,tvUsernameProfile ,fullName,tvDateOfBirth;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_page);

        // Initialize views
        tvUsernameProfile = findViewById(R.id.tvUserProfile);
        fullName = findViewById(R.id.tvFullName);
        tvUsername = findViewById(R.id.tv_username);
        tvEmail = findViewById(R.id.tv_email);
        tvPhone = findViewById(R.id.tv_phone);
        tvDateOfBirth = findViewById(R.id.tv_date_of_birth);

        // Retrieve username from intent
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        dbHelper = new DatabaseHelper(this);

        // Fetch user details if username is not null
        if (username != null) {
          fetchUserDetails(username);
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchUserDetails(String username) {
        // Use DatabaseHelper to fetch user details based on username
        Cursor cursor = dbHelper.getUserDetails(username);

        if (cursor != null && cursor.moveToFirst()) {
            // Extract data from cursor
            String Username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USERNAME));
            String firstname = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FIRSTNAME));
            String lastname = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LASTNAME));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MOBILE));
            String dob = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DATEOFBIRTH));


            // Populate EditText fields
            tvUsernameProfile.setText(firstname + " profile");
            fullName.setText(firstname + " " +lastname);
            tvUsername.setText(Username);
            tvEmail.setText(email);
            tvPhone.setText(phone);
            tvDateOfBirth.setText("" + dob);


        } else {
            Toast.makeText(this, "User details not found", Toast.LENGTH_SHORT).show();
        }

        // Close cursor
        if (cursor != null) {
            cursor.close();
        }
    }






}