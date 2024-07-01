package com.bookcably;

import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;

public class DeleteCarsByNameAdmin extends AppCompatActivity {

    private EditText editTextName;
    private TextView TextCarBrandName;
    private TextView TextCarSeatNumber;
    private TextView TextCarNumber;
    private TextView perHourCost;

    private ImageView imageViewProduct;
    private Button buttonDelete;
    private Button buttonSearch;
    private Button buttonGoBackHome;



    private DatabaseHelper databaseHelper;
    private byte[] productImageByteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_cars_by_name_admin);

        editTextName = findViewById(R.id.edit_car_modelNo);
        TextCarBrandName = findViewById(R.id.text_carBrandName);
        TextCarSeatNumber = findViewById(R.id.edit_text_NumberOfSeat);
        TextCarNumber = findViewById(R.id.edit_text_CarNumber);
        perHourCost = findViewById(R.id.edit_text_perHourCost);
        TextCarBrandName = findViewById(R.id.text_carBrandName);


        imageViewProduct = findViewById(R.id.image_view_product);
        buttonDelete = findViewById(R.id.button_delete);
        buttonSearch = findViewById(R.id.button_search);
        buttonGoBackHome = findViewById(R.id.btn_goBackHome);

        databaseHelper = new DatabaseHelper(this);

        buttonSearch.setOnClickListener(view -> searchProduct());
        buttonDelete.setOnClickListener(view -> showDeleteConfirmationDialog());

        buttonGoBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(DeleteCarsByNameAdmin.this, UserListActivityAdminPage.class);
            startActivity(intent);
        });
    }

    private void searchProduct() {
        String productName = editTextName.getText().toString().trim();
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
            int costPerHour = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_COSTPERDAY));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_IMAGE_URI));

            TextCarBrandName.setText("Brand name: " + brandNameOfCar);
            TextCarSeatNumber.setText(String.valueOf("Seat Number: " + seatNumber));
            TextCarNumber.setText("Car Number: " + carNumber);
            perHourCost.setText(String.valueOf(costPerHour + "৳"));


            if (image != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                imageViewProduct.setImageBitmap(bitmap);
                productImageByteArray = image;
            }
            cursor.close();
        } else {
            Toast.makeText(this, "Product not found please try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete this product?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> deleteProduct())
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void deleteProduct() {
        String productName = editTextName.getText().toString().trim();

        if (productName.isEmpty()) {
            Toast.makeText(this, "Please enter a product name to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isDeleted = databaseHelper.deleteProductByModelName(productName);
        if (isDeleted) {
            Toast.makeText(this, "Deleted ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DeleteCarsByNameAdmin.this, ViewCarActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Product not deleted", Toast.LENGTH_SHORT).show();
        }
    }
}
