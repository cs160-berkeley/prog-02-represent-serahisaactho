package javis.wearsyncservice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.CompactTweetView;

import io.fabric.sdk.android.Fabric;

import java.util.HashMap;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

//import edu.berkeley.cs160.plswork.R;

public class CustomDialog extends DialogFragment {


    /**The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface CustomDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    CustomDialogListener mListener;
    String[] bioguide_id;
    int position;
    String[] name; //holds all the candidate names in order
    String[] party; //holds all the parties in order
    String[] title; //holds either Senator or Representative // take from title tag in json object
    String[] email;// oc_email tag
    String[] website;// website tag
    String[] twitter_id; //twitter_id tag
    String[] term_end; //term_end tag

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "xPBH74d17OPYBRSyOiAbo8HlN";
    private static final String TWITTER_SECRET = "mg6OvvLt0xsmCmn67FqutOTrT4rsFAcIflaNGWEYStLvLj89HI";

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the CustomDialogListener so we can send events to the host
            mListener = (CustomDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement CustomDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Bundle mArgs = getArguments(); //Does this go here??
        String clicked_name = mArgs.getString("clicked_name");
        bioguide_id = mArgs.getStringArray("bioguide_id");
        position = mArgs.getInt("position");
        name = mArgs.getStringArray("name");
        party = mArgs.getStringArray("party");
        title = mArgs.getStringArray("title");
        email = mArgs.getStringArray("email");
        website = mArgs.getStringArray("website");
        twitter_id = mArgs.getStringArray("twitter_id");
        term_end = mArgs.getStringArray("term_end");
        //TODO: Add coloured stroke around the candidate image.

        //fill_maps(); //stores all the data before it needs to be accessed.

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        //builder.setView(R.layout.screen_split); //Sets the custom layout for the layout
        final View w = inflater.inflate(R.layout.screen_split, null);
        builder.setView(w);
        TextView title_widget = (TextView)w.findViewById(R.id.Title);
        TextView party_widget = (TextView)w.findViewById(R.id.Party);
        TextView email_widget = (TextView)w.findViewById(R.id.Email);
        TextView website_widget = (TextView)w.findViewById(R.id.Website);
        final ImageView picture_widget = (ImageView)w.findViewById(R.id.overlapImage);
        //ImageView tweet_widget = (ImageView)w.findViewById(R.id.Tweet);
        ImageButton mail_icon = (ImageButton)w.findViewById(R.id.MailIcon);
        ImageButton home_icon = (ImageButton)w.findViewById(R.id.HomeIcon);
        //HashMap<String,String> info = get_correct_map(clicked_name);

        title_widget.setText(title[position]+" "+name[position]);
        party_widget.setText(party[position]);
//        picture.setImageResource(Integer.parseInt(info.get("picture")));
//        tweet.setImageResource(Integer.parseInt(info.get("tweet")));

        LinearLayout top = (LinearLayout)w.findViewById(R.id.layoutTop);

        if (party[position]=="DEMOCRAT")
        {
            party_widget.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.blue));
            mail_icon.setImageResource(R.drawable.blueenvelope);
            home_icon.setImageResource(R.drawable.bluehome);
            top.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.blue));
        }
        else if (party[position]=="REPUBLICAN")
        {
            party_widget.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.red));
            mail_icon.setImageResource(R.drawable.mailredenvelope);
            home_icon.setImageResource(R.drawable.redhome);
            top.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.red));
        }
        //email_widget.setText(email[position]);
        //website_widget.setText(website[position]);
        website_widget.setClickable(true);
        website_widget.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='"+website[position]+"'>"+ website[position] +"</a>";
        website_widget.setText(Html.fromHtml(text));
        email_widget.setClickable(true);
        email_widget.setText(Html.fromHtml("<a href=\"mailto:"+email[position]+"\">"+email[position]+"</a>"));
        email_widget.setMovementMethod(LinkMovementMethod.getInstance());

        //EMBED TWEET STUFF
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(getActivity(), new Twitter(authConfig));

        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result appSessionResult) {
                AppSession guestAppSession = (AppSession) appSessionResult.data;

                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(guestAppSession);
                //SearchService service = twitterApiClient.getSearchService();
                //service.tweets("crime", null, "en", null, null, null, null, null, null, true, new Callback<Search>()
                StatusesService service = twitterApiClient.getStatusesService();
                int count = 1;
                final LinearLayout myLayout
                        = (LinearLayout)w.findViewById(R.id.my_tweet_layout);
                service.userTimeline(null, twitter_id[position] ,count, null, null, null, null, null, null, new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> result) {
                        System.out.println("BAZOOKA YUM SUCCESS");
                        final List<Tweet> tweets = result.data;
                        for (Tweet tweet : tweets) {
                            /*TweetView v = (TweetView)findViewById(R.id.bike_tweet);
                            v.setTweet(tweet);*/
                            myLayout.addView(new CompactTweetView(getActivity(), tweet));
                            //String x = tweet.text + "\n\n";
                            //System.out.println("BAZOOKA x = " + x);
                        }
                    }

                    @Override
                    public void failure(TwitterException e) {
                        System.out.println("BAZOOKA Failure " + e.getMessage());
                    }
                });
            }

            public void failure(TwitterException exception) {
                //Do something on failure
                System.out.println("BAZOOKA FAILURE in guest auth");
            }

        });

        //EMBED TWEET STUFF


        //TWITTER IMAGE STUFF
        TwitterCore.getInstance().logInGuest(new com.twitter.sdk.android.core.Callback<AppSession>() {
            @Override
            public void success(Result appSessionResult) {
                AppSession guestAppSession = (AppSession) appSessionResult.data;
                //TwitterSession guestAppSession = (TwitterSession) appSessionResult.data;

                new MyTwitterApiClient(guestAppSession).getUsersService().show(null, twitter_id[position], true, new com.twitter.sdk.android.core.Callback<User>() {
                    @Override
                    public void success(Result<User> result)
                    {
                        new DownloadImageTask(picture_widget)
                                .execute(result.data.profileImageUrlHttps.replace("_normal", "_bigger"));
                        //The original image was 200 x 200
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

        //TWITTER IMAGE STUFF
        return builder.create();
    }

   /* private HashMap<String,String> get_correct_map(String name)
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
    }
*/
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

        EdLabov.put("committees", "XYZ Committee \n \t\t\t\t\t\t ABC Committee");
        HannahWhorf.put("committees","XYZ Committee \n \t\t\t\t\t\t ABC Committee");
        RonanNash.put("committees","XYZ Committee \n \t\t\t\t\t\t ABC Committee");
        MarkHagen.put("committees","XYZ Committee \n \t\t\t\t\t\t ABC Committee");
        LauraLakoff.put("committees","XYZ Committee \n \t\t\t\t\t\t ABC Committee");

        EdLabov.put("bills", "XYZ Committee \n \t\t\t\t\t\t ABC Committee");
        HannahWhorf.put("bills","XYZ Committee \n \t\t\t\t\t\t ABC Committee");
        RonanNash.put("bills","XYZ Committee \n \t\t\t\t\t\t ABC Committee");
        MarkHagen.put("bills","XYZ Committee \n \t\t\t\t\t\t ABC Committee");
        LauraLakoff.put("bills","XYZ Committee \n \t\t\t\t\t\t ABC Committee");
    }*/

}