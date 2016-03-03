package javis.wearsyncservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;

public class ChoiceActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_layout); //THIS IS THE FIRST WATCH ACTIVITY
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mWifiScanReceiver, new IntentFilter(Constant.MY_INTENT_FILTER));
    }

    private BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            System.out.println("BAZOOKA the broadacst has been received.");
            if (intent.getAction().equals(Constant.MY_INTENT_FILTER)) {
                System.out.println("BAZOOKA you are in the right place");
            }
        }
    };

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
        /*Intent intent = new Intent(this, Reps2016.class);
        //intent.putExtra("KEY", last_clicked); --> Don't need to pass in anything extra.
        //passing things along to the next screen
        startActivity(intent);*/
    }


}
