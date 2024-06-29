package com.bookcably;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;

public class MainActivity extends AppCompatActivity {
    private boolean isPasswordVisible = false;
    private ImageView ivShowPassword;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLogin = findViewById(R.id.btn_login);
        Button btnRegister = findViewById(R.id.btn_register);

        EditText etUsername = findViewById(R.id.et_username);
        EditText etPassword = findViewById(R.id.et_password);
        TextView etForgotPassword = findViewById(R.id.tv_forgot_password);

        ivShowPassword = findViewById(R.id.iv_show_password);


        //Log in button:
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString();

            if (username.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter username", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
            } else {
                // If the user is admin:
                if (username.equals("admin")) {
                    if (password.equals("password")) {
                        Intent intent = new Intent(MainActivity.this, AdminPanel.class);
                        startActivity(intent);

                        // Clear fields after successful login

                        etPassword.setText("");
                    } else {
                        Toast.makeText(MainActivity.this, "Incorrect admin password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Normal user login check:
                    DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this);
                    boolean validUserOrNot = dbHelper.checkUserByUsername(username, password);
                    if (validUserOrNot) {
                        Toast.makeText(MainActivity.this, "Log in successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,UserVIewCarActivity.class);
                        startActivity(intent);

                        // Clear fields after successful login

                        etPassword.setText("");
                    } else {
                        Toast.makeText(MainActivity.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        //Register button:

        btnRegister.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        etForgotPassword.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });


        //password visible button:

        ivShowPassword.setOnClickListener( V-> {
                if (isPasswordVisible) {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivShowPassword.setImageResource(R.drawable.open_eye); // Replace with your eye icon
                } else {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivShowPassword.setImageResource(R.drawable.visible_eye); // Replace with your closed eye icon
                }
                isPasswordVisible = !isPasswordVisible;
                etPassword.setSelection(etPassword.length());
            }
        );

    }
}