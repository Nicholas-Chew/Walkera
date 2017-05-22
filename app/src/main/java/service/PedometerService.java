package service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


/**
 * Created by nicho on 3/18/2017.
 */

public class PedometerService extends Service{

    private static final String preferenceName = "Walkera";
    private SensorManager sensorManager;
    SensorEventListener listen;
    Context ctx;
    public static long stepsCount = 0;
    boolean activityRunning;

    public PedometerService(Context appContext)
    {
        super();
        Log.i("Pedometer", "Pedometer service constructor");
    }

    public PedometerService()
    {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        ctx = this;
        startPedometer();
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.i("Pedometer", "Pedometer service Destryoed!");

        saveCountState();
        Intent broadcastIntent = new Intent("com.ntu.powerranger.walkera.ActivityRecognition.RestartSensor");
        sendBroadcast(broadcastIntent);
        stopPedometer();
    }

    public void startPedometer()
    {
        activityRunning = true;
        stepsCount = loadCountState();
        sensorManager = (SensorManager) getApplicationContext()
                .getSystemService(SENSOR_SERVICE);
        Sensor accel = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        listen = new pedometerListen();
        sensorManager.registerListener(listen, accel, SensorManager.SENSOR_DELAY_NORMAL);
    }



    public void stopPedometer()
    {
        activityRunning = false;
        sensorManager.unregisterListener(listen);
    }


    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }


    private void saveCountState()
    {
        long prevCount = loadCountState();
        try
        {
            SharedPreferences prefs= getSharedPreferences("com.ntu.powerranger.walkera.ServiceRunning", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("StepsCount", String.valueOf(stepsCount));
            editor.apply();
            //Long.i("MoveMore", "Saving readings to preferences");
        } catch (NullPointerException e)
        {
            Log.e("Add error", "error saving: are you testing?" +e.getMessage());
        }
    }

    private long loadCountState()
    {
        try
        {
            SharedPreferences prefs= getSharedPreferences(preferenceName, MODE_PRIVATE);
            return Long.parseLong(prefs.getString("StepsCount", "0"));//"No name defined" is the default value.
            //Long.i("MoveMore", "Saving readings to preferences");
        }
        catch (NullPointerException e)
        {
            Log.e("Load error", "error saving: are you testing?" +e.getMessage());
        }

        return  0;
    }

    public class pedometerListen implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (activityRunning) {
                stepsCount = Math.round(event.values[0]);

                Intent intent = new Intent("com.ntu.powerranger.walkera.pedometerService.update");
                // You can also include some extra data.
                intent.putExtra("stepscount",String.valueOf(stepsCount));
                LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent);
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }


}
