package javis.wearsyncservice;

import android.content.Intent;

import com.example.PhoneWatchClass;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeffrey Liu on 12/2/15.
 * This service will keep listening to all the message coming from the phone
 */
public class WatchWearableListenerService extends WearableListenerService {

    public String[] name; //inclusive of the title in this case, unlike mobile!
    public String[] party;
    public String location; //sent earlier from mobile but stored until
    //all mobile information is received and the intent to the main activity is called.
    public static String county_name;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equalsIgnoreCase(PhoneWatchClass.PHONE_TO_WATCH_MESSAGE_PATH)) {
            String receivedText = new String(messageEvent.getData());
            if (receivedText.startsWith(";"))
            {
                create_arrays(receivedText);
            }
            execute_msg(receivedText);
            broadcastIntent(receivedText);
        } else {
            super.onMessageReceived(messageEvent);
        }
    }

    private void create_arrays(String code)
    /**
     * Decodes the string from its special format and stores the values
     * in the string arrays for the wear component to use.
     * String Manipulation FTW.
     */
    {
        ArrayList<String> temp_name = new ArrayList<String>();
        ArrayList<String> temp_party = new ArrayList<String>();
        //Using an arraylist first because we do not know the number of elements beforehand.
        county_name = code.substring(1,code.indexOf('!'));
        String curr_word = "";
        int index=0;
        char curr_char;
        for (int i=code.indexOf('!')+1;i<code.length();i++) //Start at 1 to avoid the first ;
        {
            curr_char = code.charAt(i);
            if (curr_char==';')
            {
                temp_party.add(curr_word);
                curr_word = "";
                index++;
            }
            else if (curr_char==':')
            {
                temp_name.add(curr_word);
                curr_word = ""; //resetting the value so it can take the party name correctly.
            }
            else
            {
                curr_word = curr_word + curr_char;
            }
        }
        /*String curr_word = "";
        int index=0;
        char curr_char;
        for (int i=1;i<code.length();i++) //Start at 1 to avoid the first ;
        {
            curr_char = code.charAt(i);
            if (curr_char==';')
            {
                temp_party.add(curr_word);
                curr_word = "";
                index++;
            }
            else if (curr_char==':')
            {
                temp_name.add(curr_word);
                curr_word = ""; //resetting the value so it can take the party name correctly.
            }
            else
            {
                curr_word = curr_word + curr_char;
            }
        }*/
        name = new String[temp_name.size()];
        party = new String[temp_name.size()];
        for(int i=0;i<name.length;i++)
        {
            name[i] = temp_name.get(i);
            party[i] = temp_party.get(i);
        }
        //the name and party string arrays are good to go!
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("Zipcode", location);
        i.putExtra(SunlightWearInstructions.NAMES_TITLES, name);
        i.putExtra(SunlightWearInstructions.PARTIES, party);
        startActivity(i);
    }

    public void execute_msg(String instruction)
    {
        if (instruction.startsWith("GO/Choice;"))
        {
            String last_location = instruction.substring(instruction.indexOf(";")+1);
            location = last_location;
            //System.out.println("BAZOOKA location = "+location);
            /*Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("Zipcode", location);
            startActivity(i);*/
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