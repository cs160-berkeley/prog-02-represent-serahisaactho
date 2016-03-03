package javis.wearsyncservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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

import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {


    HashMap<String,String> EdLabov;
    HashMap<String,String> LauraLakoff;
    HashMap<String,String> HannahWhorf;
    HashMap<String,String> MarkHagen;
    HashMap<String,String> RonanNash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        View w = inflater.inflate(R.layout.activity_detailed, null);
        setContentView(w);
        //setContentView(R.layout.activity_detailed);
        //TODO: What happens when you press the hardware back button on this screen?
        //TODO: Change action bar colour. It is atrocious!!!!

        Intent intent = getIntent();
        String clicked_name = intent.getStringExtra("KEY"); //secured the clicked name from the dialog.

        fill_maps();

        fill_fields(clicked_name, w); //stores the correct information for each field.


    }


    private void fill_fields(String clicked_name, View w)
    {
        TextView title = (TextView)w.findViewById(R.id.Title);
        TextView party = (TextView)w.findViewById(R.id.Party);
        TextView eoft = (TextView)w.findViewById(R.id.EofT);
        TextView committees = (TextView)w.findViewById(R.id.Committees);
        TextView bills = (TextView)w.findViewById(R.id.Bills);
        ImageView picture = (ImageView)w.findViewById(R.id.overlapImage);
        LinearLayout top = (LinearLayout)w.findViewById(R.id.layoutTop);

        HashMap<String,String> info = get_correct_map(clicked_name);
        title.setText(info.get("title"));
        party.setText(info.get("party"));
        eoft.setText("End of Term: " + info.get("eoft"));
        committees.setText("Currently serving on:-" + info.get("committees"));
        bills.setText("Sponsored Bills(with date of introduction):-" + info.get("bills"));
        picture.setImageResource(Integer.parseInt(info.get("picture")));
        if (party.getText()=="DEMOCRAT")
        {
            party.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
            top.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
        }
        else if (party.getText()=="REPUBLICAN")
        {
            party.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
            top.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
        }

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
    }

}
