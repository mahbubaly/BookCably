package com.bookcably;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private RelativeLayout progressOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        EditText etUserName = findViewById(R.id.et_username);
        EditText etFirstName = findViewById(R.id.et_firstName);
        EditText etLastName = findViewById(R.id.et_LastName);
        EditText etEmail = findViewById(R.id.et_email);
        EditText etPhone = findViewById(R.id.et_phoneNumber);
        EditText etDateOfBirth = findViewById(R.id.et_dateOfBirth);
        EditText etAge = findViewById(R.id.et_age);
        EditText etLicense = findViewById(R.id.et_License);
        EditText etPassword = findViewById(R.id.et_password);
        EditText etConfirmPassword = findViewById(R.id.et_ConfirmPassword);

        progressOverlay = findViewById(R.id.progress_overlay);

        Button btnSignUp = findViewById(R.id.btn_signUp);
        TextView textViewAlreadyHaveAccount = findViewById(R.id.tv_already_have_ac);

        ImageView ivShowPassword = findViewById(R.id.iv_showPassword);
        ImageView ivShowConfirmPassword = findViewById(R.id.iv_showConfirmPassword);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Toggle password visibility
        ivShowPassword.setOnClickListener(v -> togglePasswordVisibility(etPassword));
        ivShowConfirmPassword.setOnClickListener(v -> togglePasswordVisibility(etConfirmPassword));

        // Auto-fill age when date of birth changes
        etDateOfBirth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String dateOfBirth = s.toString().trim();
                if (!TextUtils.isEmpty(dateOfBirth) && dateOfBirth.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        Date dob = sdf.parse(dateOfBirth);
                        if (dob != null) {
                            Calendar dobCalendar = Calendar.getInstance();
                            dobCalendar.setTime(dob);
                            etAge.setText(String.valueOf(calculateAge(dobCalendar)));
                        }
                    } catch (ParseException e) {
                        etAge.setText("");
                    }
                } else {
                    etAge.setText("");
                }
            }
        });

        btnSignUp.setOnClickListener(v -> {
            showProgress(true);

            // Get user input
            String userName = etUserName.getText().toString().trim();
            String firstName = etFirstName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phoneNumber = etPhone.getText().toString().trim();
            String dateOfBirth = etDateOfBirth.getText().toString().trim();
            String ageStr = etAge.getText().toString().trim();
            String license = etLicense.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // Initialize error fields
            TextView errorEmail = findViewById(R.id.tv_error_email);

            // Clear all error messages
            TextView[] errorViews = {
                    findViewById(R.id.tv_error_username),
                    findViewById(R.id.tv_error_firstName),
                    findViewById(R.id.tv_error_lastName),
                    errorEmail,
                    findViewById(R.id.tv_error_phoneNumber),
                    findViewById(R.id.tv_error_dateOfBirth),
                    findViewById(R.id.tv_error_age),
                    findViewById(R.id.tv_error_license),
                    findViewById(R.id.tv_error_password),
                    findViewById(R.id.tv_error_confirmPassword)
            };
            for (TextView tv : errorViews) tv.setVisibility(View.GONE);

            boolean hasError = false;

            // Validate fields
            if (TextUtils.isEmpty(userName)) {
                showError(findViewById(R.id.tv_error_username), "Username is required");
                hasError = true;
            }
            if (TextUtils.isEmpty(firstName)) {
                showError(findViewById(R.id.tv_error_firstName), "First name is required");
                hasError = true;
            }
            if (TextUtils.isEmpty(lastName)) {
                showError(findViewById(R.id.tv_error_lastName), "Last name is required");
                hasError = true;
            }
            if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showError(errorEmail, "Invalid email");
                hasError = true;
            }
            if (!phoneNumber.matches("\\+8801[3-9]\\d{8}")) {
                showError(findViewById(R.id.tv_error_phoneNumber), "Invalid phone number");
                hasError = true;
            }
            if (!dateOfBirth.matches("\\d{4}-\\d{2}-\\d{2}")) {
                showError(findViewById(R.id.tv_error_dateOfBirth), "Invalid date format");
                hasError = true;
            }
            if (TextUtils.isEmpty(password) || !password.equals(confirmPassword)) {
                showError(findViewById(R.id.tv_error_password), "Passwords do not match");
                hasError = true;
            }

            if (hasError) {
                showProgress(false);
                return;
            }

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                user.sendEmailVerification().addOnCompleteListener(emailTask -> {
                                    if (emailTask.isSuccessful()) {
                                        saveUserDataToFireStore(userName, email, firstName, lastName, phoneNumber, dateOfBirth, ageStr, license);
                                        Toast.makeText(this, "Check your email to verify", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(this, MainActivity.class));
                                        finish();
                                    }
                                });
                            }
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                // Email already in use
                                showError(errorEmail, "This email is already registered");
                            } else {
                                Toast.makeText(this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        showProgress(false);
                    });
        });


        textViewAlreadyHaveAccount.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        });
    }

    private void showError(TextView textView, String message) {
        textView.setText(message);
        textView.setVisibility(View.VISIBLE);
    }

    private void saveUserDataToFireStore(String username, String email, String firstname, String lastname, String phone, String dob, String age, String hasLicense) {
        // Create a map with the user's data
        Map<String, String> userData = new HashMap<>();
        userData.put("username", username);
        userData.put("email", email);
        userData.put("firstname", firstname);
        userData.put("lastname", lastname);
        userData.put("phone", phone);
        userData.put("dob", dob);
        userData.put("age", age);
        userData.put("hasLicense", hasLicense);

        // Use the email as the document ID and store the user data
        firestore.collection("users").document(email).set(userData)
                .addOnSuccessListener(documentReference -> Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error saving user data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }


    private void showProgress(boolean show) {
        if (progressOverlay != null) progressOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private int calculateAge(Calendar dob) {
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }

    private void togglePasswordVisibility(EditText passwordEditText) {
        int start = passwordEditText.getSelectionStart();
        int end = passwordEditText.getSelectionEnd();
        if (passwordEditText.getInputType() == 129) {
            passwordEditText.setInputType(1);
        } else {
            passwordEditText.setInputType(129);
        }
        passwordEditText.setSelection(start, end);
    }
}
