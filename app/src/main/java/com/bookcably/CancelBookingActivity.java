package com.bookcably;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;

public class CancelBookingActivity extends AppCompatActivity {

    private EditText editTextModelName;
    private TextView BrandName;
    private TextView tvCarNumber;

    private Button buttonDelete;
    private Button buttonSearch;


    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_booking);

        editTextModelName = findViewById(R.id.ed_CancelByModelName);
        BrandName = findViewById(R.id.text_Brandname);
        tvCarNumber = findViewById(R.id.Text_CarNumber);

        buttonDelete = findViewById(R.id.btn_delete_user);
        buttonSearch = findViewById(R.id.button_search);


        // Initialize the database helper
        databaseHelper = new DatabaseHelper(this);

        buttonSearch.setOnClickListener(view -> searchProduct());
        buttonDelete.setOnClickListener(view -> showDeleteConfirmationDialog());


    }

    private void searchProduct() {
        String ModelName = editTextModelName.getText().toString().trim();
        if (ModelName.isEmpty()) {
            Toast.makeText(this, "Please enter a model name to search", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = databaseHelper.getSearchedUserByModelNameAndDelete(ModelName);
        if (cursor != null && cursor.moveToFirst()) {
            String brandName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_BNAME));
            String CarNumber = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_CARNUMBER));

            BrandName.setText("Brand name: " + brandName);
            tvCarNumber.setText("Car Number: " + CarNumber);

            cursor.close();
        } else {
            Toast.makeText(this, "Model name not found, please try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> deleteProduct())
                .setNegativeButton(android.R.string.no, null)
                .show();


        finish();
    }

    private void deleteProduct() {
        String model_name = editTextModelName.getText().toString().trim();

        if (model_name.isEmpty()) {
            Toast.makeText(this, "Please enter a model name to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isDeleted = databaseHelper.deleteUserBookingByAdmin(model_name);
        if (isDeleted) {
            Toast.makeText(this, "Booking canceled", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CancelBookingActivity.this, CarOnRentActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Cancellation failed, please try again later", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToActivity(Class<?> targetActivity) {
        Intent intent = new Intent(CancelBookingActivity.this, targetActivity);
        startActivity(intent);
        finish();
}

}
