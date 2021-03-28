package com.example.transducer;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.transducer.DatabaseHelpers.ProximityDatabaseHelper;

public class ProximityActivity extends AppCompatActivity implements View.OnClickListener{

    TextView textView1;
    ImageView backPage1;
    private ProximityDatabaseHelper proximityDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity);

        backPage1 = findViewById(R.id.backPageId1);
        backPage1.setOnClickListener(this);
        textView1 = findViewById(R.id.proximityOutputId);
        proximityDatabaseHelper = new ProximityDatabaseHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor =  proximityDatabaseHelper.retrieveData();
        if(cursor.getCount()==0){
            Toast.makeText(this, "Database is empty", Toast.LENGTH_LONG).show();
        }

        StringBuffer stringBuffer = new StringBuffer();
        while (cursor.moveToNext()){
            stringBuffer.append("Id: " + cursor.getString(0) + "\n");
            stringBuffer.append("Time: " + cursor.getString(1) + "\n");
            stringBuffer.append("Value: " + cursor.getString(2) + " cm\n\n");
        }

        textView1.setText(stringBuffer.toString());
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backPageId1){
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
