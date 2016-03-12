package javis.wearsyncservice;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.example.PhoneWatchClass;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.Random;

/**
 * Created by Jeffrey Liu on 12/2/15.
 * This service will keep listening to all the message coming from the watch
 */
public class PhoneWearableListenerService extends WearableListenerService {

    String[] all_states = {"AL", "AR", "AZ", "CA", "CO",
            "CT", "DE","DC", "FL", "GA", "HI", "ID", "IL",
            "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA",
            "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH",
            "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR",
            "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT",
            "VA", "WA", "WV", "WI","WY"};

     //getResources().getStringArray(R.array.states);

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        if (messageEvent.getPath().equalsIgnoreCase(PhoneWatchClass.WATCH_TO_PHONE_MESSAGE_PATH)) {
            String receivedText = new String(messageEvent.getData());
            System.out.println("BAZOOKA Received message from watch "+ receivedText);
            go_to_screen(receivedText);
           // broadcastIntent(receivedText);
        } else {
            super.onMessageReceived(messageEvent);
        }
    }

    public void go_to_screen(String instruction)
    /**
     * Moves the phone activity to the one specified by instruction.
     */
    {
        System.out.println("BAZOOKA instruction in listener = "+instruction);
       /* if (instruction.equals("Ed Labov") || instruction.equals("Hannah Whorf") ||
                instruction.equals("Mark Hagen") || instruction.equals("Laura Lakoff")
                || instruction.equals("Ronan Nash")) {*/
        if (instruction.equals("GO/Congressional")==false) {
/*
            SunlightTask task = new SunlightTask();
            task.execute(getApplicationContext(),SunlightInstructions.CUSTOM_ZIP,location)*/
            SunlightDetailTask task1 = new SunlightDetailTask();
            String curr_bioguide_id = new String();
            int last_pos=0;
            for (int i=0;i<MainActivity.name.length;i++)
            {
                if (MainActivity.name[i].equals(instruction))
                {
                    curr_bioguide_id = MainActivity.bioguide_id[i];
                    last_pos = i;
                    break;
                }
            }
            task1.execute(this, getString(R.string.sunlight_key), null, curr_bioguide_id,
                    null, null, null, last_pos);

            /*Intent i = new Intent(this, DetailedActivity.class);
            //Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("KEY", instruction);
            //i.putExtra("go_to_detailed", true);
            startActivity(i);
            //The new re routing path to get to the detailed screen after tapping a
            //a candidate in a watch is Listener --> MainActivty -->new function with
            //api call to detailed screen.*/
        }
        else if (instruction.equals("GO/Congressional"))
        {
            /*Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //Need this line to start an activity/intent in a non-activity class.
            i.putExtra("Toast", "True");
            i.putExtra("Zipcode", "False");
            //Tells the phone that this is not a mnual location update known to the user
            startActivity(i);*/
            Random random = new Random();
            int random_index = random.nextInt(all_states.length -1 - 0 + 1) + 0;
            String random_state = all_states[random_index];

            RandomZipcodeTask task = new RandomZipcodeTask();
            System.out.println("BAZOOKA random_state = " + random_state);
            task.execute(this, getString(R.string.browser_key_1), getString(R.string.sunlight_key)
                    ,random_state, random_index);
        }
    }

    // broadcast a custom intent.
    public void broadcastIntent(String text) {
        Intent intent = new Intent();
        intent.setAction(Constant.MY_INTENT_FILTER);
        intent.putExtra(Constant.PHONE_TO_WATCH_TEXT, text);
        sendBroadcast(intent);
    }
}