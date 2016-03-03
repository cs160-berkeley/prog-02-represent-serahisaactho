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
import android.widget.Toast;

public class Data2012 extends WearableActivity implements SensorEventListener {

    private final float BASE_VALUE = -5000;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    float last_x;
    float last_y;
    float last_z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_2012);

        //SENSOR STUFF
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        last_x = BASE_VALUE;
        last_y = BASE_VALUE;
        last_z = BASE_VALUE;
        //SENSOR STUFF

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
        if ((event.values[0]!=last_x || event.values[1]!=last_y || event.values[2]!=last_z)
                && last_x==BASE_VALUE && last_y==BASE_VALUE && last_z==BASE_VALUE)
        {
            last_x = event.values[0];
            last_y = event.values[1];
            last_z = event.values[2];
            //This clause is put in to avoid the very first acceleration change detection
        }
        else if (event.values[0]!=last_x || event.values[1]!=last_y || event.values[2]!=last_z) {
            System.out.println("BAZOOKA Detected a shake " + event.values[0] + ", " + event.values[1] + ", " +
                    event.values[2]);
            last_x = event.values[0];
            last_y = event.values[1];
            last_z = event.values[2];
            Toast watch_t = Toast.makeText(getApplicationContext(),"Random Location: XYZ", Toast.LENGTH_SHORT);
            watch_t.show();
            fireMessage("GO/Congressional"); //The message tells the phone to shift to congressional view

            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);

        }
        // Do something with this sensor value.
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mWifiScanReceiver, new IntentFilter(Constant.MY_INTENT_FILTER));
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver();
        mSensorManager.unregisterListener(this);
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
            if (intent.getAction().equals(Constant.MY_INTENT_FILTER)) {
                //mTextView.setText(intent.getStringExtra(Constant.PHONE_TO_WATCH_TEXT));
                System.out.println("BAZOOKA Received the broadcasted intent in watch");
            }
        }
    };

    private void fireMessage(String text) {
        Intent msgIntent = new Intent(this, SendPhoneMessageIntentService.class);
        msgIntent.putExtra(SendPhoneMessageIntentService.INPUT_EXTRA, text);
        startService(msgIntent);
    }
}

