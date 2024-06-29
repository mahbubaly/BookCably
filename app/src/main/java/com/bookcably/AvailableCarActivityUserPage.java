package com.bookcably;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;

public class AvailableCarActivityUserPage extends AppCompatActivity {

    private ListView listViewCars;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_available_car_user_page);


        listViewCars = findViewById(R.id.list_view_Cars);

        Button btnTakeRent = findViewById(R.id.takeRent);

        databaseHelper = new DatabaseHelper(this);

        displayCarItems();


        btnTakeRent.setOnClickListener(v -> {
            Intent intent = new Intent(AvailableCarActivityUserPage.this, TakingRentAcitivty.class);
            startActivity(intent);
        });

    }


    private void displayCarItems() {
        Cursor cursor = databaseHelper.getAllProducts();
        if (cursor != null) {
            Log.d("Cursor Data", "Number of rows in cursor: " + cursor);
           // Toast.makeText(AvailableCarActivityUserPage.this, "Showing available cars...", Toast.LENGTH_SHORT).show();


            CarAdapter adapter = new CarAdapter(this, cursor, 0);

            try {
                listViewCars.setAdapter(adapter);
            } catch (Exception e) {
                Toast.makeText(AvailableCarActivityUserPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } else {
            Log.e("Cursor Data", "Cursor is null");
        }
    }


}
