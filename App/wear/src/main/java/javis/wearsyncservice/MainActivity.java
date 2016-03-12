package javis.wearsyncservice;

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
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends WearableActivity implements SensorEventListener {

    /*private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private BoxInsetLayout mContainerView;
    private TextView mTextView;
    private TextView mClockView;*/
    private final float BASE_VALUE = -5000;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    float last_x;
    float last_y;
    float last_z;
    String[] name_title;
    String[] party;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_layout);
        setAmbientEnabled();

        //ON MANUAL UPDATE TOAST STUFF
        Intent intent = getIntent();
        String location = intent.getStringExtra("Zipcode");
        if (location!=null && location.equals("False")==false)
        {
            Toast t2 = Toast.makeText(getApplicationContext(), "Location: "+location, Toast.LENGTH_LONG);
            TextView v = (TextView)t2.getView().findViewById(android.R.id.message);
            if (v!=null)
            {
                v.setGravity(Gravity.CENTER);
                //to set the text alignment to middle for the toast text
            }
            t2.show();
        }
        //ON MANUAL UPDATE TOAST STUFF

        //EXTRACTING REQUIRED INFO
        name_title = intent.getStringArrayExtra(SunlightWearInstructions.NAMES_TITLES);
        party = intent.getStringArrayExtra(SunlightWearInstructions.PARTIES);
        //EXTRACTING REQUIRED INFO

        //SENSOR STUFF
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        last_x = BASE_VALUE;
        last_y = BASE_VALUE;
        last_z = BASE_VALUE;
        //SENSOR STUFF

        //mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        //mTextView = (TextView) findViewById(R.id.text);
        //mClockView = (TextView) findViewById(R.id.clock);
        //Button mButton = (Button) findViewById(R.id.button);
        /*mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fireMessage(mTextView.getText().toString());
            }
        });*/
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        if ((event.values[0]!=last_x || event.values[1]!=last_y || event.values[2]!=last_z)
                && last_x==BASE_VALUE && last_y==BASE_VALUE && last_z==BASE_VALUE)
        {
            last_x = event.values[0];
            last_y = event.values[1];
            last_z = event.values[2];
            //This clause is put in to avoid the very first acceleration change detection
        }
            //else if (event.values[0]!=last_x || event.values[1]!=last_y || event.values[2]!=last_z) {
        else if (Math.abs(event.values[0]-last_x)>=10
                || Math.abs(event.values[1]-last_y)>=10
                || Math.abs(event.values[2]-last_z)>=10) {
            System.out.println("BAZOOKA Detected a shake " + event.values[0] + ", " + event.values[1] + ", " +
                    event.values[2]);
            last_x = event.values[0];
            last_y = event.values[1];
            last_z = event.values[2];
            //Toast watch_t = Toast.makeText(getApplicationContext(),"Random Location: XYZ", Toast.LENGTH_SHORT);
            //watch_t.show();
            fireMessage("GO/Congressional"); //The message tells the phone to shift to congressional view
        }
    }

    public void go_to_2012(View w)
    /**
     * Called when the user taps '2012 Voting Data'
     */
    {
        Intent intent = new Intent(this, Data2012.class);
        //intent.putExtra("KEY", last_clicked); --> Don't need to pass in anything extra.
        //passing things along to the next screen
        startActivity(intent);
    }

    public void go_to_2016(View w)
    /**
     * Called when the user taps '2016 Congressional Representatives'
     */
    {
        Intent intent = new Intent(this, PickerActivity.class);
        //intent.putExtra("KEY", last_clicked); --> Don't need to pass in anything extra.
        //passing things along to the next screen
        intent.putExtra(SunlightWearInstructions.NAMES_TITLES, name_title);
        intent.putExtra(SunlightWearInstructions.PARTIES, party);
        startActivity(intent);
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