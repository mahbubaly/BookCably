package com.bookcably;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;

public class DeleteUserActivity extends AppCompatActivity {

    private EditText editTextUserName;
    private TextView UserName;
    private TextView dateOfBirth;

    private Button buttonDelete;
    private Button buttonSearch;
    private Button buttonGoBackHome;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);

        editTextUserName = findViewById(R.id.ed_userName);
        UserName = findViewById(R.id.text_userFullname);
        dateOfBirth = findViewById(R.id.Text_date_ofBirth);

        buttonDelete = findViewById(R.id.btn_delete_user);
        buttonSearch = findViewById(R.id.button_search);
        buttonGoBackHome = findViewById(R.id.btn_UBackHome);

        // Initialize the database helper
        databaseHelper = new DatabaseHelper(this);

        buttonSearch.setOnClickListener(view -> searchProduct());
        buttonDelete.setOnClickListener(view -> showDeleteConfirmationDialog());

        buttonGoBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(DeleteUserActivity.this, AdminPanel.class);
            startActivity(intent);
            finish();
        });
    }

    private void searchProduct() {
        String userName = editTextUserName.getText().toString().trim();
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a username to search", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = databaseHelper.getSearchedUserByUsername(userName);
        if (cursor != null && cursor.moveToFirst()) {
            String firstname = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FIRSTNAME));
            String lastname = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LASTNAME));
            String dob = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DATEOFBIRTH));

            UserName.setText(firstname + " " + lastname);
            dateOfBirth.setText("Date of Birth: " + dob);

            cursor.close();
        } else {
            Toast.makeText(this, "User not found, please try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> deleteProduct())
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void deleteProduct() {
        String userName = editTextUserName.getText().toString().trim();

        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a username to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isDeleted = databaseHelper.deleteUserByAdmin(userName);
        if (isDeleted) {
            Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DeleteUserActivity.this, ViewCarActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "User not deleted", Toast.LENGTH_SHORT).show();
        }
    }
}
