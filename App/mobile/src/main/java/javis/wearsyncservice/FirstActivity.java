package javis.wearsyncservice;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

public class FirstActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    private String api_key;
    private GoogleApiClient mGoogleApiClient;
    private String last_latitude = "";
    private String last_longitude = "";
    public static String TAG = "GPSActivity";
    public static int UPDATE_INTERVAL_MS = 10; //CHANGE THIS VALUE
    public static int FASTEST_INTERVAL_MS = 10; //CHANGE THIS VALUE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
//        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_first2);
        api_key = getString(R.string.sunlight_key);



        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Wearable.API)  // used for data layer API
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        Button go = (Button)findViewById(R.id.GoBttn);
        final EditText zipcode = (EditText)findViewById(R.id.Zipcode);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("BAZOOKA Zipcode = " + zipcode.getText());
                if (zipcode==null || zipcode.getText()==null || (zipcode!=null && zipcode.getText().equals("")==true)) //Display a toast if the user enters nothing
                //TODO: FIX THIS
                {
                    Toast t = Toast.makeText(getApplicationContext(),"Please enter a zipcode" +
                            " or use current location.", Toast.LENGTH_LONG);
                    t.show();
                }
                else
                {

                    fly_away(zipcode.getText().toString());
                }
            }
        });
    }

    public void fly_away(String location)
    {
        /*Intent i = new Intent(this, MainActivity.class);
        i.putExtra("Toast", "False");
        i.putExtra("Zipcode", location);
        startActivity(i);*/
        //TODO: Change Toolbar Colour
//        SunlightTask task = new SunlightTask();
//        task.execute(getApplicationContext(), SunlightInstructions.CUSTOM_ZIP, api_key, location);
        ZipToLatTask task = new ZipToLatTask();
        task.execute(getApplicationContext(), getString(R.string.browser_key_1),location, api_key);
    }


    public void go_to_list_current(View w)
            /*
            Called onClick of of use current location
             */
    {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("Toast", "False");
        i.putExtra("Zipcode", "Current Location");
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Build a request for continual location updates
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_INTERVAL_MS);
        try {
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mGoogleApiClient,
                            locationRequest, this)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            if (status.getStatus().isSuccess()) {
                                Log.d(TAG, "Successfully requested");
                            } else {
                                Log.e(TAG, status.getStatusMessage());
                            }
                        }
                    });
        }
        catch(SecurityException e)
        {
            Log.d(TAG, "I think you have a permission issue");
        }
    }

    public void do_current_location_stuff(View w)
        /*
        Called onclick of the use current location button
         */
    {

        if (last_latitude.equals("")==false && last_longitude.equals("")==false)
        {
            //SunlightTask task = new SunlightTask();
            //task.execute(getApplicationContext(), SunlightInstructions.CURRENT_LOC,api_key,last_latitude, last_longitude);
            //task.execute(getApplicationContext(),SunlightInstructions.CUSTOM_ZIP,api_key,"94720");
            GeoCodeTask task = new GeoCodeTask();
            task.execute(getApplicationContext(), getString(R.string.browser_key_1), last_latitude, last_longitude, api_key,"Current Location");
        }
        else
        {
            System.out.println("BAZOOKA the latitude and longitude values were not set.");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        last_latitude = String.valueOf(location.getLatitude());
        last_longitude = String.valueOf(location.getLongitude());
        //System.out.println("BAZOOKA last_latitude = " + last_latitude);
        //System.out.println("BAZOOKA last_longitude = " + last_longitude);
    }


    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("BAZOOKA Connection Suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connResult) {
        System.out.println("BAZOOKA Connection Failed");
    }

}
