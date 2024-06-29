package com.bookcably;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText etUserName = findViewById(R.id.et_username);
        EditText etFirstName = findViewById(R.id.et_firstName);
        EditText etFastName = findViewById(R.id.et_LastName);
        EditText etEmail = findViewById(R.id.et_email);
        EditText etPhone = findViewById(R.id.et_phoneNumber);
        EditText etDateOfBirth = findViewById(R.id.et_dateOfBirth);
        EditText etAge = findViewById(R.id.et_age);
        EditText etLicense = findViewById(R.id.et_License);
        EditText etPassword = findViewById(R.id.et_password);
        EditText etConfirmPassword = findViewById(R.id.et_ConfirmPassword);

        Button btnSignUp = findViewById(R.id.btn_signUp);
        TextView textView_already_have_ac = findViewById(R.id.tv_already_have_ac);

        //Authentication valid user
        btnSignUp.setOnClickListener(v -> {

            // Get string and convert this into string:
            String userName = etUserName.getText().toString().trim();
            String firstName = etFirstName.getText().toString().trim();
            String lastName = etFastName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phoneNumber = etPhone.getText().toString().trim();
            String dateOfBirth = etDateOfBirth.getText().toString().trim();
            String ageStr = etAge.getText().toString().trim();
            String license = etLicense.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // Validate all fields are filled
            if (userName.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
                    email.isEmpty() || phoneNumber.isEmpty() || dateOfBirth.isEmpty() ||
                    ageStr.isEmpty() || license.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate username is lowercase
            if (!userName.equals(userName.toLowerCase(Locale.US))) {
                Toast.makeText(RegisterActivity.this, "Username must be in lowercase", Toast.LENGTH_SHORT).show();
                return;
            }


            // Parse and validate age
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd", Locale.US);
            Date dob;
            try {
                dob = sdf.parse(dateOfBirth);
            } catch (ParseException e) {
                Toast.makeText(RegisterActivity.this, "Invalid date of birth format. Use yyyy-MM-dd.", Toast.LENGTH_SHORT).show();
                return;
            }

            Calendar dobCalendar = Calendar.getInstance();
            dobCalendar.setTime(dob);
            int calculatedAge = calculateAge(dobCalendar);
            int providedAge;
            try {
                providedAge = Integer.parseInt(ageStr);
            } catch (NumberFormatException e) {
                Toast.makeText(RegisterActivity.this, "Invalid age format", Toast.LENGTH_SHORT).show();
                return;
            }

            if (calculatedAge != providedAge) {
                Toast.makeText(RegisterActivity.this, "Your age doesn't matched with Date of birth!!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (calculatedAge < 18) {
                Toast.makeText(RegisterActivity.this, "Age must be 18 or older", Toast.LENGTH_SHORT).show();
                return;
            }


            // Validate license agreement
            if (!license.equalsIgnoreCase("yes")) {
                Toast.makeText(RegisterActivity.this, "You are not eligible for booking car!!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate password match:
            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(RegisterActivity.this, "Successfully sign up ", Toast.LENGTH_SHORT).show();

                //connection with database:
                DatabaseHelper dbHelper = new DatabaseHelper(RegisterActivity.this);
                boolean isInserted = dbHelper.insertUser(userName, firstName, lastName, email, phoneNumber, dateOfBirth, ageStr, license, password);

                if (isInserted) {
                    Toast.makeText(RegisterActivity.this, "Please login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "Not registered please try again!!", Toast.LENGTH_SHORT).show();

                }
            }


        });


        //Btn back to home page:

        textView_already_have_ac.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
        });


    }

    private int calculateAge(Calendar dobCalendar) {
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }
}