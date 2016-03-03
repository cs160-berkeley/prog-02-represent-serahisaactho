package javis.wearsyncservice;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements CustomDialog.CustomDialogListener{
// extends AppCompatActivity {

    private TextView textView;
    private EditText editText;
    private String last_clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2); //This is the layout with the listView

        //ON SHAKE TOAST STUFF
        Intent intent = getIntent();
        String need_toast = intent.getStringExtra("Toast");
        /*Toast t = Toast.makeText(getApplicationContext(), "Random Location out: XYZ", Toast.LENGTH_SHORT);
        t.show();*/
        if (need_toast!=null && need_toast.equals("True")) //TODO:Change to boolean values
        {
            Toast t1 = Toast.makeText(getApplicationContext(),"Random Location: XYZ",Toast.LENGTH_SHORT);
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
        String[] values = new String[] { "Laura Lakoff","Ronan Nash","Ed Labov","Hannah Whorf","Mark Hagen"};

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
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
                CustomDialog d =new CustomDialog();
                Bundle b = new Bundle();
                b.putString("clicked_name", item);
                d.setArguments(b);
                d.show(getFragmentManager(),item);
                fireMessage(item);
            }

        });
    }

    public void move_to_detail(View w)
    {
        Intent i = new Intent(this, DetailedActivity.class);
        i.putExtra("KEY", last_clicked);
        startActivity(i);
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