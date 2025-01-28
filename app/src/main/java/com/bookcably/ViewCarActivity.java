package com.bookcably;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;

public class ViewCarActivity extends AppCompatActivity {

    private ListView listViewCars;

    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_car);

        listViewCars = findViewById(R.id.list_view_Cars);
        Button deleteBtn = findViewById(R.id.button_delete);
        Button UpdateBtn = findViewById(R.id.button_update);

        databaseHelper = new DatabaseHelper(this);

        displayCarItems();

        deleteBtn.setOnClickListener(v->{
            //Toast.makeText(ViewCarActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ViewCarActivity.this, DeleteCarsByNameAdmin.class);
            startActivity(intent);
            

        });

        UpdateBtn.setOnClickListener(v->{
            //Toast.makeText(ViewCarActivity.this, "Updated", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ViewCarActivity.this,UpdateCarInfo.class);
            startActivity(intent);



        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        displayCarItems();
    }


    private void displayCarItems(){
        Cursor cursor = databaseHelper.getAllProducts();
        if (cursor != null) {
            // Log.d("Cursor Data", "Number of rows in cursor: " + cursor);
           // Toast.makeText(ViewCarActivity.this, "Showing available cars...", Toast.LENGTH_SHORT).show();


            CarAdapter adapter = new CarAdapter(this,cursor,0);

            try {
                listViewCars.setAdapter(adapter);
            } catch (Exception e){
                Toast.makeText(ViewCarActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } else {
            Log.e("Cursor Data", "Cursor is null");
        }
    }
}