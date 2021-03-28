package com.example.transducer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transducer.DatabaseHelpers.AccelerometerDatabaseHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class AccelerometerActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView;
    ImageView backPage;
    private AccelerometerDatabaseHelper accelerometerDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        backPage = findViewById(R.id.backPageId);
        backPage.setOnClickListener(this);
        textView = findViewById(R.id.accelOutputId);
        accelerometerDatabaseHelper = new AccelerometerDatabaseHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor =  accelerometerDatabaseHelper.retrieveData();
        if(cursor.getCount()==0){
            Toast.makeText(this, "Database is empty", Toast.LENGTH_LONG).show();
        }

        // Show 3 axis accelerometer movement results in "meter per second-square" (m/s2) unit
        StringBuffer stringBuffer = new StringBuffer();
        while (cursor.moveToNext()){
            stringBuffer.append("Id: " + cursor.getString(0) + "\n");
            stringBuffer.append("Time: " + cursor.getString(1) + "\n");
            stringBuffer.append("Value of X axis: " + cursor.getString(2) + " m/s2\n");
            stringBuffer.append("Value of Y axis: " + cursor.getString(3) + " m/s2\n");
            stringBuffer.append("Value of Z axis: " + cursor.getString(4) + " m/s2\n\n");
        }

        textView.setText(stringBuffer.toString());
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backPageId){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}
