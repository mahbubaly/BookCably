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

public class CarConfirmedAdapter extends CursorAdapter {


    public CarConfirmedAdapter(Context context, Cursor cursor, int flags) {
        super(context,cursor,flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_of_cars_on_rent,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView BrandNameTextView = view.findViewById(R.id.text_BookingBrandName);
        TextView ModelNameTextView = view.findViewById(R.id.text_view_bookingModelName);
        TextView bookingHours = view.findViewById(R.id.text_view_bookingHours);
        TextView CarNumberTextView = view.findViewById(R.id.text_viewBooking_CarNumber);
        TextView bookingCosting = view.findViewById(R.id.text_view_bookingCost);
        ImageView productImageView = view.findViewById(R.id.image_bookingview_product);




        //Calling from database:
        String brandNam = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_BOOKING_BRAND_NAME));
        String Modelnam = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_BOOKING_MODEL_NAME));
        String carNum = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_BOOKING_CAR_NUMBER));
        int totalBookingCostHo = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_BOOKING_COST_PER_HOUR));
        int bookingH = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_BOOKING_HOURS));

        byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_BOOKING_IMAGE));


        BrandNameTextView.setText("Brand Name: " + brandNam);
        ModelNameTextView.setText("Model: " + Modelnam);
        CarNumberTextView.setText("Car Number: "+ carNum);
        bookingHours.setText(String.valueOf("Total hours: "+bookingH+"h"));
        bookingCosting.setText(String.valueOf("Total amount: "+totalBookingCostHo+"৳"));

        if (imageBytes != null && imageBytes.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            if (bitmap != null) {
                productImageView.setImageBitmap(bitmap);
            } else {
                Log.e("CarAdapter", "Failed to decode bitmap");
            }
        } else {
            Log.e("CarAdapter", "Image data is null or empty");

            productImageView.setImageResource(R.drawable.adminimage);
        }

    }
}
