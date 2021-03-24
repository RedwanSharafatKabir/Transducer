package com.example.transducer;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Html;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView lightSensorOutput, proximitySensorOutput, gyroscopeSensorOutput, accelerometerSensorOutput;
    SensorManager sensorManager;
    Sensor lightSensor, proximitySensor, accelerometerSensor, gyroscopeSensor;
    SensorEventListener lightListener, proximityListener, accelerometerListener, gyroscopeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        lightSensorOutput = findViewById(R.id.lightSensorOutputId);
        proximitySensorOutput = findViewById(R.id.proximitySensorOutputId);
        accelerometerSensorOutput = findViewById(R.id.accelerometerSensorOutputId);
        gyroscopeSensorOutput = findViewById(R.id.gyroscopeSensorOutputId);

        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // set unregisterListener for each 4 sensors when app is paused
        sensorManager.unregisterListener(lightListener);
        sensorManager.unregisterListener(proximityListener);
        sensorManager.unregisterListener(accelerometerListener);
        sensorManager.unregisterListener(gyroscopeListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onSensorChangedMethod();

        // set registerListener for each 4 sensors in sensorManager
        sensorManager.registerListener(lightListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(proximityListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(gyroscopeListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onSensorChangedMethod() {
        // Light sensor detection code >>>>>>>>>>
        lightListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(event.sensor.getType() == Sensor.TYPE_LIGHT){

                    // Show result in "lux" unit
                    lightSensorOutput.setText("Light sensor detected: " + event.values[0] + " lux");
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
                        // If something is within maximum range of device
                        proximitySensorOutput.setText("\nProximity sensor detected: " + event.values[0] + " cm");
                    } else {
                        // If there is nothing within the maximum range of device
                        proximitySensorOutput.setText("\nProximity sensor not detected: " + event.values[0] + " cm");
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };

        // Accelerometer sensor detection code >>>>>>>>>>
        // First check if accelerometer is not null
        if(!(accelerometerSensor == null)) {
            accelerometerListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){

                        // Show 3 axis accelerometer movement results in "meter per second-square" (m/s2) unit
                        accelerometerSensorOutput.setText("\nAccelerometer X axis movement: " + event.values[0] + " m/s2" +
                                "\nAccelerometer Y axis movement: " + event.values[1] + " m/s2" +
                                "\nAccelerometer Z axis movement: " + event.values[2] + " m/s2");
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {}
            };
        }
        // If accelerometer is null
        else if(accelerometerSensor == null){
            gyroscopeSensorOutput.setText("\nAccelerometer not working on device");
        }

        // Gyroscope sensor detection code >>>>>>>>>>
        // First check if gyroscope is not null
        if(!(gyroscopeSensor == null)) {
            gyroscopeListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

                        // Show 3 axis gyroscope movement results in "degree per second" (°/s) unit
                        gyroscopeSensorOutput.setText("\nGyroscope X axis movement: " + event.values[0] + " °/s" +
                                "\nGyroscope Y axis movement: " + event.values[1] + " °/s" +
                                "\nGyroscope Z axis movement: " + event.values[2] + " °/s");
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {}
            };
        }
        // If gyroscope is null
        else if(gyroscopeSensor == null){
            gyroscopeSensorOutput.setText("\nGyroscope not working on device");
        }
    }
}
