package javis.wearsyncservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;


import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import io.fabric.sdk.android.Fabric;

public class DetailedActivity extends AppCompatActivity {


    /*HashMap<String,String> EdLabov;
    HashMap<String,String> LauraLakoff;
    HashMap<String,String> HannahWhorf;
    HashMap<String,String> MarkHagen;
    HashMap<String,String> RonanNash;*/
    int last_position;
    String[] bioguide_id;
    String[] name;
    String[] title;
    String[] party;
    String[] term_end;
    String[] bill_names;
    String[] committee_names;
    String[] bill_intro_dates;
    int num_committees;
    int num_bills;
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "xPBH74d17OPYBRSyOiAbo8HlN";//"plnyQCLPdJjOhAdaEkfQH4a73";
    private static final String TWITTER_SECRET = "mg6OvvLt0xsmCmn67FqutOTrT4rsFAcIflaNGWEYStLvLj89HI";//"MbwjceC8iA8vOSNaRYq2GDGWD6r9kmvlpTcD2j1qmWHm3MM8ji";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        View w = inflater.inflate(R.layout.activity_detailed, null);
        setContentView(w);

        Intent intent = getIntent();
        String clicked_name = intent.getStringExtra("KEY"); //secured the clicked name from the dialog.
        last_position = intent.getIntExtra(SunlightInstructions.POSITION, -500);
        String bill_str = intent.getStringExtra(SunlightInstructions.BILLS);
        String committee_str = intent.getStringExtra(SunlightInstructions.COMMITTEES);
        bioguide_id = intent.getStringArrayExtra(SunlightInstructions.BIOGUIDE_IDS);
        name = intent.getStringArrayExtra(SunlightInstructions.NAMES);
        title = intent.getStringArrayExtra(SunlightInstructions.TITLES);
        party = intent.getStringArrayExtra(SunlightInstructions.PARTIES);
        term_end = intent.getStringArrayExtra(SunlightInstructions.TERM_END);

        if (bioguide_id==null || name==null || party==null)
        {
            name = MainActivity.name;
            term_end = MainActivity.term_end;
            bioguide_id = MainActivity.bioguide_id;
            party = MainActivity.party;
            title = MainActivity.title;
        }

        try {
            JSONObject committees_obj = new JSONObject(committee_str);
            JSONObject bills_obj = new JSONObject(bill_str);
            JSONArray committees_arr = committees_obj.getJSONArray("results");
            JSONArray bills_arr = bills_obj.getJSONArray("results");
            num_committees = 3; //committees_arr.length();
            num_bills = 3; //bills_arr.length();
            declare_arrays();
            parse_committees_array(committees_arr);
            parse_bills_array(bills_arr);
        }
        catch (Exception e)
        {
            Log.e("ERROR", e.getMessage(), e);
        }


