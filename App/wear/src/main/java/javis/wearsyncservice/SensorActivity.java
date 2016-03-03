package javis.wearsyncservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;

public class SensorActivity extends WearableActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    float last_x;
    float last_y;
    float last_z;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data2012);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        last_x = 0;
        last_y = 0;
        last_z = 0;
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        //float lux = event.values[0];
        if (event.values[0]!=last_x || event.values[1]!=last_y || event.values[2]!=last_z) {
//            System.out.println("BAZOOKA Detected a shake " + event.values[0] + ", " + event.values[1] + ", " +
//                    event.values[2]);
            last_x = event.values[0];
            last_y = event.values[1];
            last_z = event.values[2];
            fireMessage("GO/Congressional"); //The message tells the phone to shift to congressional view
        }
        // Do something with this sensor value.
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        registerReceiver(mWifiScanReceiver, new IntentFilter(Constant.MY_INTENT_FILTER));
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        unregisterReceiver();
    }

    private void fireMessage(String text) {
        Intent msgIntent = new Intent(this, SendPhoneMessageIntentService.class);
        msgIntent.putExtra(SendPhoneMessageIntentService.INPUT_EXTRA, text);
        //System.out.println("BAZOOKA sending message");
        startService(msgIntent);
    }

    private void unregisterReceiver() {
        try {
            if (mWifiScanReceiver != null) {
                unregisterReceiver(mWifiScanReceiver);
            }
        } catch (IllegalArgumentException e) {
            mWifiScanReceiver = null;
        }
    }


    private BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            //System.out.println("BAZOOKA Message received in wrong activity");
            if (intent.getAction().equals(Constant.MY_INTENT_FILTER)) {
                //Do whatevs needs to be done
                System.out.println("BAZOOKA Oh no! What am I doing here?");
            }
        }
    };
}
