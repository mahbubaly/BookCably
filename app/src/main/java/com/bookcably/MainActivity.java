package com.bookcably;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private boolean isPasswordVisible = false;
    private ImageView ivShowPassword;

    private FirebaseAuth auth;
    private RelativeLayout progressOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLogin = findViewById(R.id.btn_login);
        TextView signUpText = findViewById(R.id.tv_dont_have_ac);
        TextView passwordError = findViewById(R.id.tv_error_password);
        TextView emailError = findViewById(R.id.tv_error_email);
        TextView noAccountError = findViewById(R.id.tv_no_account_error);  // New TextView for "Don't have an account"

        EditText etUsername = findViewById(R.id.et_userEmail);
        EditText etPassword = findViewById(R.id.et_password);
        TextView etForgotPassword = findViewById(R.id.tv_forgot_password);

        ivShowPassword = findViewById(R.id.iv_show_password);
        progressOverlay = findViewById(R.id.progress_overlay);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Clear error dynamically as the user types
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailError.setVisibility(View.GONE);
                noAccountError.setVisibility(View.GONE);  // Hide "No account" error
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Log in button:
        btnLogin.setOnClickListener(v -> {
            showProgress(true);
            String email = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString();

            if (email.isEmpty()) {
                emailError.setText("Please enter email");
                emailError.setVisibility(View.VISIBLE);
                showProgress(false); // Hide progress immediately
                return;
            }

            if (password.isEmpty()) {
                passwordError.setText("Please enter password");
                passwordError.setVisibility(View.VISIBLE);
                showProgress(false); // Hide progress immediately
                return;
            }

            // If the user is admin:
            if (email.equals("admin@gmail.com")) {
                if (password.equals("password123")) {
                    Intent intent = new Intent(MainActivity.this, AdminPanel.class);
                    startActivity(intent);
                    new Handler().postDelayed(() -> showProgress(false), 3000);
                    etPassword.setText("");
                    finish();
                } else {
                    passwordError.setText("Incorrect admin password");
                    passwordError.setVisibility(View.VISIBLE);
                    showProgress(false); // Hide progress after error
                }
            } else {
                // Normal user login check:
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Log in successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, UserVIewCarActivity.class);
                                intent.putExtra("username", email);
                                startActivity(intent);
                                showProgress(false); // Hide progress after success
                                etPassword.setText("");
                                finish();
                            } else {
                                passwordError.setText("Wrong credentials");
                                passwordError.setVisibility(View.VISIBLE);
                                noAccountError.setText("Don't have an account? Create one first.");
                                noAccountError.setVisibility(View.VISIBLE);  // Show "No account" message
                                showProgress(false); // Hide progress after failure
                            }
                        });
            }
        });

        // Register button:
        signUpText.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        etForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        // Password visibility toggle:
        ivShowPassword.setOnClickListener(v -> {
            if (isPasswordVisible) {
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                ivShowPassword.setImageResource(R.drawable.open_eye); // Replace with your eye icon
            } else {
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                ivShowPassword.setImageResource(R.drawable.visible_eye); // Replace with your closed eye icon
            }
            isPasswordVisible = !isPasswordVisible;
            etPassword.setSelection(etPassword.length());
        });
    }

    private void showProgress(boolean show) {
        progressOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
