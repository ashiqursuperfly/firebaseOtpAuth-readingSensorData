package ashiqur.androidsensors;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import ashiqur.androidsensors.module1.LoginActivity;
import ashiqur.ashiqur_util.firebase.FireBaseDatabaseUtil;
import ashiqur.ashiqur_util.firebase.FirebaseAuthUtil;

import static ashiqur.ashiqur_util.UiUtil.setSensor;

public class MainActivity extends AppCompatActivity {

    private TextView tvAccelerometer, tvGyrometer, tvMagnetoMeter, tvLight, tvPressure, tvTemperature, tvHumidity, tvProximity, tvStepDetector, tvOrientation;
    public static final String TAG = "MainActivity";

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = new FirebaseAuthUtil().getAuth().getCurrentUser();
        if(currentUser != null) Log.wtf(TAG, currentUser.getPhoneNumber());
        else
        {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeXmlVariables();
        setSensors();


    }

   private void setSensors()
   {
       if(!setSensor(Sensor.TYPE_ACCELEROMETER, new SensorEventListener() {
           @Override
           public void onSensorChanged(SensorEvent event) {
               //Log.d(TAG, Arrays.toString(event.values));
               tvAccelerometer.setText(Arrays.toString(event.values));
           }

           @Override
           public void onAccuracyChanged(Sensor sensor, int accuracy) {


           }
       }, MainActivity.this)) {
           String error = "Device Doesnt Support Accelerometer";
           tvAccelerometer.setText(error);
       }
       if(!setSensor(Sensor.TYPE_GYROSCOPE, new SensorEventListener() {
           @Override
           public void onSensorChanged(SensorEvent event) {
               //  Log.d(TAG, Arrays.toString(event.values));
               tvGyrometer.setText(Arrays.toString(event.values));
           }

           @Override
           public void onAccuracyChanged(Sensor sensor, int accuracy) {


           }
       }, MainActivity.this)) {
           String error = "Device Doesnt Support Gyrometer";
           tvGyrometer.setText(error);
       }
       if(!setSensor(Sensor.TYPE_AMBIENT_TEMPERATURE, new SensorEventListener() {
           @Override
           public void onSensorChanged(SensorEvent event) {
               //   Log.d(TAG, Arrays.toString(event.values));
               tvTemperature.setText(Arrays.toString(event.values));
           }

           @Override
           public void onAccuracyChanged(Sensor sensor, int accuracy) {


           }
       }, MainActivity.this)) {
           String error = "Device Doesnt Support Temperature Sensor";
           tvTemperature.setText(error);
       }
       if(!setSensor(Sensor.TYPE_MAGNETIC_FIELD, new SensorEventListener() {
           @Override
           public void onSensorChanged(SensorEvent event) {
               // Log.d(TAG, Arrays.toString(event.values));
               tvMagnetoMeter.setText(Arrays.toString(event.values));
           }

           @Override
           public void onAccuracyChanged(Sensor sensor, int accuracy) {


           }
       }, MainActivity.this)) {
           String error = "Device Doesnt Support Magnetometer";
           tvMagnetoMeter.setText(error);
       }
       if(!setSensor(Sensor.TYPE_LIGHT, new SensorEventListener() {
           @Override
           public void onSensorChanged(SensorEvent event) {
               //  Log.d(TAG, Arrays.toString(event.values));
               tvLight.setText(Arrays.toString(event.values));
           }

           @Override
           public void onAccuracyChanged(Sensor sensor, int accuracy) {


           }
       }, MainActivity.this)) {
           String error = "Device Doesnt Support Light Sensor";
           tvLight.setText(error);
       }
       if(!setSensor(Sensor.TYPE_PRESSURE, new SensorEventListener() {
           @Override
           public void onSensorChanged(SensorEvent event) {
               //  Log.d(TAG, Arrays.toString(event.values));
               tvPressure.setText(Arrays.toString(event.values));
           }

           @Override
           public void onAccuracyChanged(Sensor sensor, int accuracy) {


           }
       }, MainActivity.this)) {
           String error = "Device Doesnt Support Barometer";
           tvPressure.setText(error);
       }
       if(!setSensor(Sensor.TYPE_RELATIVE_HUMIDITY, new SensorEventListener() {
           @Override
           public void onSensorChanged(SensorEvent event) {
               //  Log.d(TAG, Arrays.toString(event.values));
               tvHumidity.setText(Arrays.toString(event.values));
           }

           @Override
           public void onAccuracyChanged(Sensor sensor, int accuracy) {


           }
       }, MainActivity.this)) {
           String error = "Device Doesnt Support Humidity Sensor";
           tvHumidity.setText(error);
       }
       if(!setSensor(Sensor.TYPE_PROXIMITY,new SensorEventListener() {
           @Override
           public void onSensorChanged(SensorEvent event) {
               tvProximity.setText(Arrays.toString(event.values));
           }

           @Override
           public void onAccuracyChanged(Sensor sensor, int accuracy) {

           }
       }, MainActivity.this)){
           String error = "Device Doesnt Support Proximity Sensor";
           tvHumidity.setText(error);
       }

       if(!setSensor(Sensor.TYPE_STEP_DETECTOR,new SensorEventListener() {
           @Override
           public void onSensorChanged(SensorEvent event) {
               Log.wtf(TAG, "Step Detector :"+Arrays.toString(event.values));
               tvStepDetector.setText(Arrays.toString(event.values));
           }

           @Override
           public void onAccuracyChanged(Sensor sensor, int accuracy) {

           }
       }, MainActivity.this)){
           String error = "Device Doesnt Support step detector Sensor";
           tvStepDetector.setText(error);
       }
       if(!setSensor(Sensor.TYPE_ORIENTATION,new SensorEventListener() {
           @Override
           public void onSensorChanged(SensorEvent event) {
               tvOrientation.setText(Arrays.toString(event.values));
           }

           @Override
           public void onAccuracyChanged(Sensor sensor, int accuracy) {

           }
       }, MainActivity.this)){
           String error = "Device Doesn't Support Orientation Sensor";
           tvOrientation.setText(error);
       }
   }

    private void initializeXmlVariables() {
        tvAccelerometer = findViewById(R.id.tv_accelerometer);
        tvGyrometer = findViewById(R.id.tv_gyrometer);
        tvMagnetoMeter = findViewById(R.id.tv_magnetometer);
        tvLight = findViewById(R.id.tv_light);
        tvTemperature = findViewById(R.id.tv_temperature);
        tvPressure = findViewById(R.id.tv_pressure);
        tvHumidity = findViewById(R.id.tv_humidity);
        tvProximity = findViewById(R.id.tv_proximity);
        tvStepDetector = findViewById(R.id.tv_pedometer);
        tvOrientation = findViewById(R.id.tv_orientation);
    }
}
