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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class SunlightTask extends AsyncTask<Object,Void,String> {

    private Exception exception;
    private Context mContext;
    private String location; //to pass for the toast in MainActivity

    protected void onPreExecute() {
        //progressBar.setVisibility(View.VISIBLE);
        //responseView.setText("");
    }

    // protected String doInBackground(Void... urls) {
    //Can make it take in Object type so can be of different types
    protected String doInBackground(Object... zipcodes) {
        int index = 0;
        mContext = (Context)zipcodes[index]; index++;
        String instruction = (String)zipcodes[index]; index++;
        String api_key = (String)zipcodes[index]; index++;

        try {
            URL url = get_url(instruction, api_key, index, zipcodes);//NOT UPDATING INDEX AFTER THIS

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }


    private URL get_url(String instruction, String api_key, int start_index, Object[] zipcodes)
            /*
            Returns the correct URL based on the instruction. Starts extracting
            instruction specfic parameters from start_index
             */
    {
        URL url = null;
        try {
            if (instruction == SunlightInstructions.CURRENT_LOC) {
                String latitude = (String) zipcodes[start_index];
                String longitude = (String) zipcodes[start_index+1];
                location = "Current Location";
                url = new URL("https://congress.api.sunlightfoundation.com/legislators/locate?latitude=" + latitude +
                        "&longitude=" + longitude + "&apikey=" + api_key);
            } else if (instruction == SunlightInstructions.CUSTOM_ZIP) {
                String zip = (String) zipcodes[start_index];
                location = zip;
                url = new URL("https://congress.api.sunlightfoundation.com/legislators" +
                        "/locate?zip=" + zip + "&apikey=" + api_key);
            }
        }
        catch(Exception e)
        {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
        return url;
    }

    protected void onPostExecute(String response) {
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
        Intent i = new Intent(mContext, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(SunlightInstructions.CONGRESS_JSON, response);
        i.putExtra("Toast", "False");
        i.putExtra("Zipcode", location);
        mContext.startActivity(i);
    }
}

