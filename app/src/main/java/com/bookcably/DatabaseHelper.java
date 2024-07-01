package com.bookcably;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DatabaseName = "CarRental";
    private static final int DatabaseVersion = 1;

    // Register table created:
    private static final String TABLE_REGISTER = "Register";

    private static final String COL_ID = "_id";
    static final String COL_USERNAME = "username";
    public static final String COL_FIRSTNAME = "FirstName";
    public static final String COL_LASTNAME = "LastName";
    public static final String COL_EMAIL = "Email";
    public static final String COL_MOBILE = "Mobile";
    public static final String COL_DATEOFBIRTH = "DateOfBirth";
    public static final String COL_DRIVINGLICENSE = "Licence";
    public static final String COL_AGE = "AGE";
    private static final String COL_PASSWORD = "Password";

    // Product table
    private static final String TABLE_PRODUCTS = "CarItems";
    public static final String COL_PRODUCT_ID = "_id";
    public static final String COL_PRODUCT_BNAME = "Brand_Name";
    public static final String COL_PRODUCT_MODELNUMBER = "Model_Number";
    static final String COL_PRODUCT_CARNUMBER = "CarNumber";
    public static final String COL_PRODUCT_SEATNUMBER = "NumberOfSeat";
    public static final String COL_PRODUCT_COSTPERDAY = "PerHourCost";
    public static final String COL_PRODUCT_IMAGE_URI = "ItemImageUrl";

    // Bookings
    public static final String TABLE_BOOKINGS = "Bookings";
    public static final String COL_BOOKING_ID = "_id";
    public static final String COL_BOOKING_MODEL_NAME = "model_name";
    public static final String COL_BOOKING_BRAND_NAME = "brand_name";
    public static final String COL_BOOKING_CAR_NUMBER = "car_number";
    public static final String COL_BOOKING_SEAT_NUMBER = "seat_number";
    public static final String COL_BOOKING_COST_PER_HOUR = "cost_per_hour";
    public static final String COL_BOOKING_HOURS = "hours";
    public static final String COL_BOOKING_TOTAL_AMOUNT = "total_amount";
    public static final String COL_BOOKING_IMAGE = "image";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Create tables again
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Register table
        db.execSQL("CREATE TABLE " + TABLE_REGISTER + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_FIRSTNAME + " TEXT, " +
                COL_LASTNAME + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_MOBILE + " BIGINT, " +
                COL_DATEOFBIRTH + " TEXT, " +
                COL_AGE + " INT, " +
                COL_PASSWORD + " TEXT, " +
                COL_DRIVINGLICENSE + " TEXT)");

        // Product table
        db.execSQL("CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COL_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PRODUCT_BNAME + " TEXT, " +
                COL_PRODUCT_MODELNUMBER + " TEXT, " +
                COL_PRODUCT_SEATNUMBER + " INTEGER, " +
                COL_PRODUCT_CARNUMBER + " TEXT, " +
                COL_PRODUCT_COSTPERDAY + " INTEGER, " +
                COL_PRODUCT_IMAGE_URI + " BLOB)");

        // Bookings table
        db.execSQL("CREATE TABLE " + TABLE_BOOKINGS + " (" +
                COL_BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_BOOKING_BRAND_NAME + " TEXT, " +
                COL_BOOKING_MODEL_NAME + " TEXT, " +
                COL_BOOKING_SEAT_NUMBER + " INTEGER, " +
                COL_BOOKING_CAR_NUMBER + " TEXT, " +
                COL_BOOKING_COST_PER_HOUR + " INTEGER, " +
                COL_BOOKING_HOURS + " INTEGER, " +
                COL_BOOKING_TOTAL_AMOUNT + " INTEGER, " +
                COL_BOOKING_IMAGE + " BLOB)");
    }

    public boolean insertUser(String userName, String firstName, String lastName, String email, String phoneNumber, String dateOfBirth, String ageStr, String license, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_USERNAME, userName);
        contentValues.put(COL_FIRSTNAME, firstName);
        contentValues.put(COL_LASTNAME, lastName);
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_MOBILE, phoneNumber);
        contentValues.put(COL_DATEOFBIRTH, dateOfBirth);
        contentValues.put(COL_AGE, ageStr);
        contentValues.put(COL_DRIVINGLICENSE, license);
        contentValues.put(COL_PASSWORD, password);

        long resultDb = db.insert(TABLE_REGISTER, null, contentValues);

        return resultDb != -1;
    }

    // Checking the user
    public boolean checkUserByUsername(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_REGISTER + " WHERE " + COL_USERNAME + " = ? AND " + COL_PASSWORD + " = ?", new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean insertCar(String nameOfBrand, String model, String seatOfCar, String carNumberString, String costPerDayString, byte[] imageByteArray) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_PRODUCT_BNAME, nameOfBrand);
        values.put(COL_PRODUCT_MODELNUMBER, model);
        values.put(COL_PRODUCT_SEATNUMBER, Integer.parseInt(seatOfCar));
        values.put(COL_PRODUCT_CARNUMBER, carNumberString);
        values.put(COL_PRODUCT_COSTPERDAY, Integer.parseInt(costPerDayString));
        values.put(COL_PRODUCT_IMAGE_URI, imageByteArray);
        long resultDb = db.insert(TABLE_PRODUCTS, null, values);

        return resultDb != -1;
    }

    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_REGISTER, null);
    }

    public Cursor getItemsByModelName(String SearchedModelName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COL_PRODUCT_MODELNUMBER + " = ?", new String[]{SearchedModelName});
    }

    public boolean updateProduct(int productId, String brandName, String modelName, int seatNumber, String carNumber, int costPerHour, byte[] productImageByteArray) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_PRODUCT_BNAME, brandName);
        values.put(COL_PRODUCT_MODELNUMBER, modelName);
        values.put(COL_PRODUCT_SEATNUMBER, seatNumber);
        values.put(COL_PRODUCT_CARNUMBER, carNumber);
        values.put(COL_PRODUCT_COSTPERDAY, costPerHour);
        values.put(COL_PRODUCT_IMAGE_URI, productImageByteArray);

        String whereClause = COL_PRODUCT_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(productId)};

        long resultDb = db.update(TABLE_PRODUCTS, values, whereClause, whereArgs);
        return resultDb != -1;
    }

    public boolean deleteProductByModelName(String modelName) {
        SQLiteDatabase db = this.getWritableDatabase();
        long resultDb = db.delete(TABLE_PRODUCTS, COL_PRODUCT_MODELNUMBER + " = ?", new String[]{modelName});
        return resultDb != -1;
    }

    public boolean insertBooking(String brandName, String modelName, String carNumber, int seatNumber, int costPerHour, int hours, int totalAmount, byte[] productImageByteArray) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_BOOKING_BRAND_NAME, brandName);
        values.put(COL_BOOKING_MODEL_NAME, modelName);
        values.put(COL_BOOKING_SEAT_NUMBER, seatNumber);
        values.put(COL_BOOKING_CAR_NUMBER, carNumber);
        values.put(COL_BOOKING_COST_PER_HOUR, costPerHour);
        values.put(COL_BOOKING_HOURS, hours);
        values.put(COL_BOOKING_TOTAL_AMOUNT, totalAmount);
        values.put(COL_BOOKING_IMAGE, productImageByteArray);

        long resultDb = db.insert(TABLE_BOOKINGS, null, values);
        return resultDb != -1;
    }

    public Cursor getAllConfirmedProduct() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_BOOKINGS, null);
    }

    // This is for delete user:
    public Cursor getSearchedUserByUsername(String SearchedUsername) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_REGISTER + " WHERE " + COL_USERNAME + " = ?", new String[]{SearchedUsername});
    }

    // Deleting user by admin:
    public boolean deleteUserByAdmin(String userName) {
        SQLiteDatabase db = this.getWritableDatabase();
        long resultDb = db.delete(TABLE_REGISTER, COL_USERNAME + " = ?", new String[]{userName});
        return resultDb != -1;
    }

    public Cursor getUserDetails(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_REGISTER, null, COL_USERNAME + "=?", new String[]{username},
                null, null, null);
    }

    public Cursor getSearchedUserByModelNameAndDelete(String modelName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COL_PRODUCT_MODELNUMBER + " = ?", new String[]{modelName});
    }

    public boolean deleteUserBookingByAdmin(String modelName) {
        SQLiteDatabase db = this.getWritableDatabase();
        long resultDb = db.delete(TABLE_BOOKINGS, COL_BOOKING_MODEL_NAME + " = ?", new String[]{modelName});
        return resultDb != -1;
    }
}
