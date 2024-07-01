package com.bookcably;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UpdateCarInfo extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private TextView editTextBrandName;
    private EditText editTextModelName;
    private EditText editTextSeatNum;
    private EditText editTextCarNumber;
    private EditText editTextPerHourCost;
    private ImageView imageViewProduct;
    private Button buttonUpdate;
    private Button buttonSelectImage;
    private Button buttonSearch;
    private TextView textViewProductId;

    private DatabaseHelper databaseHelper;
    private byte[] productImageByteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_car_info);

        editTextModelName = findViewById(R.id.edit_car_modelNo);
        editTextBrandName = findViewById(R.id.text_carBrandName);
        editTextSeatNum = findViewById(R.id.edit_text_NumberOfSeat);
        editTextCarNumber = findViewById(R.id.edit_text_CarNumber);
        editTextPerHourCost = findViewById(R.id.edit_text_perHourCost);

        imageViewProduct = findViewById(R.id.image_view_product);

        buttonUpdate = findViewById(R.id.button_update);

        buttonSelectImage = findViewById(R.id.button_select_image);
        buttonSearch = findViewById(R.id.button_search);
        textViewProductId = findViewById(R.id.text_view_product_id);

        databaseHelper = new DatabaseHelper(this);

        buttonSearch.setOnClickListener(view -> searchProduct());
        buttonSelectImage.setOnClickListener(view -> selectImage());
        buttonUpdate.setOnClickListener(view -> updateProduct());
    }

    private void searchProduct() {
        String modelName = editTextModelName.getText().toString().trim();

        if (modelName.isEmpty()) {
            Toast.makeText(this, "Please enter a model name to search", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = databaseHelper.getItemsByModelName(modelName);
        if (cursor != null && cursor.moveToFirst()) {
            int productId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_ID));
            String brandNameOfCar = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_BNAME));
            String carNumber = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_CARNUMBER));
            int seatNumber = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_SEATNUMBER));
            int costPerHour = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_COSTPERDAY));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_IMAGE_URI));

            editTextBrandName.setText("Brand name: " + brandNameOfCar);
            editTextSeatNum.setText("Total Seat: " + seatNumber);
            editTextCarNumber.setText("Car Number: " + carNumber);
            editTextPerHourCost.setText(costPerHour + "৳");
//            textViewProductId.setText("Id: " + productId);

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

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageViewProduct.setImageBitmap(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                productImageByteArray = byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateProduct() {
        String brandName = editTextBrandName.getText().toString().replace("Brand name: ", "").trim();
        String modelName = editTextModelName.getText().toString().trim();
        String seatNum = editTextSeatNum.getText().toString().replace("Total Seat: ", "").trim();
        String carNumber = editTextCarNumber.getText().toString().replace("Car Number: ", "").trim();
        String perHourCost = editTextPerHourCost.getText().toString().replace("৳", "").trim();
        String productIdText = textViewProductId.getText().toString().replace("Id: ", "").trim();

        if (brandName.isEmpty() || modelName.isEmpty() || seatNum.isEmpty() || carNumber.isEmpty() || perHourCost.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int seatNumber = Integer.parseInt(seatNum);
        int costPerHour = Integer.parseInt(perHourCost);
        int productId = Integer.parseInt(productIdText);

        boolean isUpdated = databaseHelper.updateProduct(productId, brandName, modelName, seatNumber, carNumber, costPerHour, productImageByteArray);

        if (isUpdated) {
            Toast.makeText(this, "Information updated successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdateCarInfo.this, ViewCarActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Failed to update information", Toast.LENGTH_SHORT).show();
        }
    }
}
