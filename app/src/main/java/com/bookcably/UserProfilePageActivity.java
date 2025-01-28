package com.bookcably;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserProfilePageActivity extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private EditText tvFullName, tvUsername, tvPhone;
    private TextView tvEmail, tvDateOfBirth, tvUsernameProfile;
    private Button BtnEditUserProfile, btnSaveProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_page);

        // Initialize views
        tvUsernameProfile = findViewById(R.id.tvUserProfile);
        tvFullName = findViewById(R.id.tvFullName);
        tvUsername = findViewById(R.id.tv_username);
        tvPhone = findViewById(R.id.tv_phone);
        tvEmail = findViewById(R.id.tv_email);
        tvDateOfBirth = findViewById(R.id.tv_date_of_birth);
        BtnEditUserProfile = findViewById(R.id.btn_edit_profile);
        btnSaveProfile = findViewById(R.id.btnSave);

        // Fetch current Firebase user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        if (user != null) {
            String email = user.getEmail();

            // Set email from Firebase
            tvEmail.setText(email);

            // Retrieve additional user information from Firestore
            firestore.collection("users")
                    .document(email)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Map<String, Object> userData = documentSnapshot.getData();
                            if (userData != null) {
                                String firstname = (String) userData.get("firstname");
                                String lastname = (String) userData.get("lastname");
                                String username = (String) userData.get("username");
                                String phone = (String) userData.get("phone");
                                String dob = (String) userData.get("dob");

                                tvFullName.setText(firstname + " " + lastname);
                                tvUsername.setText(username);
                                tvPhone.setText(phone);
                                tvDateOfBirth.setText(dob);
                                tvUsernameProfile.setText(firstname);
                            }
                        }
                    });
        }


        BtnEditUserProfile.setOnClickListener(v -> {
            tvFullName.setEnabled(true);
            tvUsername.setEnabled(true);
            tvPhone.setEnabled(true);

            tvEmail.setEnabled(false);
            tvDateOfBirth.setEnabled(false);

            btnSaveProfile.setVisibility(View.VISIBLE);
        });


        btnSaveProfile.setOnClickListener(v -> {
            String updatedFullName = tvFullName.getText().toString().trim();
            String updatedUsername = tvUsername.getText().toString().trim();
            String updatedPhone = tvPhone.getText().toString().trim();

            if (updatedFullName.isEmpty() || updatedUsername.isEmpty() || updatedPhone.isEmpty()) {
                Toast.makeText(this, "Please fill in all editable fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] nameParts = updatedFullName.split(" ", 2);
            String updatedFirstName = nameParts.length > 0 ? nameParts[0] : "";
            String updatedLastName = nameParts.length > 1 ? nameParts[1] : "";

            Map<String, Object> updatedData = new HashMap<>();
            updatedData.put("firstname", updatedFirstName);
            updatedData.put("lastname", updatedLastName);
            updatedData.put("username", updatedUsername);
            updatedData.put("phone", updatedPhone);

            FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
            if (user1 != null) {
                firestore.collection("users")
                        .document(user1.getEmail())
                        .update(updatedData)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Profile updated successfully.", Toast.LENGTH_SHORT).show();
                            tvFullName.setEnabled(false);
                            tvUsername.setEnabled(false);
                            tvPhone.setEnabled(false);
                            btnSaveProfile.setVisibility(View.GONE);
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "Error updating profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }
}
