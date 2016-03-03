package javis.wearsyncservice;

import android.app.Activity;
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

/**
 * Custom Adapter to handle different row layouts for odd/even positions
 */
public class ListViewAdapter extends ArrayAdapter {

    private Activity activity;
    List elements; // holds all the elements that occupy rows in the ListView

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
        View v = convertView;
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
        ImageView candidate_img = (ImageView)v.findViewById(R.id.CandiImg);
        int correct_img = set_correct_img(txt);
        candidate_img.setImageResource(correct_img);

        return v;
        //TODO: 136px is the magic number for the images in condensed view.
    }

    private int set_correct_img(String person_name)
    {
        /*String file_name = person_name.toLowerCase();
        file_name.replaceAll("\\s+","");
        //to remove the whitespace between first & last name
        return R.id.(file_name.toString());
        */
        //TODO: Find a better way to abstract finding the resources.
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