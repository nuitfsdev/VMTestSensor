package com.example.vmtestsensor;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_PRECISE_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.hardware.Sensor.TYPE_GYROSCOPE;
import static android.hardware.Sensor.TYPE_ORIENTATION;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Debug;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Utils {
    // system properties
    private static Property[] known_props = {new Property("init.svc.qemud", null),
            new Property("init.svc.qemu-props", null), new Property("qemu.hw.mainkeys", null),
            new Property("qemu.sf.fake_camera", null), new Property("qemu.sf.lcd_density", null),
            new Property("ro.bootloader", "unknown"), new Property("ro.bootmode", "unknown"),
            new Property("ro.hardware", "goldfish"), new Property("ro.kernel.android.qemud", null),
            new Property("ro.kernel.qemu.gles", null), new Property("ro.kernel.qemu", "1"),
            new Property("ro.product.device", "generic"), new Property("ro.product.model", "sdk"),
            new Property("ro.product.name", "sdk"),
            new Property("ro.serialno", null)};
    private static int MIN_PROPERTIES_THRESHOLD = 0x5;
    private static String qemu_property;



    //sensor
    private static float oldsensor;
    private static float sensor_count;
    public static boolean detectsensor(final TextView textView, final Context context){
        try {
            final SensorManager smanger = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            final Sensor sensor = smanger.getDefaultSensor(TYPE_GYROSCOPE);
            SensorEventListener sensorEvent = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    sensor_count++;
                    if((sensor_count == 4) && (event.values[0] == oldsensor)  ){
                        textView.setText("The sensor has value: \n"+ Float.toString(event.values[0])+"\n"+Float.toString(event.values[1])+"\n"+Float.toString(event.values[2]));
                    }
                    else {
                        oldsensor = event.values[0];
                        textView.setText("The sensor has value: \n"+ Float.toString(event.values[0])+"\n"+Float.toString(event.values[1])+"\n"+Float.toString(event.values[2]) );
                        return;
                    }
                    //oldsensor = event.values[0];
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }
            };
            smanger.registerListener(sensorEvent, sensor, SensorManager.SENSOR_DELAY_UI);
            return false;
        }catch (Exception e){
            textView.setText("Can't detect sensor!");
            return true;
        }
    }

}