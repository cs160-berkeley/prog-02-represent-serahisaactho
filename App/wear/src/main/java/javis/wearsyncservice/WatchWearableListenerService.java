package javis.wearsyncservice;

import android.content.Intent;

import com.example.PhoneWatchClass;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by Jeffrey Liu on 12/2/15.
 * This service will keep listening to all the message coming from the phone
 */
public class WatchWearableListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equalsIgnoreCase(PhoneWatchClass.PHONE_TO_WATCH_MESSAGE_PATH)) {
            String receivedText = new String(messageEvent.getData());
            execute_msg(receivedText);
            broadcastIntent(receivedText);
        } else {
            super.onMessageReceived(messageEvent);
        }
    }

    public void execute_msg(String instruction)
    {
        if (instruction.startsWith("GO/Choice;"))
        {
            String location = instruction.substring(instruction.indexOf(";")+1);
            System.out.println("BAZOOKA location = "+location);
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("Zipcode", location);
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