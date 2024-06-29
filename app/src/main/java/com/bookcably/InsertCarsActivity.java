package com.bookcably;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import com.example.application.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InsertCarsActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;

    private EditText brandName;
    private EditText modelNumber;
    private EditText numberOfSeat;
    private EditText carNumber;

    private EditText perDayCost;
    private ImageView UploadImageView;

    private Button selectImageButton;
    private Button uploadBtn;

    private byte[] imageByteArray;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_cars);

        brandName = findViewById(R.id.et_brand_name);
        modelNumber = findViewById(R.id.et_Model);
        numberOfSeat = findViewById(R.id.et_carSeater);
        carNumber = findViewById(R.id.et_carNumber);
        perDayCost = findViewById(R.id.et_perHourCost);


        UploadImageView = findViewById(R.id.iv_selected_image);
        selectImageButton = findViewById(R.id.btn_select_image);
        uploadBtn = findViewById(R.id.btn_upload);



        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri imageUri = result.getData().getData();
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    UploadImageView.setImageBitmap(imageBitmap);
                    imageByteArray = bitmapToByteArray(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        selectImageButton.setOnClickListener(view -> showImageSelectionDialog());

        uploadBtn.setOnClickListener(view -> insertProduct());
    }


    private void showImageSelectionDialog() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        imagePickerLauncher.launch(pickIntent);
    }



    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void insertProduct() {
        String NameOfBrand = brandName.getText().toString().trim();

        String model = modelNumber.getText().toString().trim();
        String SeatOfCar = numberOfSeat.getText().toString().trim();
        String carNumberString = carNumber.getText().toString().trim();
        String costPerDayString = perDayCost.getText().toString().trim();


        // Validate all fields
        if (NameOfBrand.isEmpty()  ||
                model.isEmpty() || SeatOfCar.isEmpty() ||
                carNumberString.isEmpty() || costPerDayString.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!!", Toast.LENGTH_SHORT).show();

        } else if ( imageByteArray == null) {
            Toast.makeText(this, "Please select an image!!", Toast.LENGTH_SHORT).show();

        }
        else{
            DatabaseHelper dbHelper = new DatabaseHelper(InsertCarsActivity.this);
            boolean inserted =  dbHelper.insertCar(NameOfBrand ,model,SeatOfCar,carNumberString,costPerDayString, imageByteArray);
           if (inserted){
               Toast.makeText(this, "Items inserted successfully", Toast.LENGTH_SHORT).show();

               Intent intent = new Intent(InsertCarsActivity.this,AdminPanel.class);
               startActivity(intent);

           }
           else{
               Toast.makeText(this, "Items not inserted ", Toast.LENGTH_SHORT).show();

           }




        }





    }

}

