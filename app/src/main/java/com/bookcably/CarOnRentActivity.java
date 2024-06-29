package com.bookcably;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;

public class CarOnRentActivity extends AppCompatActivity {
    private ListView listViewCars;

    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_on_rent);

        listViewCars = findViewById(R.id.lists_view_Cars_on_rent);

        databaseHelper = new DatabaseHelper(this);

        displayCarItems();

    }



    private void displayCarItems(){
        Cursor cursor = databaseHelper.getAllConfirmedProduct();
        if (cursor != null) {
            // Log.d("Cursor Data", "Number of rows in cursor: " + cursor);
            // Toast.makeText(ViewCarActivity.this, "Showing available cars...", Toast.LENGTH_SHORT).show();


            CarConfirmedAdapter adapter = new CarConfirmedAdapter(this,cursor,0);

            try {
                listViewCars.setAdapter(adapter);
            } catch (Exception e){
                Toast.makeText(CarOnRentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } else {
            Log.e("Cursor Data", "Cursor is null");
        }
    }


}


