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
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.wearable.Wearable;

public class PickerActivity extends WearableActivity implements SensorEventListener {


    private final float BASE_VALUE = -5000;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    float last_x;
    float last_y;
    float last_z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_adapter);

        //SENSOR STUFF
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        last_x = BASE_VALUE;
        last_y = BASE_VALUE;
        last_z = BASE_VALUE;
        //SENSOR STUFF

        final GridViewPager pager = (GridViewPager) findViewById(R.id.arrival_pager);
        DotsPageIndicator dots = (DotsPageIndicator) findViewById(R.id.indicator);
        //try to lift up the dots positioning
        dots.setPager(pager);
        pager.setAdapter(new PickerAdapter(this, getFragmentManager()));
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

    public void update_phone(View w)
            /*
            Called on click of a page in the 2d picker.
             */
    {
        //Make it go to the detailed view on the phone.
        TextView name = (TextView)w.findViewById(R.id.Title);
        String formatted_name = new String();
        String name_txt = name.getText().toString();
        switch(name_txt)
                //The purpose of this switch block is to ensure that the phone
                //receives a message that it can interpret.
        {
            case "Senator Laura Lakoff": formatted_name = "Laura Lakoff"; break;
            case "Senator Ronan Nash": formatted_name = "Ronan Nash"; break;
            case "Senator Ed Labov": formatted_name = "Ed Labov"; break;
            case "Representative Hannah Whorf": formatted_name = "Hannah Whorf"; break;
            case "Senator Mark Hagen": formatted_name = "Mark Hagen"; break;
        }
        System.out.println("BAZOOKA Will do the needful in the phone");
        fireMessage(formatted_name);
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

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        //updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        //updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        //updateDisplay();
        super.onExitAmbient();
    }

   /* private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            mTextView.setTextColor(getResources().getColor(android.R.color.white));
            mClockView.setVisibility(View.VISIBLE);

            mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
        } else {
            mContainerView.setBackground(null);
            mTextView.setTextColor(getResources().getColor(android.R.color.black));
            mClockView.setVisibility(View.GONE);
        }
    }*/

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

