package javis.wearsyncservice;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.User;

import io.fabric.sdk.android.Fabric;

/**
 * Custom Adapter to handle different row layouts for odd/even positions
 */
public class ListViewAdapter extends ArrayAdapter {

    private Activity activity;
    List elements; // holds all the elements that occupy rows in the ListView
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "xPBH74d17OPYBRSyOiAbo8HlN";//"plnyQCLPdJjOhAdaEkfQH4a73";
    private static final String TWITTER_SECRET = "mg6OvvLt0xsmCmn67FqutOTrT4rsFAcIflaNGWEYStLvLj89HI";//"MbwjceC8iA8vOSNaRYq2GDGWD6r9kmvlpTcD2j1qmWHm3MM8ji";

    public ListViewAdapter(Activity activity, int resource, List objects) {
        super(activity, resource, objects);
        this.activity = activity;
        this.elements = objects;
    }

    @Override
    public int getViewTypeCount() {
        // return the total number of view types. this value should never change
        // at runtime
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        return position % 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //View v = convertView;
        final View v;
        //TODO: Add line stroke around the shape rows for the list view
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        int layoutResource = 0; // determined by view type
        int viewType = getItemViewType(position);

        if (viewType == 0) { //use even row layout
            //view.setBackgroundColor(Color.BLACK);
            // layoutResource = R.layout.even_row_layout;
            v = inflater.inflate(R.layout.even_row_layout, parent, false);
        } else { //use odd row layout
            //view.setBackgroundColor(Color.WHITE);
            //layoutResource = R.layout.odd_row_layout;
            v = inflater.inflate(R.layout.odd_row_layout, parent, false);
        }
        String txt = (String) elements.get(position);
        //implicit understanding that all the elements are String types.
        TextView candidate_name = (TextView)v.findViewById(R.id.Name);// exists in both layouts!!
        candidate_name.setText(txt);
        //ImageView candidate_img = (ImageView)v.findViewById(R.id.CandiImg);

        //TWITTER CALL STUFF
        final int final_position = position;
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(activity, new Twitter(authConfig)); //Is this the right context?

        TwitterCore.getInstance().logInGuest(new com.twitter.sdk.android.core.Callback<AppSession>() {
            @Override
            public void success(Result appSessionResult) {
                AppSession guestAppSession = (AppSession) appSessionResult.data;
                //TwitterSession guestAppSession = (TwitterSession) appSessionResult.data;

                new MyTwitterApiClient(guestAppSession).getUsersService().show(null, MainActivity.twitter_id[final_position], true, new com.twitter.sdk.android.core.Callback<User>() {
                    @Override
                    public void success(Result<User> result)
                    {
                        new DownloadImageTask((ImageView) v.findViewById(R.id.CandiImg))
                                .execute(result.data.profileImageUrlHttps.replace("_normal", "_bigger"));
                        //The image dimensions used to be 136x136
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

        //TWITTER CALL STUFF


        //int correct_img = set_correct_img(txt);
        //candidate_img.setImageResource(correct_img);

        return v;
    }

    private int set_correct_img(String person_name)
    {
        /*String file_name = person_name.toLowerCase();
        file_name.replaceAll("\\s+","");
        //to remove the whitespace between first & last name
        return R.id.(file_name.toString());
        */
        switch(person_name)
        {
            case "Ed Labov": return R.drawable.edlabovmin;
            case "Hannah Whorf": return R.drawable.hannahwhorf;
            case "Laura Lakoff": return R.drawable.lauralakoff;
            case "Mark Hagen": return R.drawable.markhagenmin;
            case "Ronan Nash": return R.drawable.ronannashmin;
        }
        return -1;//Error case
    }

}