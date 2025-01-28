package com.bookcably;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.application.R;

public class CarAdapter extends CursorAdapter {

    public CarAdapter(Context context, Cursor cursor, int flags){

        super(context,cursor,flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_of_cars,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView BrandNameTextView = view.findViewById(R.id.text_viewBrandName);
        TextView ModelNameTextView = view.findViewById(R.id.text_view_ModelName);
        TextView SeatNumberView = view.findViewById(R.id.text_view_Seat);
        TextView CarNumberTextView = view.findViewById(R.id.text_view_CarNumber);
        TextView perHourTextView = view.findViewById(R.id.text_view_Cost);
        ImageView productImageView = view.findViewById(R.id.image_view_product);

        //Calling from database:
        String brandNam = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_BNAME));

        String Modelnam = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_MODELNUMBER));

        int seatNa = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_SEATNUMBER));

        String carNum = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_CARNUMBER));

        int costHo = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_COSTPERDAY));

        byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_IMAGE_URI));
        Log.d("Image bytes: ", "adapter: " + imageBytes.toString());
        // Set text and image
        BrandNameTextView.setText("Brand Name: " + brandNam);
        ModelNameTextView.setText("Model: " + Modelnam);

        SeatNumberView.setText(String.valueOf("Number of Seat: "+ seatNa));
        CarNumberTextView.setText("Car Number: "+ carNum);


        perHourTextView.setText(String.valueOf("Per Hour cost: "+costHo+"৳"));

        if (imageBytes != null && imageBytes.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            if (bitmap != null) {
                productImageView.setImageBitmap(bitmap);
            } else {
                Log.e("CarAdapter", "Failed to decode bitmap");
            }
        } else {
            Log.e("CarAdapter", "Image data is null or empty");
            // Optionally set a default image or hide the ImageView
            productImageView.setImageResource(R.drawable.adminimage);
        }

    }
}
