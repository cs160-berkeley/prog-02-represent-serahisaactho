package javis.wearsyncservice;

import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends FragmentActivity implements CustomDialog.CustomDialogListener{
// extends AppCompatActivity {

    private TextView textView;
    private EditText editText;
    private String last_clicked;
    private String api_key;
    int last_position;

    int num_people;
    static ArrayList<String> img_urls = new ArrayList<String>();
    static String[] bioguide_id;
    static String[] name; //holds all the candidate names in order
    static String[] party; //holds all the parties in order
    static String[] title; //holds either Senator or Representative // take from title tag in json object
    static String[] email;// oc_email tag
    static String[] website;// website tag
    static String[] twitter_id; //twitter_id tag
    static String[] term_end; //term_end tag

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "xPBH74d17OPYBRSyOiAbo8HlN";//"plnyQCLPdJjOhAdaEkfQH4a73";
    private static final String TWITTER_SECRET = "mg6OvvLt0xsmCmn67FqutOTrT4rsFAcIflaNGWEYStLvLj89HI";//"MbwjceC8iA8vOSNaRYq2GDGWD6r9kmvlpTcD2j1qmWHm3MM8ji";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2); //This is the layout with the listView
        api_key = getString(R.string.sunlight_key);

        //ON SHAKE TOAST STUFF
        Intent intent = getIntent();
        String need_toast = intent.getStringExtra("Toast");
        /*Toast t = Toast.makeText(getApplicationContext(), "Random Location out: XYZ", Toast.LENGTH_SHORT);
        t.show();*/
        if (need_toast!=null && need_toast.equals("True"))
        {
            Toast t1 = Toast.makeText(getApplicationContext(), "Random Location: XYZ", Toast.LENGTH_SHORT);
            t1.show();
        }
        //ON SHAKE TOAST STUFF



        //Location Set TOAST STUFF
        String location = intent.getStringExtra("Zipcode");
        if (location!=null && location.equals("False")==false)
        {
            Toast t2 = Toast.makeText(getApplicationContext(), "Location: "+location, Toast.LENGTH_LONG);
            t2.show();
            fireMessage("GO/Choice;"+location); //Tells the watch to display the updated choice screen
        }
        //Location Set Toast Stuff

        final ListView listview = (ListView) findViewById(R.id.listView);
        /*String[] values = new String[] { "Laura Lakoff","Ronan Nash","Ed Labov","Hannah Whorf","Mark Hagen"};

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }*/

        String object = intent.getStringExtra(SunlightInstructions.CONGRESS_JSON);
        try {
            JSONObject jsonObj = new JSONObject(object);
            JSONArray arr = jsonObj.getJSONArray("results"); //gets the array following results tag
            num_people = arr.length(); //finds the number of representatives
            declare_arrays();//declares the size of all the arrays so that they are ready to use.
            parse_array(arr);


            //RETURN FROM WATCH STUFF
            /*boolean go_to_detailed  = intent.getBooleanExtra("go_to_detailed", false);
            if (go_to_detailed)
            {
                String name_key = intent.getStringExtra("KEY");
                if (name==null)
                {
                    System.out.println("BAZOOKA name is null");
                }
                for (int i=0;i<name.length;i++)
                {
                    if (name[i].equals(name_key))
                    {
                        last_position = i;
                        last_clicked = name_key;
                    }
                }
                System.out.println("BAZOOKA Inside the den");
                move_to_detail(new View(getApplicationContext()));
            }*/
            //RETURN FROM WATCH STUFF

            //TWITTER IMAGE URLS

            //System.out.println("BAZOOKA img_url " + get_img_url("RepBarbaraLee"));



            //TWITTER IMAGE URLS

            //PASSING INFORMATION TO WATCH
            String input = ";"+GeoCodeTask.county_name+"!";
            for (int i=0;i<name.length;i++)
            {
                input = input + title[i] + " " + name[i] +":" + party[i] + ";";
                //NAME(w/ title):PARTY;NAME2(w/ title):PARTY2;....
            }
            fireMessage(input);
            //PASSING INFORMATION TO WATCH

            final ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < num_people; ++i) {
                list.add(name[i]); }


            final ListViewAdapter adapter = new ListViewAdapter(this,
                    android.R.layout.simple_list_item_1, list);
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    final String item = (String) parent.getItemAtPosition(position);
                    //Make it show the dialogue
                    last_clicked = item;
                    last_position = position;
                    CustomDialog d =new CustomDialog();
                    Bundle b = new Bundle();
                    b.putString("clicked_name", item);
                    b.putInt("position", position);
                    b.putStringArray("name", name);
                    b.putStringArray("bioguide_id", bioguide_id);
                    b.putStringArray("party", party);
                    b.putStringArray("title", title);
                    b.putStringArray("email",email);
                    b.putStringArray("website", website);
                    b.putStringArray("twitter_id", twitter_id);
                    b.putStringArray("term_end", term_end);
                    //Sending all the parsed information
                    d.setArguments(b);
                    d.show(getFragmentManager(), item);
                    //fireMessage(item);
                /*String input = ";";
                for (int i=0;i<name.length;i++)
                {
                   input = input + title[i] + " " + name[i] +":" + party[i] + ";";
                    //NAME(w/ title):PARTY;NAME2(w/ title):PARTY2;....
                }
                fireMessage(input);*/
                }

            });

        }
        catch (Exception e)
        {
            Log.e("ERROR", e.getMessage(), e);
        }



        /*//PASSING INFORMATION TO WATCH
        String input = ";";
        for (int i=0;i<name.length;i++)
        {
            input = input + title[i] + " " + name[i] +":" + party[i] + ";";
            //NAME(w/ title):PARTY;NAME2(w/ title):PARTY2;....
        }
        fireMessage(input);
        //PASSING INFORMATION TO WATCH

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < num_people; ++i) {
            list.add(name[i]); }


        final ListViewAdapter adapter = new ListViewAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                //Make it show the dialogue
                last_clicked = item;
                last_position = position;
                CustomDialog d =new CustomDialog();
                Bundle b = new Bundle();
                b.putString("clicked_name", item);
                b.putInt("position", position);
                b.putStringArray("name", name);
                b.putStringArray("bioguide_id", bioguide_id);
                b.putStringArray("party", party);
                b.putStringArray("title", title);
                b.putStringArray("email",email);
                b.putStringArray("website", website);
                b.putStringArray("twitter_id", twitter_id);
                b.putStringArray("term_end", term_end);
                //Sending all the parsed information
                d.setArguments(b);
                d.show(getFragmentManager(), item);
                //fireMessage(item);
                *//*String input = ";";
                for (int i=0;i<name.length;i++)
                {
                   input = input + title[i] + " " + name[i] +":" + party[i] + ";";
                    //NAME(w/ title):PARTY;NAME2(w/ title):PARTY2;....
                }
                fireMessage(input);*//*
            }

        });*/
    }

    public void move_to_detail(View w)
    /**
     * Called onClick of the MORE INFO button
     */
    {
        /*Intent i = new Intent(this, DetailedActivity.class);
        i.putExtra("KEY", last_clicked);
        startActivity(i);*/
        SunlightDetailTask task = new SunlightDetailTask();
        /*task.execute(getApplicationContext(), api_key, name, bioguide_id,
                party, title, term_end, last_position);*/
        task.execute(this, api_key, name, bioguide_id[last_position],
                party, title, term_end, last_position);
    }

    public String get_img_url(final String curr_twitter_id)
    {
        /*TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        String output = "";
        TwitterCore.getInstance().logInGuest(new com.twitter.sdk.android.core.Callback<AppSession>() {
            @Override
            public void success(Result appSessionResult) {
                AppSession guestAppSession = (AppSession) appSessionResult.data;
                //TwitterSession guestAppSession = (TwitterSession) appSessionResult.data;
                    new MyTwitterApiClient(guestAppSession).getUsersService().show(null, curr_twitter_id, true, new com.twitter.sdk.android.core.Callback<User>() {
                        @Override
                        public void success(Result<User> result) {
                            output =  (result.data.profileImageUrlHttps.replace("_normal", "_mini"));
                        }

                        @Override
                        public void failure(TwitterException exception) {

                        }
                    });
                }

            public void failure(TwitterException exception) {
                //Do something on failure
                System.out.println("BAZOOKA FAILURE in guest auth");
            }

        });
        return output;*/
        return null;
    }


    private void parse_array(JSONArray arr)
            /*
            Extracts all the required information from the JSON array and
            stores in the arrays IN ORDER.
             */
    {
        try {
            for (int i = 0; i < arr.length(); i++) {
                JSONObject jsonobject = arr.getJSONObject(i);
                bioguide_id[i] = jsonobject.getString("bioguide_id");
                name[i] = jsonobject.getString("first_name") + " " + jsonobject.getString("last_name");
                if (jsonobject.getString("party").equals("D")){
                    party[i] = "DEMOCRAT";
                }
                else if (jsonobject.getString("party").equals("R")) {
                    party[i] = "REPUBLICAN";
                }
                else{
                    party[i] = "UNIDENTIFIED PARTY";
                }
                if (jsonobject.getString("title").equals("Sen")) {
                    title[i] = "Senator";
                }
                else if (jsonobject.getString("title").equals("Rep")) {
                    title[i] = "Representative";
                }
                else {
                    title[i] = "Unidentified Title";
                }
                email[i] = jsonobject.getString("oc_email");
                website[i] = jsonobject.getString("website");
                twitter_id[i] = jsonobject.getString("twitter_id");
                term_end[i] = jsonobject.getString("term_end");

            }
        }
        catch (Exception e)
        {
            Log.e("ERROR", e.getMessage(), e);
        }
    }

    private void declare_arrays()
    /**
     * Declares all the arrays with the coorect size so that they can used.
     */
    {
        bioguide_id = new String[num_people];
        name = new String[num_people];
        party = new String[num_people];
        title = new String[num_people];
        email = new String[num_people];
        website = new String[num_people];
        twitter_id = new String[num_people];
        term_end = new String[num_people];
        //img_urls = new String[num_people];
    }

    private void display_arr(String[] array)
    /**
     * Displays the contents of the guven array for debugging purposes.
     */
    {
        for (int i=0;i<num_people;i++)
        {
            System.out.println("BAZOOKA " + array[i]);
        }

    }

    @Override
    public void onDialogPositiveClick(DialogFragment d)
    {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment d)
    {

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mWifiScanReceiver, new IntentFilter(Constant.MY_INTENT_FILTER));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver();
    }

    private void fireMessage(String text) {
        Intent msgIntent = new Intent(this, SendWatchMessageIntentService.class);
        msgIntent.putExtra(SendWatchMessageIntentService.INPUT_EXTRA, text);
        startService(msgIntent);
    }

   /* private void fireMessagewParams(String[] text) {
        Intent msgIntent = new Intent(this, SendWatchMessageIntentService.class);
        msgIntent.putExtra(SendWatchMessageIntentService.INPUT_EXTRA, text);
        startService(msgIntent);
    }*/

    private BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            System.out.println("BAZOOKA Received something in the receiver");
            go_to_screen(intent.getStringExtra(Constant.PHONE_TO_WATCH_TEXT));
            if (intent.getAction().equals(Constant.MY_INTENT_FILTER)) {
                //textView.setText(intent.getStringExtra(Constant.PHONE_TO_WATCH_TEXT));
                //Do the needfull
            }
        }
    };

    public void go_to_screen(String instruction)
    /**
     * Moves the phone activity to the one specified by instruction.
     */
    {
        System.out.println("BAZOOKA instruction in main_activity = "+instruction);
       /* if (instruction=="Ed Labov" || instruction=="Hannah Whorf" ||
                instruction=="Mark Hagen" || instruction=="Laura Lakoff"
                || instruction=="Ronan Nash")*/ {
            System.out.println("BAZOOKA I am in the right place");
            Intent i = new Intent(this, DetailedActivity.class);
            i.putExtra("KEY", instruction);
            startActivity(i);
        }
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
}