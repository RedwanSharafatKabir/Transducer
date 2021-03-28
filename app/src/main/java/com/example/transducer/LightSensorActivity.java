package com.example.transducer;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.transducer.DatabaseHelpers.LightSensorDatabaseHelper;

public class LightSensorActivity extends AppCompatActivity implements View.OnClickListener{

    TextView textView2;
    ImageView backPage2;
    private LightSensorDatabaseHelper lightSensorDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_sensor);

        backPage2 = findViewById(R.id.backPageId2);
        backPage2.setOnClickListener(this);
        textView2 = findViewById(R.id.lightOutputId);
        lightSensorDatabaseHelper = new LightSensorDatabaseHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor =  lightSensorDatabaseHelper.retrieveData();
        if(cursor.getCount()==0){
            Toast.makeText(this, "Database is empty", Toast.LENGTH_LONG).show();
        }

        StringBuffer stringBuffer = new StringBuffer();
        while (cursor.moveToNext()){
            stringBuffer.append("Id: " + cursor.getString(0) + "\n");
            stringBuffer.append("Time: " + cursor.getString(1) + "\n");
            stringBuffer.append("Value: " + cursor.getString(2) + " lux\n\n");
        }

        textView2.setText(stringBuffer.toString());
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backPageId2){
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
