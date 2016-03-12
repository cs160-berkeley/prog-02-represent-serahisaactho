package javis.wearsyncservice;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Does the committtees (0) and bills(1) API call.
 */
public class SunlightDetailTask extends AsyncTask<Object,Void, String[]> {

    private Exception exception;
    private Context mContext;
    private String[] name;
    private String[] bioguide_id;
    private String[] party;
    private String[] title;
    private String[] term_end;
    private int position;
    private String curr_bioguide_id;


    protected void onPreExecute() {
    }

    protected String[] doInBackground(Object... zipcodes) {
        int index = 0;
        mContext = (Context)zipcodes[index]; index++;
        String api_key = (String)zipcodes[index]; index++;
        /*name = (String[])zipcodes[index]; index++;
        bioguide_id = (String[])zipcodes[index]; index++;
        party = (String[])zipcodes[index]; index++;
        title = (String[])zipcodes[index]; index++;
        term_end = (String[])zipcodes[index]; index++;
        position = (Integer)zipcodes[index]; index++;*/
        curr_bioguide_id = (String) zipcodes[3];
        position = (Integer) zipcodes[7];
        //curr_bioguide_id = (String)bioguide_id[position];

        String[] output = new String[2];

        try {
            URL url1 = new URL
                    ("https://congress.api.sunlightfoundation.com/committees?member_ids="+curr_bioguide_id+"&apikey="+api_key);
            URL url2 = new URL
                    ("https://congress.api.sunlightfoundation.com/bills?sponsor_id="+curr_bioguide_id+"&apikey="+api_key);
            HttpURLConnection urlConnection1 = (HttpURLConnection) url1.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection1.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                output[0] = stringBuilder.toString();

            }
            finally{
                urlConnection1.disconnect();
            }

            HttpURLConnection urlConnection2 = (HttpURLConnection) url2.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection2.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                output[1] = stringBuilder.toString();

            }
            finally{
                urlConnection2.disconnect();
            }
            return output;
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }


    protected void onPostExecute(String[] response) {
        if(response == null) {
            System.out.println("BAZOOKA error with bills & committees onPostExecute");
        }
        Intent i = new Intent(mContext, DetailedActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(SunlightInstructions.COMMITTEES, response[0]); //Unparsed String
        i.putExtra(SunlightInstructions.BILLS,response[1]); //Unparsed String
        i.putExtra(SunlightInstructions.BIOGUIDE_IDS, bioguide_id);
        i.putExtra(SunlightInstructions.PARTIES, party);
        i.putExtra(SunlightInstructions.POSITION, position);
        i.putExtra(SunlightInstructions.NAMES, name);
        i.putExtra(SunlightInstructions.TITLES, title);
        i.putExtra(SunlightInstructions.TERM_END, term_end);
        mContext.startActivity(i);
    }
}