        fill_fields(w); //stores the correct information for each field.
    }

    private void parse_committees_array(JSONArray arr)
    {
        try {
            //for (int i = 0; i < arr.length(); i++) {
            for (int i = 0; i < num_committees; i++) {
                JSONObject jsonobject = arr.getJSONObject(i);
                committee_names[i] = jsonobject.getString("name");
            }
        }
        catch (Exception e)
        {
            Log.e("ERROR", e.getMessage(), e);
        }
    }

    private void parse_bills_array(JSONArray arr)
    {
        try {
//            for (int i = 0; i < arr.length(); i++) {
            for (int i = 0; i < num_bills; i++) {
                JSONObject jsonobject = arr.getJSONObject(i);
                bill_names[i] = jsonobject.getString("short_title");
                if (bill_names[i].equals("null"))
                {
                    bill_names[i] = jsonobject.getString("official_title");
                }
                bill_intro_dates[i] = jsonobject.getString("introduced_on");
            }
        }
        catch (Exception e)
        {
            Log.e("ERROR", e.getMessage(), e);
        }
    }

    private void declare_arrays()
    {
        committee_names = new String[num_committees];
        bill_names = new String[num_bills];
        bill_intro_dates = new String[num_bills];
    }

    private void fill_fields(View w)
    {
        TextView title_widget = (TextView)w.findViewById(R.id.Title);
        TextView party_widget = (TextView)w.findViewById(R.id.Party);
        TextView eoft_widget = (TextView)w.findViewById(R.id.EofT);
        TextView committees_widget = (TextView)w.findViewById(R.id.Committees);
        TextView bills_widget = (TextView)w.findViewById(R.id.Bills);
        final ImageView picture = (ImageView)w.findViewById(R.id.overlapImage);
        LinearLayout top = (LinearLayout)w.findViewById(R.id.layoutTop);

        title_widget.setText(title[last_position]+" "+name[last_position]);
        party_widget.setText(party[last_position]);
        eoft_widget.setText("End of Term: " + term_end[last_position]);
        //picture.setImageResource(Integer.parseInt(info.get("picture")))

        //String committee_str = "Currently serving on:-";
        String committee_str = "";
        //String bill_str = "Sponsored Bills(with date of introduction):-";
        String bill_str = "";
        for (int i=0;i<num_committees;i++)
        {
            committee_str = committee_str + "\n \t\t\t\t\t\t" + committee_names[i];
        }
        for (int i=0;i<num_bills;i++)
        {
            bill_str = bill_str + "\n \t\t\t\t\t\t" + bill_names[i] + " (" + bill_intro_dates[i] + ")";
        }
        committees_widget.setText(committee_str);
        bills_widget.setText(bill_str);

        if (party_widget.getText().equals("DEMOCRAT"))
        {
            party_widget.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
            top.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
        }
        else if (party_widget.getText().equals("REPUBLICAN"))
        {
            party_widget.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
            top.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
        }

        //TWITTER PHOTO STUFF
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        TwitterCore.getInstance().logInGuest(new com.twitter.sdk.android.core.Callback<AppSession>() {
            @Override
            public void success(Result appSessionResult) {
                AppSession guestAppSession = (AppSession) appSessionResult.data;
                //TwitterSession guestAppSession = (TwitterSession) appSessionResult.data;

                new MyTwitterApiClient(guestAppSession).getUsersService().show(null, MainActivity.twitter_id[last_position], true, new com.twitter.sdk.android.core.Callback<User>() {
                    @Override
                    public void success(Result<User> result)
                    {
                       /* System.out.println("BAZOOKA User's Profile URL is " +
                        //result.data.profileImageUrlHttps.replace("_normal", "_bigger"));
                                result.data.profileImageUrlHttps.replace("_normal", ""));*/

                        new DownloadImageTask(picture)
                                .execute(result.data.profileImageUrlHttps.replace("_normal", "_bigger"));
                    }
                    @Override
                    public void failure(TwitterException exception)
                    {

                    }
                });
            }
            public void failure(TwitterException exception) {
                //Do something on failure
                System.out.println("BAZOOKA FAILURE in guest auth");
            }

        });

        //TWITTER PHOTO STUFF

    }

    /*private HashMap<String,String> get_correct_map(String name)
    {
        switch(name)
        {
            case "Ed Labov":return EdLabov;
            case "Hannah Whorf":return HannahWhorf;
            case "Laura Lakoff":return LauraLakoff;
            case "Mark Hagen":return MarkHagen;
            case "Ronan Nash":return RonanNash;
        }
        return new HashMap<String,String>();//should never reach this state.
    }*/

   /* private void fill_maps()
    //Stores the dummy data in all the dictionaries.
    {
        EdLabov = new HashMap<String,String>();
        HannahWhorf = new HashMap<String,String>();
        RonanNash = new HashMap<String,String>();
        MarkHagen = new HashMap<String,String>();
        LauraLakoff = new HashMap<String,String>();

        EdLabov.put("title","Senator Ed Labov");
        HannahWhorf.put("title","Representative Hannah Whorf");
        RonanNash.put("title","Senator Ronan Nash");
        MarkHagen.put("title","Senator Mark Hagen");
        LauraLakoff.put("title","Senator Laura Lakoff");

        EdLabov.put("party","REPUBLICAN");
        HannahWhorf.put("party","DEMOCRAT");
        RonanNash.put("party","DEMOCRAT");
        MarkHagen.put("party","DEMOCRAT");
        LauraLakoff.put("party","REPUBLICAN");

        EdLabov.put("email","edl@gmail.com");
        HannahWhorf.put("email","hanw@gmail.com");
        RonanNash.put("email","ronash@gmail.com");
        MarkHagen.put("email","hagen@gmail.com");
        LauraLakoff.put("email","llakoff@gmail.com");

        EdLabov.put("website","www.voteeddie.com");
        HannahWhorf.put("website","www.hwhorf2016.com");
        RonanNash.put("website","www.nash2016.com");
        MarkHagen.put("website","www.votemarkh.com");
        LauraLakoff.put("website","www.lakoff.com");

        EdLabov.put("picture", String.valueOf(R.drawable.edlabovmin));
        HannahWhorf.put("picture",String.valueOf(R.drawable.hannahwhorf));
        RonanNash.put("picture",String.valueOf(R.drawable.ronannashmin));
        MarkHagen.put("picture",String.valueOf(R.drawable.markhagenmin));
        LauraLakoff.put("picture",String.valueOf(R.drawable.lauralakoff));

        EdLabov.put("tweet", String.valueOf(R.drawable.edtweet));
        HannahWhorf.put("tweet",String.valueOf(R.drawable.hannahtweet));
        RonanNash.put("tweet",String.valueOf(R.drawable.ronantweet));
        MarkHagen.put("tweet",String.valueOf(R.drawable.marktweet));
        LauraLakoff.put("tweet",String.valueOf(R.drawable.lauratweet));

        EdLabov.put("eoft", "1/2/2016");
        HannahWhorf.put("eoft","1/2/2016");
        RonanNash.put("eoft","1/2/2016");
        MarkHagen.put("eoft","1/2/2016");
        LauraLakoff.put("eoft","1/2/2016");

        EdLabov.put("committees", "\n \t\t\t\t\t\t XYZ Committee \n \t\t\t\t\t\t ABC Committee");
        HannahWhorf.put("committees","\n \t\t\t\t\t\t XYZ Committee \n \t\t\t\t\t\t ABC Committee");
        RonanNash.put("committees","\n \t\t\t\t\t\t XYZ Committee \n \t\t\t\t\t\t ABC Committee");
        MarkHagen.put("committees","\n \t\t\t\t\t\t XYZ Committee \n \t\t\t\t\t\t ABC Committee");
        LauraLakoff.put("committees","\n \t\t\t\t\t\t XYZ Committee \n \t\t\t\t\t\t ABC Committee");

        EdLabov.put("bills", "\n \t\t\t\t\t\t XYZ Committee \n \t\t\t\t\t\t ABC Committee");
        HannahWhorf.put("bills","\n \t\t\t\t\t\t XYZ Committee \n \t\t\t\t\t\t ABC Committee");
        RonanNash.put("bills","\n \t\t\t\t\t\t XYZ Committee \n \t\t\t\t\t\t ABC Committee");
        MarkHagen.put("bills","\n \t\t\t\t\t\t XYZ Committee \n \t\t\t\t\t\t ABC Committee");
        LauraLakoff.put("bills","\n \t\t\t\t\t\t XYZ Committee \n \t\t\t\t\t\t ABC Committee");
    }*/

}
