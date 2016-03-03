package javis.wearsyncservice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    HashMap<String,String> EdLabov;
    HashMap<String,String> LauraLakoff;
    HashMap<String,String> HannahWhorf;
    HashMap<String,String> MarkHagen;
    HashMap<String,String> RonanNash;
    //TODO: Find a better way to store & reference this dictionary of dummy data

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

    //TODO: WHat does the email and website with link have to do specifically?
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Bundle mArgs = getArguments(); //Does this go here??
        String clicked_name = mArgs.getString("clicked_name");
        //TODO: Add coloured stroke around the candidate image.

        fill_maps(); //stores all the data before it needs to be accessed.

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        //builder.setView(R.layout.screen_split); //Sets the custom layout for the layout
        View w = inflater.inflate(R.layout.screen_split, null);
        builder.setView(w);
        TextView title = (TextView)w.findViewById(R.id.Title);
        TextView party = (TextView)w.findViewById(R.id.Party);
        TextView email = (TextView)w.findViewById(R.id.Email);
        TextView website = (TextView)w.findViewById(R.id.Website);
        ImageView picture = (ImageView)w.findViewById(R.id.overlapImage);
        ImageView tweet = (ImageView)w.findViewById(R.id.Tweet);
        ImageButton mail_icon = (ImageButton)w.findViewById(R.id.MailIcon);
        ImageButton home_icon = (ImageButton)w.findViewById(R.id.HomeIcon);
        HashMap<String,String> info = get_correct_map(clicked_name);

        title.setText(info.get("title"));
        party.setText(info.get("party"));
        picture.setImageResource(Integer.parseInt(info.get("picture")));
        tweet.setImageResource(Integer.parseInt(info.get("tweet")));

        LinearLayout top = (LinearLayout)w.findViewById(R.id.layoutTop);

        if (party.getText()=="DEMOCRAT")
        {
            party.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.blue));
            mail_icon.setImageResource(R.drawable.blueenvelope);
            home_icon.setImageResource(R.drawable.bluehome);
            top.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.blue));
        }
        else if (party.getText()=="REPUBLICAN")
        {
            party.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.red));
            mail_icon.setImageResource(R.drawable.mailredenvelope);
            home_icon.setImageResource(R.drawable.redhome);
            top.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.red));
        }
        email.setText(info.get("email"));
        website.setText(info.get("website"));

//        builder.setTitle(R.string.app_name)
//                //.setMessage(R.string.dialog_fire_missiles)
//                .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // FIRE ZE MISSILES!
//                        mListener.onDialogPositiveClick(CustomDialog.this);
//                    }
//                })
//                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User cancelled the dialog
//                        mListener.onDialogNegativeClick(CustomDialog.this);
//                    }
//                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private HashMap<String,String> get_correct_map(String name)
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

    private void fill_maps()
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
    }

}