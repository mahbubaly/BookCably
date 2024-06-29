package com.bookcably;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.application.R;

public class UserAdapter extends CursorAdapter {
    public UserAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_of_user_on_adminpage, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameOfUser = view.findViewById(R.id.text_NameOfUser);
        TextView emailOfUser = view.findViewById(R.id.text_view_email);
        TextView userPhoneNumber = view.findViewById(R.id.text_view_PhoneNumber); // Updated to the correct ID
        TextView userAge = view.findViewById(R.id.text_viewAge);
        TextView userLicense = view.findViewById(R.id.text_view_License);

        // Ensure none of the TextViews are null
        if (nameOfUser == null || emailOfUser == null || userPhoneNumber == null || userAge == null || userLicense == null) {
            throw new NullPointerException("One of the TextViews is null. Please check your layout IDs.");
        }

        // Get the text from database
        String userFirstName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FIRSTNAME));
        String userLastName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LASTNAME));
        String userEmail = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL));
        String userPhone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MOBILE));
        String license = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DRIVINGLICENSE));
        String age = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_AGE));

        // Set the texts
        nameOfUser.setText("Name: " + userFirstName + " " + userLastName);
        emailOfUser.setText("Email: "+userEmail);
        userPhoneNumber.setText("Phone: "+userPhone);
        userAge.setText("Age: "+age);
        userLicense.setText("Available License: "+license);
    }
}
