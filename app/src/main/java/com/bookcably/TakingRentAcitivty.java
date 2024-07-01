package com.bookcably;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;

public class TakingRentAcitivty extends AppCompatActivity {

    private EditText editTextModelName;
    private TextView TextCarBrandName;
    private TextView TextCarSeatNumber;
    private TextView TextCarNumber;
    private TextView perHourCost;
    private EditText hoursForRent;
    private TextView totalAmountBill;

    private ImageView imageViewProduct;
    private Button buttonConfirm;
    private Button buttonSearch;
    private Button buttonGoBackHome;
    private Button btnTotalAmountINTK;



    private DatabaseHelper databaseHelper;
    private byte[] productImageByteArray;

    private int costPerHour; // Added to hold cost per hour value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taking_rent_acitivty);

        editTextModelName = findViewById(R.id.edit_Take_car_modelNo);
        TextCarBrandName = findViewById(R.id.text_Take_carBrandName);
        TextCarSeatNumber = findViewById(R.id.tv_take_NumberOfSeat);
        TextCarNumber = findViewById(R.id.tv_take_text_CarNumber);
        perHourCost = findViewById(R.id.tv_take_text_perHourCost);
        hoursForRent = findViewById(R.id.edit_hourforTaking);
        totalAmountBill = findViewById(R.id.text_total_amount);

        imageViewProduct = findViewById(R.id.image_view_product);
        buttonConfirm = findViewById(R.id.button_confirm);
        buttonSearch = findViewById(R.id.button_search);
        btnTotalAmountINTK = findViewById(R.id.btn_Total_amount);
        buttonGoBackHome = findViewById(R.id.btn_goBackHome);

        databaseHelper = new DatabaseHelper(this);

        buttonSearch.setOnClickListener(view -> searchProduct());
        buttonConfirm.setOnClickListener(view -> confirmedProduct());
        buttonGoBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(TakingRentAcitivty.this, UserVIewCarActivity.class);
            startActivity(intent);
        });

        btnTotalAmountINTK.setOnClickListener(v -> calculateTotalBill());
    }

    private void searchProduct() {
        String productName = editTextModelName.getText().toString().trim();
        if (productName.isEmpty()) {
            Toast.makeText(this, "Please enter a product name to search", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = databaseHelper.getItemsByModelName(productName);
        if (cursor != null && cursor.moveToFirst()) {
            int productId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_ID));
            String brandNameOfCar = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_BNAME));
            String carNumber = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_CARNUMBER));
            int seatNumber = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_SEATNUMBER));
            costPerHour = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_COSTPERDAY)); // Store cost per hour
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_IMAGE_URI));

            TextCarBrandName.setText("Brand name: " + brandNameOfCar);
            TextCarSeatNumber.setText("Seat Number: " + seatNumber);
            TextCarNumber.setText("Car Number: " + carNumber);
            perHourCost.setText("Per hour cost: " + costPerHour + "৳");


            if (image != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                imageViewProduct.setImageBitmap(bitmap);
                productImageByteArray = image;
            }

            cursor.close();
        } else {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void calculateTotalBill() {
        String hoursForRentStr = hoursForRent.getText().toString().trim();
        if (!hoursForRentStr.isEmpty()) {
            int hours = Integer.parseInt(hoursForRentStr);
            int totalBill = costPerHour * hours;
            totalAmountBill.setText("Total Amount: " + totalBill + "৳");
        } else {
            Toast.makeText(this, "Please enter the number of hours", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmedProduct() {
        String brandName = TextCarBrandName.getText().toString().replace("Brand name: ", "").trim();
        String modelName = editTextModelName.getText().toString().trim();
        String carNumber = TextCarNumber.getText().toString().replace("Car Number: ", "").trim();
        String seatNumberStr = TextCarSeatNumber.getText().toString().replace("Seat Number: ", "").trim();
        String hoursForRentStr = hoursForRent.getText().toString().trim();

        if (modelName.isEmpty() || brandName.isEmpty() || carNumber.isEmpty() || seatNumberStr.isEmpty() || hoursForRentStr.isEmpty()) {
            Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show();
            return;
        }

        int seatNumber = Integer.parseInt(seatNumberStr);
        int hours = Integer.parseInt(hoursForRentStr);
        int totalAmount = costPerHour * hours;

        DatabaseHelper dbHelper = new DatabaseHelper(TakingRentAcitivty.this);
        boolean isInserted = dbHelper.insertBooking(brandName ,modelName, carNumber, seatNumber, costPerHour, hours, totalAmount, productImageByteArray);

        if (isInserted) {

            Intent intent = new Intent(TakingRentAcitivty.this,AvailableCarActivityUserPage.class);
            startActivity(intent);
            Toast.makeText(this, "Booking confirmed", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Booking failed", Toast.LENGTH_SHORT).show();
        }
    }

}
