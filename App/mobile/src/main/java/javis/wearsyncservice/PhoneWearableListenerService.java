package javis.wearsyncservice;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.example.PhoneWatchClass;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by Jeffrey Liu on 12/2/15.
 * This service will keep listening to all the message coming from the watch
 */
public class PhoneWearableListenerService extends WearableListenerService {

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
        if (instruction.equals("Ed Labov") || instruction.equals("Hannah Whorf") ||
                instruction.equals("Mark Hagen") || instruction.equals("Laura Lakoff")
                || instruction.equals("Ronan Nash")) {
            System.out.println("BAZOOKA I am in the right place");
            Intent i = new Intent(this, DetailedActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //Need this line to start an activity/intent in a non-activity class.
            i.putExtra("KEY", instruction);
            startActivity(i);
        }
        else if (instruction.equals("GO/Congressional"))
        {
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //Need this line to start an activity/intent in a non-activity class.
            i.putExtra("Toast", "True");
            i.putExtra("Zipcode", "False");
            //Tells the phone that this is not a mnual location update known to the user
            startActivity(i);
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