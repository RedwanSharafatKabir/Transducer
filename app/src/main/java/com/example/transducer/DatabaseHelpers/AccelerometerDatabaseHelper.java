package com.example.transducer.DatabaseHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class AccelerometerDatabaseHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "AccelSensor.db";
    static final String TABLE_NAME = "Accelerometer_Details";
    static final int VERSION_NUMBER = 1;
    static final String ID = "Id";
    static final String TIME = "Time";
    static final String VALUEX = "X_axis";
    static final String VALUEY = "Y_axis";
    static final String VALUEZ = "Z_axis";
    static final String CREATE_TABLE_COMMAND = "CREATE TABLE " + TABLE_NAME + "( " + ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + TIME + " VARCHAR(255), " + VALUEX + " REAL, "
            + VALUEY + " REAL, " + VALUEZ + " REAL);";
    static final String SHOW_ALL_DATA_COMMAND = "SELECT * FROM " + TABLE_NAME;
    Context context;

    public AccelerometerDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try{
            sqLiteDatabase.execSQL(CREATE_TABLE_COMMAND);
        } catch (Exception e){
            Toast.makeText(context, "Exception: " + e, Toast.LENGTH_LONG).show();
        }
    }

    // Insert data into AccelSensor.db database
    public long insertData(String time, String valuex, String valuey, String valuez){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME, time);
        contentValues.put(VALUEX, valuex);
        contentValues.put(VALUEY, valuey);
        contentValues.put(VALUEZ, valuez);
        long rowId = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        return  rowId;
    }

    // Retrieve data from AccelSensor.db database
    public Cursor retrieveData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(SHOW_ALL_DATA_COMMAND, null);
        return cursor;
    }

    // Count row numbers of Accelerometer_Details table
    public long countRows() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        long rowNumbers = DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NAME);
        sqLiteDatabase.close();

        return rowNumbers;
    }

    // Delete data from AccelSensor.db database
    public Integer deleteData(String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME, ID + " = ?", new String[]{id});
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {}
}
