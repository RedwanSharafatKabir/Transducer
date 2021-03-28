package com.example.transducer;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.transducer.DatabaseHelpers.GyroscopeDatabaseHelper;

public class GyroscopeActivity extends AppCompatActivity implements View.OnClickListener{

    TextView textView3;
    ImageView backPage3;
    private GyroscopeDatabaseHelper gyroscopeDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope);

        backPage3 = findViewById(R.id.backPageId3);
        backPage3.setOnClickListener(this);
        textView3 = findViewById(R.id.gyroscopeOutputId);
        gyroscopeDatabaseHelper = new GyroscopeDatabaseHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor =  gyroscopeDatabaseHelper.retrieveData();
        if(cursor.getCount()==0){
            Toast.makeText(this, "Database is empty", Toast.LENGTH_LONG).show();
        }

        // Show 3 axis gyroscope movement results in "meter per second-square" (°/s) unit
        StringBuffer stringBuffer = new StringBuffer();
        while (cursor.moveToNext()){
            stringBuffer.append("Id: " + cursor.getString(0) + "\n");
            stringBuffer.append("Time: " + cursor.getString(1) + "\n");
            stringBuffer.append("Value of X axis: " + cursor.getString(2) + " °/s\n");
            stringBuffer.append("Value of Y axis: " + cursor.getString(3) + " °/s\n");
            stringBuffer.append("Value of Z axis: " + cursor.getString(4) + " °/s\n\n");
        }

        textView3.setText(stringBuffer.toString());
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backPageId3){
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
