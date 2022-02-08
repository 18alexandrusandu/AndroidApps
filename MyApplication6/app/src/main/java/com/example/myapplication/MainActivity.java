package com.example.myapplication;

import static java.lang.Math.abs;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompatSideChannelService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager manager;
    private Sensor accelerometer;
    private boolean switchero=true;
    private CameraManager cameraman;
   private boolean OnCamera=true;
    private Vibrator vibrator;
    private float x_anterior=0;
    private MediaPlayer player;
    private int number_of_shakes=0;


  static final String Tag="MainActivity";
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(Tag,"on setting manager listener");

        cameraman=(CameraManager)getSystemService(CAMERA_SERVICE);

      vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);



        manager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer= manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Log.d(Tag,"on registering listener");
        manager.registerListener(MainActivity.this,accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        player=MediaPlayer.create(MainActivity.this,R.raw.music);





        Button buttStart= (Button)findViewById(R.id.button);
        buttStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchero=!switchero;
                if(switchero)
                {
                    buttStart.setText("AppOn");

                    player.start();




                }else {
                    buttStart.setText("AppOff");
                    player.pause();
                }

            }

        });


    }






    @SuppressLint("NewApi")
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Log.d(Tag, "se verifica");
        final VibrationEffect vibrationEffect1;
        TextView txtv = (TextView) findViewById(R.id.textView);
        vibrationEffect1 = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE);


        if (switchero) {

            if (Math.abs(sensorEvent.values[0] - x_anterior) > 2) {
                number_of_shakes++;

                Log.d(Tag, "X value:" + x_anterior + " new X value:" + sensorEvent.values[0]);
            }
            x_anterior = sensorEvent.values[0];
            if (number_of_shakes == 2) {
                number_of_shakes=0;
                OnCamera = !OnCamera;
                Log.d(Tag, "X value:" + x_anterior + " new X value:" + sensorEvent.values[0]);

                vibrator.vibrate(vibrationEffect1);


            }

            if (OnCamera)
                txtv.setText("Flash On");
            else
                txtv.setText("Flash Off");


            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {//WILL WORK WITH FLASH

                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) { //turn flash on

                    try {
                        cameraman.setTorchMode("0", OnCamera);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }

                } else {

                    Log.d(Tag, "no flash");
                }

            } else {
                Log.d(Tag, "no camera");
            }


        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}