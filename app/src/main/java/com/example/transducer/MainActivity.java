package com.example.transducer;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.transducer.BroadCastReceiverServices.BroadCastService;
import com.example.transducer.DatabaseHelpers.AccelerometerDatabaseHelper;
import com.example.transducer.DatabaseHelpers.GyroscopeDatabaseHelper;
import com.example.transducer.DatabaseHelpers.LightSensorDatabaseHelper;
import com.example.transducer.DatabaseHelpers.ProximityDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout proximityPage, lightSensorPage, accelerometerPage, gyroscopePage, about;
    SensorManager sensorManager;
    Sensor lightSensor, proximitySensor, accelerometerSensor, gyroscopeSensor;
    SensorEventListener lightListener, proximityListener, accelerometerListener, gyroscopeListener;
    private AccelerometerDatabaseHelper accelerometerDatabaseHelper;
    private LightSensorDatabaseHelper lightSensorDatabaseHelper;
    private GyroscopeDatabaseHelper gyroscopeDatabaseHelper;
    private ProximityDatabaseHelper proximityDatabaseHelper;
    long timeLeftInMilliseconds = 30000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, BroadCastService.class));

        proximityPage = findViewById(R.id.proximityPageId);
        lightSensorPage = findViewById(R.id.lightSensorPageId);
        accelerometerPage = findViewById(R.id.accelerometerPageId);
        gyroscopePage = findViewById(R.id.gyroscopePageId);
        about = findViewById(R.id.aboutId);
        proximityPage.setOnClickListener(this);
        lightSensorPage.setOnClickListener(this);
        accelerometerPage.setOnClickListener(this);
        gyroscopePage.setOnClickListener(this);
        about.setOnClickListener(this);

        accelerometerDatabaseHelper = new AccelerometerDatabaseHelper(this);
        lightSensorDatabaseHelper = new LightSensorDatabaseHelper(this);
        gyroscopeDatabaseHelper = new GyroscopeDatabaseHelper(this);
        proximityDatabaseHelper = new ProximityDatabaseHelper(this);

        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Update GUI
            updateGUI(intent);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        onSensorChangedMethod();
        registerReceiver(broadcastReceiver, new IntentFilter(BroadCastService.COUNTDOWN_BR));
//        startTimer();
        // set registerListener for each 4 sensors in sensorManager
        sensorManager.registerListener(lightListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(proximityListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(gyroscopeListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        // set unregisterListener for each 4 sensors when app is paused
        sensorManager.unregisterListener(lightListener);
        sensorManager.unregisterListener(proximityListener);
        sensorManager.unregisterListener(accelerometerListener);
        sensorManager.unregisterListener(gyroscopeListener);
    }

    @Override
    public void onStop() {
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Sensors cannot detect", Toast.LENGTH_SHORT).show();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, BroadCastService.class));
        super.onDestroy();
    }

    private void updateGUI(Intent intent) {
        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 30000);
            int seconds = (int) (millisUntilFinished / 1000);

            if(seconds<0){
                long temp1 = accelerometerDatabaseHelper.countRows();
                long temp2 = lightSensorDatabaseHelper.countRows();
                long temp3 = gyroscopeDatabaseHelper.countRows();
                long temp4 = proximityDatabaseHelper.countRows();
                for(long i=1; i<=temp1; i++){
                    accelerometerDatabaseHelper.deleteData(String.valueOf(i));
                }
                for(long i=1; i<=temp2; i++){
                    lightSensorDatabaseHelper.deleteData(String.valueOf(i));
                }
                for(long i=1; i<=temp3; i++){
                    gyroscopeDatabaseHelper.deleteData(String.valueOf(i));
                }
                for(long i=1; i<=temp4; i++){
                    proximityDatabaseHelper.deleteData(String.valueOf(i));
                }
            }
        }
    }

    // store values of 4 sensors in SQLite database
    public void onSensorChangedMethod() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        String timeData = simpleDateFormat.format(calendar.getTime());

        // Light sensor detection code >>>>>>>>>>
        lightListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(event.sensor.getType() == Sensor.TYPE_LIGHT){
                    // Show result in "lux" unit
                    lightSensorDatabaseHelper.insertData(timeData, String.valueOf(event.values[0]));
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };

        // Proximity sensor detection code >>>>>>>>>>
        proximityListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(event.sensor.getType() == Sensor.TYPE_PROXIMITY){
                    // getMaximumRange() is the maximum range of proximity sensor of device
                    // Show result in "centimetre" (cm) unit
                    if(event.values[0] < proximitySensor.getMaximumRange()){
                        // If an object is detected within maximum range of device
                        proximityDatabaseHelper.insertData(timeData, String.valueOf(event.values[0]));
                    } else {
                        // If no object is detected within the maximum range of device
                        proximityDatabaseHelper.insertData(timeData, String.valueOf(event.values[0]));
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };

        // Accelerometer sensor detection code >>>>>>>>>>
        // First check if accelerometer is not null
        if(accelerometerSensor != null) {
            accelerometerListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                        // Show 3 axis accelerometer movement results in "meter per second-square" (m/s2) unit
                        accelerometerDatabaseHelper.insertData(timeData,
                                String.valueOf(event.values[0]), String.valueOf(event.values[1]), String.valueOf(event.values[2]));
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {}
            };
        }
        // If accelerometer is null
        else if(accelerometerSensor == null){
            accelerometerDatabaseHelper.insertData(timeData, "not moved", "not moved", "not moved");
        }

        // Gyroscope sensor detection code >>>>>>>>>>
        // First check if gyroscope is not null
        if(gyroscopeSensor != null) {
            gyroscopeListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                        // Show 3 axis gyroscope movement results in "degree per second" (°/s) unit
                        gyroscopeDatabaseHelper.insertData(timeData,
                                String.valueOf(event.values[0]), String.valueOf(event.values[1]), String.valueOf(event.values[2]));
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {}
            };
        }
        // If gyroscope is null
        else if(gyroscopeSensor == null){
            gyroscopeDatabaseHelper.insertData(timeData, "not moved", "not moved", "not moved");
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.aboutId){
            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }

        if(v.getId()==R.id.proximityPageId){
            Intent intent = new Intent(getApplicationContext(), ProximityActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }

        if(v.getId()==R.id.lightSensorPageId){
            Intent intent = new Intent(getApplicationContext(), LightSensorActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }

        if(v.getId()==R.id.accelerometerPageId){
            Intent intent = new Intent(getApplicationContext(), AccelerometerActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }

        if(v.getId()==R.id.gyroscopePageId){
            Intent intent = new Intent(getApplicationContext(), GyroscopeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("EXIT !");
        alertDialogBuilder.setMessage("Are you sure you want to close this app ?");
        alertDialogBuilder.setIcon(R.drawable.exit);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });

        alertDialogBuilder.setNeutralButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
